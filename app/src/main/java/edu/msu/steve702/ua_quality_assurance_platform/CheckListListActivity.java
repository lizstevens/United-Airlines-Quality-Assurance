package edu.msu.steve702.ua_quality_assurance_platform;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class CheckListListActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<String> checklistList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist_list);

        listView = findViewById(R.id.checklist_listview);
        checklistList = new ArrayList<String>();

        Field[] fields = R.raw.class.getFields();
        for (int i=0; i < fields.length; i++) {
            Log.d("Files", "FileName: " + fields[i].getName());
            checklistList.add(i, fields[i].getName());
        }

        adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, checklistList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Log.d("CheckListListActivity", "onItemClick:" + checklistList.get(i));
                Toast.makeText(CheckListListActivity.this, checklistList.get(i), Toast.LENGTH_SHORT).show();
                openChecklistActivity(checklistList.get(i));
            }
        });
    }

    public void openChecklistActivity(String checklistName) {
        Intent intent = new Intent(this, CheckListActivity.class);
        intent.putExtra("checklistName", checklistName);
        startActivity(intent);
    }

}
