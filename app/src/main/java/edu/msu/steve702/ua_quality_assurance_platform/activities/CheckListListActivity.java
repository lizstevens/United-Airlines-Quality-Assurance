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
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;

import edu.msu.steve702.ua_quality_assurance_platform.ExcelParser;
import edu.msu.steve702.ua_quality_assurance_platform.R;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.ChecklistDataObject;

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
                openAuditActivity(checklistList.get(i));
            }
        });
    }

    public void openAuditActivity(String checklistName) {

        ChecklistDataObject obj = null;
        try{
            ExcelParser parser = new ExcelParser(this);

            obj = parser.readXLSXFile(checklistName);
        }catch(IOException e){
            Log.e("Failed to Parse Excel", "Error message: " + e.getMessage());
        }

        Intent intent = new Intent(this, AuditActivity.class);
        intent.putExtra("checklistName", checklistName);

        if(obj != null){
            intent.putExtra("checklistDataObject", obj);
        }
        startActivity(intent);
    }

}
