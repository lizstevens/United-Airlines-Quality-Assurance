package edu.msu.steve702.ua_quality_assurance_platform;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import java.io.OutputStream;

public class TabularDataActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseFirestore db;

    private EditText tdPartNumEdit, tdManufEdit, tdAtaEdit , tdRevLevelEdit, tdRevDateEdit, tdCommentsEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabular_data);

        db = FirebaseFirestore.getInstance();

        // request permissions
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        // edit technical data table
        tdPartNumEdit = findViewById(R.id.tdPartNumText);
        tdManufEdit = findViewById(R.id.tdManufText);
        tdAtaEdit = findViewById(R.id.tdAtaText);
        tdRevLevelEdit = findViewById(R.id.tdRevLevelText);
        tdRevDateEdit = findViewById(R.id.tdRevDateText);
        tdCommentsEdit = findViewById(R.id.tdCommentsText);

        findViewById(R.id.save_btn).setOnClickListener(this);
        findViewById(R.id.view_td).setOnClickListener(this);
        findViewById(R.id.switch_to_in_process_btn).setOnClickListener(this);
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
            case R.id.save_btn:
                saveTechnicalData();
                break;
            case R.id.switch_to_in_process_btn:
                startActivity(new Intent(this, InProcessActivity.class));
                break;
            case R.id.view_td:
                startActivity(new Intent(this, TechnicalDataListActivity.class));
                break;
        }

    }


}
