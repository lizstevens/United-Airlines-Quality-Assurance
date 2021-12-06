package edu.msu.steve702.ua_quality_assurance_platform;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;


public class ImageDisplayActivity extends AppCompatActivity {

    private ArrayList<byte[]> photos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

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
