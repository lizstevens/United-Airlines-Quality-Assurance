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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;


/**
 * ImageDisplayActivity Class
 * Class for displaying an image in the image activity.
 */
public class ImageDisplayActivity extends AppCompatActivity {

    /** byte array of photos **/
    private ArrayList<byte[]> photos = new ArrayList<>();
    /** return button **/
    private Button return_button;
    /** photo size **/
    private int photoSize;
    /** boolean to delete a photo **/
    private boolean delete;
    /** the toolbar **/
    private Toolbar mToolbar;
    /** list of images to delete **/
    private ArrayList<Integer> toDelete = new ArrayList<>();

    /**
     * Function to create the activity
     * @param savedInstanceState the saved instance state
     */
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

    /**
     * Function to display the photo gallery
     */
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
            imageView.setAdjustViewBounds(true);

            imageView.setImageBitmap(bitmap);


            display.addView(imageView);


        }

    }

    /**
     * Function to delete an image
     */
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

    /**
     * Function close the activity
     */
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

    /**
     * function to destroy the activity
     */
    @Override
    public void onBackPressed() {
        close();
    }


    /**
     * Function to create the menu options for this activity
     * @param menu the menu
     * @return boolean representing if the menu is created
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_gallery, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Function called when a menu option is selected
     * @param item the item selected
     * @return boolean representing if the item was selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

      item.setChecked(!delete);

      delete = item.isChecked();

      deleteImages();

      return true;

    }

}
