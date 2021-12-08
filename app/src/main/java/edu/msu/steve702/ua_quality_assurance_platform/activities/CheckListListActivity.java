package edu.msu.steve702.ua_quality_assurance_platform.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import java.io.IOException;
import java.util.ArrayList;

import edu.msu.steve702.ua_quality_assurance_platform.R;

/**
 * CheckListListActivity Class
 * This class handles listing the checklists that are available to use for an audit for user selection.
 */
public class CheckListListActivity extends AppCompatActivity {

    /** The list view **/
    private ListView listView;
    /** the list of checklist filenames **/
    private ArrayList<String> checklistList;
    /** array adapter **/
    private ArrayAdapter<String> adapter;

    /**
     * Function for creating the activity
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist_list);

        listView = findViewById(R.id.checklist_listview);
        checklistList = new ArrayList<String>();


        try {
            String[] sheets = getAssets().list("templates");

            for (int i=0; i < sheets.length; i++) {
                Log.d("Files", "FileName: " + sheets[i]);
                checklistList.add(i, sheets[i]);


            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, checklistList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Log.d("CheckListListActivity", "onItemClick:" + checklistList.get(i));
                Toast.makeText(CheckListListActivity.this, "Loading Checklist", Toast.LENGTH_SHORT).show();
                openAuditActivity(checklistList.get(i));
            }
        });
    }

    /**
     * Function for opening the audit activity
     * @param checklistName the checklist name selected for the audit
     */
    public void openAuditActivity(String checklistName) {

        Intent intent = new Intent(this, AuditActivity.class);
        intent.putExtra("checklistName", checklistName);

        startActivity(intent);
    }

}
