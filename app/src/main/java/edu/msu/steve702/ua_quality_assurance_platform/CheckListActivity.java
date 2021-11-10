package edu.msu.steve702.ua_quality_assurance_platform;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import edu.msu.steve702.ua_quality_assurance_platform.data_objects.ChecklistDataObject;

public class CheckListActivity extends AppCompatActivity {

    String checklist_name;

    ChecklistDataObject obj;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_checklist);

        checklist_name = getIntent().getStringExtra("checklistName");




    }
}
