package biz.psyphon.beersociety;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import biz.psyphon.beersociety.VolleyRequests.RemoveEventRequest;
import biz.psyphon.beersociety.VolleyRequests.myEventRequest;

public class UserAreaActivity extends AppCompatActivity {

    public static final String TAG = UserAreaActivity.class.getSimpleName();

    private String removalId;
    private boolean removeCheck = false;
    private ArrayList<EventItem> eventItems = new ArrayList<>();

    private static final int REQUEST_TAKE_PHOTO = 0;
    private static final int REQUEST_PICK_VIDEO = 3;
    private static final int MEDIA_TYPE_IMAGE = 5;

    private Uri mMediaUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);
        final ListView lv1 = (ListView) findViewById(R.id.custom_list);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String retrievedAuthToken = sharedPreferences.getString(getString(R.string.tokenKey), null);
        Log.e(TAG, retrievedAuthToken);

        final TextView welcomeMessage = (TextView) findViewById(R.id.tvWelcomeMsg);
        final Button bLogOut = (Button) findViewById(R.id.bLogOut);
        final Button bAllEvents = (Button) findViewById(R.id.bAllEvents);
        final Button bTakePhoto = (Button) findViewById(R.id.bTakePhoto);
        final CustomListAdapter adapter = new CustomListAdapter(UserAreaActivity.this, eventItems);

        final TextView userName = (TextView) findViewById(R.id.tvName);



        bLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(getString(R.string.tokenKey));
                editor.commit();

                String retrievedAuthToken = sharedPreferences.getString(getString(R.string.tokenKey), null);

                Intent intent = new Intent(UserAreaActivity.this, LoginActivity.class);
                UserAreaActivity.this.startActivity(intent);
            }
        });

        bAllEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserAreaActivity.this, AllEventsActivity.class);
                UserAreaActivity.this.startActivity(intent);
            }
        });

        bTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                if (mMediaUri == null) {
                    Toast.makeText(UserAreaActivity.this, "There was a problem accessing your device's external storage",
                            Toast.LENGTH_LONG).show();
                } else {
                    Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                    startActivityForResult(takePhotoIntent, REQUEST_TAKE_PHOTO);
                }
            }
        });

        final Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONObject user = jsonResponse.getJSONObject("user");
                    JSONArray eventsArray = user.getJSONArray("events");
                    boolean success = jsonResponse.getBoolean("success");
                    String name = user.getString("name");
                    userName.setText(name);


                    for(int j = 0; j<eventsArray.length(); j++) {
                        EventItem eventItem = new EventItem();
                        JSONObject row = eventsArray.getJSONObject(j);
                        eventItem.setName(row.getString("title"));
                        if(row.getString("body")!=null && !row.getString("body").isEmpty()) {
                            eventItem.setDescription(row.getString("body"));
                        }
                        eventItem.setId(row.getString("_id"));

                        eventItems.add(eventItem);
                    }


                    if(success){
                        Log.e(TAG, response);

                        adapter.setListData(eventItems);
                        lv1.setAdapter(adapter);



                    } else {
                        //Do something else
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        final RequestQueue queue = Volley.newRequestQueue(UserAreaActivity.this);
        myEventRequest myEventRequest = new myEventRequest(retrievedAuthToken, responseListener);
        queue.add(myEventRequest);

        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object o = lv1.getItemAtPosition(i);
                EventItem eventItem = (EventItem) o;
//                                Log.e(TAG, eventItem.getId());

                removalId = eventItem.getId();
                removeCheck = true;
                Log.e(TAG, "This is the ID: " + removalId);
                Toast.makeText(UserAreaActivity.this, "Selected: " + " " + eventItem, Toast.LENGTH_LONG).show();
                eventItems.remove(lv1.getItemAtPosition(i));
                adapter.notifyDataSetChanged();

                RemoveEventRequest removeEventRequest = new RemoveEventRequest(removalId, retrievedAuthToken,
                        responseListener);
                queue.add(removeEventRequest);
            }
        });

    }

    private Uri getOutputMediaFileUri(int mediaType) {
        // check for external storage
        if(isExternalStorageAvailable()) {
            // get the URI

            //1. Get external directory
            File mediaStorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            //2. Create unique file name
            String fileName = "";
            String fileType = "";
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            if (mediaType == MEDIA_TYPE_IMAGE) {
                fileName = "IMG_"+ timeStamp;
                fileType = ".jpg";
            } else {
                return null;
            }

            //3. Create the file
            File mediaFile = null;
            try {
                mediaFile = File.createTempFile(fileName, fileType, mediaStorageDir);
                Log.i(TAG, "File" + Uri.fromFile(mediaFile));

                 //4. return the file's URI
                return Uri.fromFile(mediaFile);
            } catch (IOException e) {
                e.printStackTrace();
            }



        }

        //something went wrong
        return null;
    }

    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                Intent intent = new Intent(this, ViewImageActivity.class);
                intent.setData(mMediaUri);
                startActivity(intent);
            }

        } else if (resultCode != RESULT_CANCELED) {
            Toast.makeText(UserAreaActivity.this, "Sorry, there was an error!",
                    Toast.LENGTH_LONG).show();
        }
    }

}
