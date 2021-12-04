package edu.msu.steve702.ua_quality_assurance_platform;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class ImageDisplayActivity extends AppCompatActivity {

    private ArrayList<byte[]> photos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);


        if(savedInstanceState != null){

            photos = (ArrayList<byte[]>) savedInstanceState.getSerializable("PHOTOS");
        }

        if(photos.size() > 0){

        }
    }

    public void displayGallery(){

        // Add an image view for each photo
        for(byte[] photo : photos){

            Bitmap bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);


        }
    }
}
