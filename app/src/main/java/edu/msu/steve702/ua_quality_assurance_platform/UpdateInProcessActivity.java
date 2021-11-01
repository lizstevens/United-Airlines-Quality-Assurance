package edu.msu.steve702.ua_quality_assurance_platform;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.io.OutputStream;

import edu.msu.steve702.ua_quality_assurance_platform.data_objects.InProcessObject;


public class UpdateInProcessActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseFirestore db;

    private InProcessObject inProcessIntent;

    private Button saveButton, generatePDFButton, addTableButton, updateButton;
    private EditText titleEdit, employeeNameEdit, partNumberEdit , serialNumberEdit ,nomenclatureEdit ,taskEdit;
    private EditText techSpecificationsEdit , toolingEdit , shelfLifeEdit, traceEdit, reqTrainingEdit, trainingDateEdit;
    private Button clearButton;
    private Button goBackToCreateAudit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_process_update);

        inProcessIntent = (InProcessObject)getIntent().getSerializableExtra("in-process");
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
        findViewById(R.id.generate_pdf_btn).setOnClickListener(this);
        findViewById(R.id.createAudit).setOnClickListener(this);
        findViewById(R.id.view_in_process_sheets).setOnClickListener(this);
        findViewById(R.id.deleteAudit).setOnClickListener(this);
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

        InProcessObject inProcessUpdate = new InProcessObject(
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

    private void deleteInProcess() {
        db.collection("in-process").document(inProcessIntent.getId()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(UpdateInProcessActivity.this, "In-Process Deleted", Toast.LENGTH_LONG).show();
                        finish();
                        startActivity(new Intent(UpdateInProcessActivity.this, InProcessListActivity.class));
                    }

                }
            });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.updateAudit:
                updateInProcess();
                break;
            case R.id.generate_pdf_btn:
                try {
                    createPdf();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.createAudit:
                startActivity(new Intent(this, InProcessActivity.class));
                break;
            case R.id.view_in_process_sheets:
                startActivity(new Intent(this, InProcessListActivity.class));
                break;
            case R.id.deleteAudit:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Are you sure you would like to delete this in-process sheet data?");
                builder.setMessage("This deletion is permanent");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteInProcess();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                break;
        }


    }



}