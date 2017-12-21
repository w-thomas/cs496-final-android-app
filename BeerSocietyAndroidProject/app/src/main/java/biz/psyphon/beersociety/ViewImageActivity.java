package biz.psyphon.beersociety;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ViewImageActivity extends AppCompatActivity {

    public static final String TAG = ViewImageActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image2);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);


        Intent intent = getIntent();
        Uri imageUri = intent.getData();

        Picasso.with(this).load(imageUri).into(imageView);
    }
}
