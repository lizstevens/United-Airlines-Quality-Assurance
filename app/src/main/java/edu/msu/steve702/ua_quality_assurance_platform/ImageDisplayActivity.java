package edu.msu.steve702.ua_quality_assurance_platform;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import edu.msu.steve702.ua_quality_assurance_platform.activities.AuditActivity;


public class ImageDisplayActivity extends AppCompatActivity {

    private ArrayList<byte[]> photos = new ArrayList<>();

    private Button return_button;

    private int photoSize;

    private boolean delete = true;

    private ArrayList<Integer> toDelete = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

        return_button = findViewById(R.id.buttonReturn);

        return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });

        Bundle extras = getIntent().getExtras();

//        photos = (ArrayList<byte[]>) extras.getSerializable("PHOTOS");
        photoSize = extras.getInt("SIZE");




        if(photoSize > 0){

            displayGallery();
        }


    }

    public void displayGallery(){

        // Get the view pager to display the image
        ScrollView scrollView = findViewById(R.id.imageScroll);

        LinearLayout display = findViewById(R.id.imageLayout);



        // Add an image view for each photo
        for(int i=0; i < photoSize; i++){

            FileInputStream infile;
            try {
                infile = openFileInput("photo" + i + ".jpeg");
            } catch (FileNotFoundException ex) {
                return;
            }

            Bitmap bitmap = BitmapFactory.decodeStream(infile);

            ImageView imageView = new ImageView(this);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lp.setMargins(10, 10, 10, 10);
            imageView.setLayoutParams(lp);

            imageView.setImageBitmap(bitmap);

            int index = i;

            imageView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    if(delete){

                        ((ViewGroup)view.getParent()).removeView(view);
                        toDelete.add(index);
                    }
                }
            });

            display.addView(imageView);


        }

    }

    public void close(){

        // Delete files from internal storage
        for(int i=0; i < photoSize; i++) {

            deleteFile("photo" + i + ".jpeg");

        }

        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", toDelete);
        setResult(Activity.RESULT_OK, returnIntent);

        finish();
    }

}
