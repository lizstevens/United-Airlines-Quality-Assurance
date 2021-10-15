package edu.msu.steve702.ua_quality_assurance_platform;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class InProcessActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseFirestore db;

    private Button saveButton, generatePDFButton, addTableButton, updateButton;
    private EditText titleEdit, employeeNameEdit, partNumberEdit , serialNumberEdit ,nomenclatureEdit ,taskEdit;
    private EditText techSpecificationsEdit , toolingEdit , shelfLifeEdit, traceEdit, reqTrainingEdit, trainingDateEdit;
    private Button clearButton;
    private Button viewAndUpdateButton;
    Bitmap btmp, scaledbtmp;
    private LinearLayout inProcessPdf;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_process);

        db = FirebaseFirestore.getInstance();

        // request permissions
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

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

        inProcessPdf = findViewById(R.id.inProcessPdf);

        // adding the logo to the pdf header
        btmp = BitmapFactory.decodeResource(getResources(), R.drawable.united_airlines_quality_assurance_logo_pdf);
        scaledbtmp = Bitmap.createScaledBitmap(btmp, 1200, 518, false);




        findViewById(R.id.save_btn).setOnClickListener(this);
        findViewById(R.id.clear_data).setOnClickListener(this);
        findViewById(R.id.generate_pdf_btn).setOnClickListener(this);
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

    // this function allows user to create a pdf to store locally
    private void createPdf() {
        PdfDocument myPdfDocument = new PdfDocument();
        Paint myPaint = new Paint();

        PdfDocument.PageInfo myInfo = new PdfDocument.PageInfo.Builder(1200, 2010, 1).create();
        PdfDocument.Page myPage = myPdfDocument.startPage(myInfo);
        Canvas canvas = myPage.getCanvas();

//        canvas.drawBitmap(scaledbtmp, 0, 0, myPaint);
        canvas.drawText("Technical Operations Quality Assurance", 40, 50, myPaint);
        canvas.drawText(titleEdit.getText().toString(), 80, 100, myPaint);
        canvas.drawText(employeeNameEdit.getText().toString(), 120, 100, myPaint);
        canvas.drawText(partNumberEdit.getText().toString(), 160, 100, myPaint);
        canvas.drawText(serialNumberEdit.getText().toString(), 200, 100, myPaint);
        canvas.drawText(nomenclatureEdit.getText().toString(), 240, 100, myPaint);
        canvas.drawText(taskEdit.getText().toString(), 280, 100, myPaint);
        canvas.drawText(techSpecificationsEdit.getText().toString(), 320, 100, myPaint);
        canvas.drawText(toolingEdit.getText().toString(), 360, 100, myPaint);
        canvas.drawText(shelfLifeEdit.getText().toString(), 400, 100, myPaint);
        canvas.drawText(traceEdit.getText().toString(), 440, 100, myPaint);
        canvas.drawText(reqTrainingEdit.getText().toString(), 480, 100, myPaint);
        canvas.drawText(trainingDateEdit.getText().toString(), 520, 100, myPaint);

        myPdfDocument.finishPage(myPage);

        File file = new File(getExternalFilesDir("/"), "in-process.pdf");

        try {
         myPdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException error) {
         error.printStackTrace();
        }

        myPdfDocument.close();
        Toast.makeText(getApplicationContext(), "PDF Created", Toast.LENGTH_LONG).show();
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
            case R.id.generate_pdf_btn:
                createPdf();
                break;
            case R.id.switch_to_data_tables_btn:
                startActivity(new Intent(this, TabularDataActivity.class));
                break;
        }

    }


}