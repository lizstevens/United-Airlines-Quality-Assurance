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


public class UpdateInProcessActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseFirestore db;

    private DataObject inProcessIntent;

    private Button saveButton, generatePDFButton, addTableButton, updateButton;
    private EditText titleEdit, employeeNameEdit, partNumberEdit , serialNumberEdit ,nomenclatureEdit ,taskEdit;
    private EditText techSpecificationsEdit , toolingEdit , shelfLifeEdit, traceEdit, reqTrainingEdit, trainingDateEdit;
    private Button clearButton;
    private Button viewAndUpdateButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_process_update);

        inProcessIntent = (DataObject)getIntent().getSerializableExtra("in-process");
        db = FirebaseFirestore.getInstance();

        // edit in process title
        titleEdit = findViewById(R.id.edit_AuditTitle);

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

        titleEdit.setText(inProcessIntent.getTitleObj());
        employeeNameEdit.setText(inProcessIntent.getEmployeeNameObj());
        partNumberEdit.setText(inProcessIntent.getPartNumberObj());
        serialNumberEdit.setText(inProcessIntent.getSerialNumberObj());
        nomenclatureEdit.setText(inProcessIntent.getNomenclatureObj());
        taskEdit.setText(inProcessIntent.getTaskObj());
        techSpecificationsEdit.setText(inProcessIntent.getTechSpecificationsObj());
        toolingEdit.setText(inProcessIntent.getToolingObj());
        shelfLifeEdit.setText(inProcessIntent.getShelfLifeObj());
        traceEdit.setText(inProcessIntent.getTraceObj());
        reqTrainingEdit.setText(inProcessIntent.getReqTrainingObj());
        trainingDateEdit.setText(inProcessIntent.getTrainingDateObj());

        findViewById(R.id.updateAudit).setOnClickListener(this);
    }

    private void updateInProcess() {
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

        DataObject inProcessUpdate = new DataObject(
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

        // override existing data using id
        db.collection("in-process").document(inProcessIntent.getId()).set(inProcessUpdate)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UpdateInProcessActivity.this, "Data Updated", Toast.LENGTH_LONG).show();

                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.updateAudit:
                updateInProcess();
                break;
        }

    }


}