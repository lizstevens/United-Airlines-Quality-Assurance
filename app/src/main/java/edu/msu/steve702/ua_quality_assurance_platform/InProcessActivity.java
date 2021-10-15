package edu.msu.steve702.ua_quality_assurance_platform;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class InProcessActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseFirestore db;

    private Button saveButton, generatePDFButton, addTableButton, updateButton;
    private EditText titleEdit, employeeNameEdit, partNumberEdit , serialNumberEdit ,nomenclatureEdit ,taskEdit;
    private EditText techSpecificationsEdit , toolingEdit , shelfLifeEdit, traceEdit, reqTrainingEdit, trainingDateEdit;
    private Button clearButton;
    private Button viewAndUpdateButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_process);

        db = FirebaseFirestore.getInstance();

        // edit in process title
        titleEdit = findViewById(R.id.edit_AuditTitle);
        // edit text name
        employeeNameEdit = findViewById(R.id.empNameText);
        // edit text part number
        partNumberEdit = findViewById(R.id.partNumText);
        // edit text serial number
        serialNumberEdit = findViewById(R.id.serialNumText);
        // edit text nomenclature
        nomenclatureEdit = findViewById(R.id.nomenText);
        // edit text task
        taskEdit = findViewById(R.id.taskText);
        // edit text techSpecifications
        techSpecificationsEdit = findViewById(R.id.techSpecText);
        // edit text tooling
        toolingEdit = findViewById(R.id.toolingText);
        // edit text shelfLife
        shelfLifeEdit = findViewById(R.id.shelfLifeText);
        // edit text traceability
        traceEdit = findViewById(R.id.traceText);
        // edit text reqTraining
        reqTrainingEdit = findViewById(R.id.reqTrainingText);
        // edit text trainingDate
        trainingDateEdit = findViewById(R.id.dateText);

        findViewById(R.id.save_btn).setOnClickListener(this);
        findViewById(R.id.clear_data).setOnClickListener(this);
        findViewById(R.id.switch_to_data_tables_btn).setOnClickListener(this);

    }



    // this function sets all the data objects
    private void saveInProcess() {
        String titleObj = ((EditText)titleEdit).getText().toString();
        if (titleObj.isEmpty()) {
            titleObj = "untitled_audit_";
        }
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
//
//        CollectionReference dbInProcessSheets = db.collection(title);

        DataObject inProcess = new DataObject(
                titleObj,
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

    // this function allows the user to clear all the data when they want to start another in-process sheet
    private void clearData() {
        titleEdit.getText().clear();
        employeeNameEdit.getText().clear();
        partNumberEdit.getText().clear();
        serialNumberEdit.getText().clear();
        nomenclatureEdit.getText().clear();
        taskEdit.getText().clear();
        techSpecificationsEdit.getText().clear();
        toolingEdit.getText().clear();
        shelfLifeEdit.getText().clear();
        traceEdit.getText().clear();
        reqTrainingEdit.getText().clear();
        trainingDateEdit.getText().clear();
        Toast.makeText(InProcessActivity.this, "Data Cleared", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_btn:
                saveInProcess();
                break;
            case R.id.clear_data:
                clearData();
                break;
            case R.id.switch_to_data_tables_btn:
                startActivity(new Intent(this, TabularDataActivity.class));
                break;
        }

    }


}