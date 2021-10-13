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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;


public class InProcessActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("in-process");

    private Button saveButton, generatePDFButton, addTableButton;
    DataObject dataObject = new DataObject();
    private EditText employeeNameEdit , partNumberEdit , serialNumberEdit ,nomenclatureEdit ,taskEdit;
    private EditText techSpecificationsEdit , toolingEdit , shelfLifeEdit, traceEdit, reqTrainingEdit, trainingDateEdit;
    long auditNumber = 0;
    Button clearButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_process);

//        callFindViewId();

//        callSaveOnClickListener();

//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                auditNumber = snapshot.getChildrenCount();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        // request permissions
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        createPdf();

        addTableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertTabularDataActivity();

            }
        });

        clearData();

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

    // this function sets all the data objects
//    private void callSaveOnClickListener() {
//        saveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dataObject.auditNumber = auditNumber + 1;
//                dataObject.employeeNameObj = String.valueOf(employeeNameEdit.getText());
//                dataObject.partNumberObj = String.valueOf(partNumberEdit.getText());
//                dataObject.serialNumberObj = String.valueOf(serialNumberEdit.getText());
//                dataObject.nomenclatureObj = String.valueOf(nomenclatureEdit.getText());
//                dataObject.taskObj = String.valueOf(taskEdit.getText());
//                dataObject.techSpecificationsObj = String.valueOf(techSpecificationsEdit.getText());
//                dataObject.toolingObj = String.valueOf(toolingEdit.getText());
//                dataObject.shelfLifeObj = String.valueOf(shelfLifeEdit.getText());
//                dataObject.traceObj = String.valueOf(traceEdit.getText());
//                dataObject.reqTrainingObj = String.valueOf(reqTrainingEdit.getText());
//                dataObject.trainingDateObj = String.valueOf(trainingDateEdit.getText());
//
//                // adds one to every in process sheet made
//                myRef.child(String.valueOf(auditNumber + 1)).setValue(dataObject);
//            }
//        });
//    }

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