package edu.msu.steve702.ua_quality_assurance_platform;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import edu.msu.steve702.ua_quality_assurance_platform.activities.AuditActivity;


public class ImageDisplayActivity extends AppCompatActivity {

    private ArrayList<byte[]> photos = new ArrayList<>();

    private Button return_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

        return_button = findViewById(R.id.buttonReturn);

        return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();

        photos = (ArrayList<byte[]>) extras.getSerializable("PHOTOS");


        if(photos.size() > 0){
            displayGallery();
        }
    }

    public void displayGallery(){

        // Get the view pager to display the image
        FrameLayout display = findViewById(R.id.imageFrame);

        // Add an image view for each photo
        for(byte[] photo : photos){

            Bitmap bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);

            ImageView imageView = new ImageView(this);

            imageView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

            imageView.setImageBitmap(bitmap);

            display.addView(imageView);


        }
    }

}
