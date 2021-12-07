package edu.msu.steve702.ua_quality_assurance_platform;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import edu.msu.steve702.ua_quality_assurance_platform.activities.AuditActivity;


public class ImageDisplayActivity extends AppCompatActivity {

    private ArrayList<byte[]> photos = new ArrayList<>();

    private Button return_button;

    private int photoSize;

    private boolean delete;

    private Toolbar mToolbar;

    private ArrayList<Integer> toDelete = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

        return_button = findViewById(R.id.buttonReturn);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        delete = false;


        return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });

        // Retrieve how many photos are in internal storage
        Bundle extras = getIntent().getExtras();

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
            lp.setMargins(1, 1, 1, 1);
            imageView.setLayoutParams(lp);

            imageView.setImageBitmap(bitmap);


            display.addView(imageView);


        }

    }

    public void deleteImages() {

        LinearLayout layout = findViewById(R.id.imageLayout);

        for (int i=0; i < layout.getChildCount(); i++) {

            View v = layout.getChildAt(i);

            int index = i;

            // Delete images when selected
            v.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {

                    if(delete){
                        ((ViewGroup)view.getParent()).removeView(view);
                        toDelete.add(index);
                    }
                }
            });






        }
    }

    public void close(){

        // Delete files from internal storage
        for(int i=0; i < photoSize; i++) {

            deleteFile("photo" + i + ".jpeg");

        }

        // Close application and return to audit activity
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", toDelete);
        setResult(Activity.RESULT_OK, returnIntent);

        finish();
    }

    @Override
    public void onBackPressed() {
        close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_gallery, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

      item.setChecked(!delete);

      delete = item.isChecked();

      deleteImages();

      return true;

    }

}
