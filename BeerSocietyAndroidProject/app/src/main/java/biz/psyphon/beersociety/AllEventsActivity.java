package biz.psyphon.beersociety;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import biz.psyphon.beersociety.VolleyRequests.AddEventRequest;
import biz.psyphon.beersociety.VolleyRequests.AllEventRequest;

public class AllEventsActivity extends AppCompatActivity {

    public static final String TAG = AllEventsActivity.class.getSimpleName();

    private ArrayList<EventItem> eventItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_events);

        final ListView lv1 = (ListView) findViewById(R.id.allEventList);
        final Button myEvents = (Button) findViewById(R.id.bMyEvents);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String retrievedAuthToken = sharedPreferences.getString(getString(R.string.tokenKey), null);

        final CustomListAdapter adapter = new CustomListAdapter(AllEventsActivity.this, eventItems);

        myEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllEventsActivity.this, UserAreaActivity.class);
                AllEventsActivity.this.startActivity(intent);
            }
        });

        final Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    boolean fullDoc = jsonResponse.getBoolean("fulldoc");


                    if(!fullDoc) {
                        JSONArray eventsArray = jsonResponse.getJSONArray("events");

                        for (int j = 0; j < eventsArray.length(); j++) {
                            EventItem eventItem = new EventItem();
                            JSONObject row = eventsArray.getJSONObject(j);
                            eventItem.setName(row.getString("title"));
                            if (row.getString("body") != null && !row.getString("body").isEmpty()) {
                                eventItem.setDescription(row.getString("body"));
                            }
                            eventItem.setId(row.getString("_id"));

                            eventItems.add(eventItem);
                        }

                        if (success) {
                            Log.e(TAG, response);

                            adapter.setListData(eventItems);
                            lv1.setAdapter(adapter);


                        } else {
                            Toast.makeText(AllEventsActivity.this, "There was an error with your request",
                                    Toast.LENGTH_LONG).show();
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        AllEventRequest allEventRequest = new AllEventRequest(retrievedAuthToken, responseListener);
        final RequestQueue queue = Volley.newRequestQueue(AllEventsActivity.this);
        queue.add(allEventRequest);

        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object listObject = lv1.getItemAtPosition(i);
                EventItem eventItem = (EventItem) listObject;
                Toast.makeText(AllEventsActivity.this, "Adding " + eventItem.getName() + " to your events",
                        Toast.LENGTH_LONG).show();
                AddEventRequest addEventRequest = new AddEventRequest(eventItem.getId(), retrievedAuthToken,
                        responseListener);
                queue.add(addEventRequest);

            }
        });
    }

}
