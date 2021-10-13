package edu.msu.steve702.ua_quality_assurance_platform;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;


public class InProcessActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseFirestore db;

//    DataObject inProcessIntent = (DataObject)getIntent().getSerializableExtra("in-process");

    private Button saveButton, generatePDFButton, addTableButton, updateButton;
    private EditText employeeNameEdit, partNumberEdit , serialNumberEdit ,nomenclatureEdit ,taskEdit;
    private EditText techSpecificationsEdit , toolingEdit , shelfLifeEdit, traceEdit, reqTrainingEdit, trainingDateEdit;
    private Button clearButton;
    private Button viewAndUpdateButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_process);

        db = FirebaseFirestore.getInstance();

        // edit text name
        employeeNameEdit = findViewById(R.id.empNameText);

        // edit text part number
        partNumberEdit  = findViewById(R.id.partNumText);

        // edit text serial number
        serialNumberEdit  = findViewById(R.id.serialNumText);

        // edit text nomenclature
        nomenclatureEdit  = findViewById(R.id.nomenText);

        // edit text task
        taskEdit  = findViewById(R.id.taskText);

        // edit text techSpecifications
        techSpecificationsEdit  = findViewById(R.id.techSpecText);

        // edit text tooling
        toolingEdit  = findViewById(R.id.toolingText);

        // edit text shelfLife
        shelfLifeEdit  = findViewById(R.id.shelfLifeText);

        // edit text traceability
        traceEdit  = findViewById(R.id.traceText);

        // edit text reqTraining
        reqTrainingEdit  = findViewById(R.id.reqTrainingText);

        // edit text trainingDate
        trainingDateEdit  = findViewById(R.id.dateText);

        findViewById(R.id.save_btn).setOnClickListener(this);
        findViewById(R.id.view_inProcess).setOnClickListener(this);
    }


    // this function sets all the data objects
    private void saveInProcess() {
        String employeeNameObj = employeeNameEdit.getText().toString();
        String partNumberObj = partNumberEdit.getText().toString();
        String serialNumberObj = serialNumberEdit.getText().toString();
        String nomenclatureObj = nomenclatureEdit.getText().toString();
        String taskObj = taskEdit.getText().toString();
        String techSpecificationsObj = techSpecificationsEdit.getText().toString();
        String toolingObj = toolingEdit.getText().toString();
        String shelfLifeObj = shelfLifeEdit.getText().toString();
        String traceObj = traceEdit.getText().toString();
        String reqTrainingObj = reqTrainingEdit.getText().toString();
        String trainingDateObj = trainingDateEdit.getText().toString();


        CollectionReference dbInProcessSheets = db.collection("in-process");

        DataObject inProcess = new DataObject(
                employeeNameObj,
                partNumberObj,
                serialNumberObj,
                nomenclatureObj,
                taskObj,
                techSpecificationsObj,
                toolingObj,
                shelfLifeObj,
                traceObj,
                reqTrainingObj,
                trainingDateObj
        );

        // save in firestore
        dbInProcessSheets.add(inProcess).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(InProcessActivity.this, "Data Added", Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(InProcessActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

//    private void setText() {
//        updateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                employeeNameEdit.setText(inProcessIntent.getEmployeeNameObj());
//                partNumberEdit.setText(inProcessIntent.getPartNumberObj());
//                serialNumberEdit.setText(inProcessIntent.getSerialNumberObj());
//                nomenclatureEdit.setText(inProcessIntent.getNomenclatureObj());
//                taskEdit.setText(inProcessIntent.getTaskObj());
//                techSpecificationsEdit.setText(inProcessIntent.getTechSpecificationsObj());
//                toolingEdit.setText(inProcessIntent.getToolingObj());
//                shelfLifeEdit.setText(inProcessIntent.getShelfLifeObj());
//                traceEdit.setText(inProcessIntent.getTraceObj());
//                reqTrainingEdit.setText(inProcessIntent.getReqTrainingObj());
//                trainingDateEdit.setText(inProcessIntent.getTrainingDateObj());
//            }
//        });
//    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.save_btn:
                saveInProcess();
                break;
            case R.id.view_inProcess:
                startActivity(new Intent(this, UpdateInProcessActivity.class));
                break;
        }

    }


}