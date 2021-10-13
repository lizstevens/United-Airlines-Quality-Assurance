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


public class InProcessActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    DataObject inProcess = new DataObject();

//    DataObject inProcessIntent = (DataObject)getIntent().getSerializableExtra("in-process");
    private Button saveButton, generatePDFButton, addTableButton, updateButton;
    private EditText employeeNameEdit, partNumberEdit , serialNumberEdit ,nomenclatureEdit ,taskEdit;
    private EditText techSpecificationsEdit , toolingEdit , shelfLifeEdit, traceEdit, reqTrainingEdit, trainingDateEdit;
    Button clearButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_process);

        db = FirebaseFirestore.getInstance();

        callFindViewId();

        callSaveOnClickListener();

//        setText();

        // request permissions
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

//        createPdf();

//        addTable();

//        clearData();

    }

    // this function calls all the ids
    private void callFindViewId() {
        // button to generate PDF
        generatePDFButton = findViewById(R.id.generate_pdf_btn);

        // save button
        saveButton = findViewById(R.id.save_btn);

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

        // button to add table
        addTableButton = findViewById(R.id.add_data_table_btn);

        // button to clear data
        clearButton =  findViewById(R.id.clear_data);

        // button to update data
//        updateButton = findViewById(R.id.update_btn);
    }

    // this function sets all the data objects
    private void callSaveOnClickListener() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inProcess.employeeNameObj = String.valueOf(employeeNameEdit.getText());
                inProcess.partNumberObj = String.valueOf(partNumberEdit.getText());
                inProcess.serialNumberObj = String.valueOf(serialNumberEdit.getText());
                inProcess.nomenclatureObj = String.valueOf(nomenclatureEdit.getText());
                inProcess.taskObj = String.valueOf(taskEdit.getText());
                inProcess.techSpecificationsObj = String.valueOf(techSpecificationsEdit.getText());
                inProcess.toolingObj = String.valueOf(toolingEdit.getText());
                inProcess.shelfLifeObj = String.valueOf(shelfLifeEdit.getText());
                inProcess.traceObj = String.valueOf(traceEdit.getText());
                inProcess.reqTrainingObj = String.valueOf(reqTrainingEdit.getText());
                inProcess.trainingDateObj = String.valueOf(trainingDateEdit.getText());

                CollectionReference dbInProcessSheets = db.collection("in-process");

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

    private void addTable() {
        addTableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertTabularDataActivity();

            }
        });
    }

    // this function allows the user to clear all the data when they want to start another in-process sheet
    private void clearData() {
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });
    }

    // this function allows the in process activity to call the tabular activity
    public void insertTabularDataActivity() {
        Intent intent = new Intent(InProcessActivity.this, TabularDataActivity.class);
        startActivity(intent);
    }

    // this function allows user to create a pdf
    private void createPdf() {
        generatePDFButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PdfDocument myPdfDocument = new PdfDocument();
                    Paint myPaint = new Paint();

                    PdfDocument.PageInfo myInfo = new PdfDocument.PageInfo.Builder(1200, 2010, 1).create();
                    PdfDocument.Page myPage = myPdfDocument.startPage(myInfo);
                    Canvas canvas = myPage.getCanvas();

                    canvas.drawText("Technical Operations Quality Assurance", 40, 50, myPaint);

                    myPdfDocument.finishPage(myPage);

                    File file = new File(getExternalFilesDir("/"), "temp2.pdf");

                    try {
                        myPdfDocument.writeTo(new FileOutputStream(file));
                    } catch (IOException error) {
                        error.printStackTrace();
                    }

                    myPdfDocument.close();
                    Toast.makeText(getApplicationContext(), "PDF Created", Toast.LENGTH_LONG).show();
                }

            }
        );
    }

}