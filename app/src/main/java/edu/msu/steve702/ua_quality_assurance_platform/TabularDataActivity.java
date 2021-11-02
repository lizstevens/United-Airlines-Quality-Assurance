package edu.msu.steve702.ua_quality_assurance_platform;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
//import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import edu.msu.steve702.ua_quality_assurance_platform.data_objects.TabularDataObject;

public class TabularDataActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseFirestore db;

    private EditText tdPartNumEdit, tdManufEdit, tdAtaEdit , tdRevLevelEdit, tdRevDateEdit, tdCommentsEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_table_data_tabs);

        db = FirebaseFirestore.getInstance();

        // request permissions
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        // edit technical data table
        tdPartNumEdit = findViewById(R.id.row1_col1);
        tdManufEdit = findViewById(R.id.row1_col2);
        tdAtaEdit = findViewById(R.id.row1_col3);
        tdRevLevelEdit = findViewById(R.id.row1_col4);
        tdRevDateEdit = findViewById(R.id.row1_col5);
        tdCommentsEdit = findViewById(R.id.row1_col6);

    }

    // this function sets all the data objects
    private void saveTechnicalData() {
        String tdPartNumObj = ((EditText)tdPartNumEdit).getText().toString();
        String tdManufObj = tdManufEdit.getText().toString();
        String tdAtaObj = tdAtaEdit.getText().toString();
        String tdRevLevelObj = tdRevLevelEdit.getText().toString();
        String tdRevDateObj = tdRevDateEdit.getText().toString();
        String tdCommentsObj = tdCommentsEdit.getText().toString();



        CollectionReference dbInProcessSheets = db.collection("technical_data");


        TabularDataObject techData = new TabularDataObject(
                tdPartNumObj,
                tdManufObj,
                tdAtaObj,
                tdRevLevelObj,
                tdRevDateObj,
                tdCommentsObj
        );

        // save in firestore
        dbInProcessSheets.add(techData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(TabularDataActivity.this, "Technical Data Added", Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TabularDataActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }

    }


}
