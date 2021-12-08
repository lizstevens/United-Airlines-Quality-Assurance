package edu.msu.steve702.ua_quality_assurance_platform;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.io.FilenameFilter;

import edu.msu.steve702.ua_quality_assurance_platform.activities.CheckListListActivity;
import edu.msu.steve702.ua_quality_assurance_platform.activities.EditAuditListActivity;
import edu.msu.steve702.ua_quality_assurance_platform.activities.pdfActivity;

/**
 * MainActivity Class
 * Activity that displays the main dashboard of the application and handles all dashboard functions.
 */
public class MainActivity extends AppCompatActivity {

    /** New Audit Button View **/
    private Button newAuditButton;
    /** Edit Audit Button View **/
    private Button editAuditButton;

    /**
     * Function for creating the Main Acitivity
     * @param savedInstanceState the saved instance state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newAuditButton = (Button)findViewById(R.id.createAudit);
        newAuditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewAuditActivity();
            }
        });

        editAuditButton = (Button)findViewById(R.id.editAudit);
        editAuditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEditAuditActivity();
            }
        });
    }

    /**
     * Function for starting a new Audit Activity
     */
    public void startNewAuditActivity() {
        Intent intent = new Intent(this, CheckListListActivity.class);
        startActivity(intent);
    }

    /**
     * Function for starting an edit Audit Activity
     */
    public void startEditAuditActivity() {
        startActivity(new Intent(this, EditAuditListActivity.class));
    }

    /**
     * Function for resuming the Activity
     */
    @Override
    protected void onResume() {
        super.onResume();
        File f = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);


        File[] files = f.listFiles(new FilenameFilter(){
            @Override
            public boolean accept(File file, String s) {
                return s.matches("[0-9]{4}-[0-3][0-9]-[0-9]{2}[R0-9]*-*[ ()0-9]*.pdf");
            }
        });

        LinearLayout layout = (LinearLayout) findViewById(R.id.FAAData);

        // Check if table already exists
        if(findViewById(R.id.RegTable) != null){
            layout.removeView(findViewById(R.id.RegTable));
        }

        if(files.length > 0){

            // Add table layout

            TableLayout tl = new TableLayout(this);

            tl.setId(R.id.RegTable);

            tl.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT));

            tl.setStretchAllColumns(true);

            // Add header
            TableRow header = new TableRow(this);

            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setColor(getResources().getColor(R.color.UnitedBlue));


            shape.setCornerRadii(new float[]{10.0f, 10.0f,
                    10.0f, 10.0f,
                    0.0f, 0.0f,
                    0.0f, 0.0f
            });
            shape.mutate();

            header.setBackgroundDrawable(shape);

            TextView file = new TextView(this);
            file.setText("Airworthiness Directives");
            file.setTextColor(getColor(android.R.color.white));
            file.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            file.setTextSize(12);
            file.setTypeface(null, Typeface.BOLD);
            header.addView(file);


            TextView delete = new TextView(this);
            delete.setText("Delete");
            delete.setTextColor(getColor(android.R.color.white));
            delete.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            delete.setTextSize(12);
            delete.setTypeface(null, Typeface.BOLD);
            header.addView(delete);

            tl.addView(header);

            for(int i=0; i < files.length; i++){
                TableRow tableRow = new TableRow(this);
                TextView fileName = new TextView(this);
                fileName.setText(files[i].getName());
                fileName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                fileName.setTextSize(12);

                int finalI = i;
                fileName.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {


                        Intent intent = new Intent(MainActivity.this,
                                pdfActivity.class);
                        intent.putExtra("URI", Uri.fromFile(files[finalI]).toString());
                        startActivity(intent);



                    }
                });

                tableRow.addView(fileName);

                Button button = new Button(this);
                button.setText("Delete");
                button.setTextSize(12);
                button.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

                button.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {

                        // Delete associated file in downloads directory
                        File deleteFile = files[finalI];

                        boolean deleted = deleteFile.delete();

                        // Restart activity
                        onResume();


                    }
                });

                tableRow.addView(button);

                tl.addView(tableRow);
            }

            layout.addView(tl);
        }

    }

    /**
     * Function for getting the regulation documents
     * @param view the view of the FAA regulations
     */
    public void onGetRegulations(View view){
        Intent httpIntent = new Intent(Intent.ACTION_VIEW);
        httpIntent.setData(Uri.parse("http://35.9.22.101"));

        startActivity(httpIntent);
    }

}
