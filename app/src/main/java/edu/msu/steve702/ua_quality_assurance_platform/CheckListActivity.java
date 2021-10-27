package edu.msu.steve702.ua_quality_assurance_platform;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class CheckListActivity extends AppCompatActivity {

    String checklist_name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);

        checklist_name = getIntent().getStringExtra("checklistName");

    }
}
