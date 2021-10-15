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
//import android.graphics.pdf.PdfDocument;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
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
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


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
    private void createPdf() throws FileNotFoundException {
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath, titleEdit.getText() + ".pdf");

        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        Drawable drawable = getDrawable(R.drawable.united_airlines_quality_assurance_logo);
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitmapData = stream.toByteArray();

        ImageData imageData = ImageDataFactory.create(bitmapData);
        Image image = new Image(imageData);
        image.setHeight(80);
        image.setWidth(500);

        Paragraph paragraph = new Paragraph("Technical Operations Quality Assurance");

        float columnWidth[] = {200f, 200f};
        Table table = new Table(columnWidth);

        // add cell
        table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Employee Name: ")));
        table.addCell(employeeNameEdit.getText().toString());

        table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Part Number: ")));
        table.addCell(partNumberEdit.getText().toString());

        table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Serial Number: ")));
        table.addCell(serialNumberEdit.getText().toString());

        table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Nomeclature: ")));
        table.addCell(nomenclatureEdit.getText().toString());

        table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Task: ")));
        table.addCell(taskEdit.getText().toString());

        table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Technical Specifications: ")));
        table.addCell(techSpecificationsEdit.getText().toString());

        table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Tooling: ")));
        table.addCell(toolingEdit.getText().toString());

        table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Shelf Life: ")));
        table.addCell(shelfLifeEdit.getText().toString());

        table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Traceability: ")));
        table.addCell(traceEdit.getText().toString());

        table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Required Training: ")));
        table.addCell(reqTrainingEdit.getText().toString());

        table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Date Qualified: ")));
        table.addCell(trainingDateEdit.getText().toString());


        document.add(image);
        document.add(paragraph);
        document.add(table);

        document.close();
        Toast.makeText(getApplicationContext(), "PDF Created", Toast.LENGTH_LONG).show();



//        PdfDocument myPdfDocument = new PdfDocument();
//        Paint myPaint = new Paint();
//
//        PdfDocument.PageInfo myInfo = new PdfDocument.PageInfo.Builder(1200, 2010, 1).create();
//        PdfDocument.Page myPage = myPdfDocument.startPage(myInfo);
//        Canvas canvas = myPage.getCanvas();
//
////        canvas.drawBitmap(scaledbtmp, 0, 0, myPaint);
//        canvas.drawText("Technical Operations Quality Assurance", 40, 50, myPaint);
//        canvas.drawText(titleEdit.getText().toString(), 80, 100, myPaint);
//        canvas.drawText(employeeNameEdit.getText().toString(), 120, 100, myPaint);
//        canvas.drawText(partNumberEdit.getText().toString(), 160, 100, myPaint);
//        canvas.drawText(serialNumberEdit.getText().toString(), 200, 100, myPaint);
//        canvas.drawText(nomenclatureEdit.getText().toString(), 240, 100, myPaint);
//        canvas.drawText(taskEdit.getText().toString(), 280, 100, myPaint);
//        canvas.drawText(techSpecificationsEdit.getText().toString(), 320, 100, myPaint);
//        canvas.drawText(toolingEdit.getText().toString(), 360, 100, myPaint);
//        canvas.drawText(shelfLifeEdit.getText().toString(), 400, 100, myPaint);
//        canvas.drawText(traceEdit.getText().toString(), 440, 100, myPaint);
//        canvas.drawText(reqTrainingEdit.getText().toString(), 480, 100, myPaint);
//        canvas.drawText(trainingDateEdit.getText().toString(), 520, 100, myPaint);
//
//        myPdfDocument.finishPage(myPage);
//
//        File file = new File(getExternalFilesDir("/"), titleEdit.getText() + ".pdf");
//
//        try {
//         myPdfDocument.writeTo(new FileOutputStream(file));
//        } catch (IOException error) {
//         error.printStackTrace();
//        }
//
//        myPdfDocument.close();
//        Toast.makeText(getApplicationContext(), "PDF Created", Toast.LENGTH_LONG).show();
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
                try {
                    createPdf();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.switch_to_data_tables_btn:
                startActivity(new Intent(this, TabularDataActivity.class));
                break;
        }

    }


}