package edu.msu.steve702.ua_quality_assurance_platform;

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

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class InProcessActivity extends AppCompatActivity {

    private Button createButton, addTableButton;
    private EditText name, partNum;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_process);


        // button to generate PDF
        createButton = findViewById(R.id.generate_pdf_btn);

        // button to add table
        addTableButton = findViewById(R.id.add_data_table_btn);

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
    }

    public void insertTabularDataActivity() {
        Intent intent = new Intent(InProcessActivity.this, TabularDataActivity.class);
        startActivity(intent);
    }

    private void createPdf() {
        createButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PdfDocument myPdfDocument = new PdfDocument();
                    Paint myPaint = new Paint();

                    PdfDocument.PageInfo myInfo = new PdfDocument.PageInfo.Builder(1200, 2010, 1).create();
                    PdfDocument.Page myPage = myPdfDocument.startPage(myInfo);
                    Canvas canvas = myPage.getCanvas();

                    canvas.drawText("Technical Operations Quality Assurance", 40, 50, myPaint);
                    myPdfDocument.finishPage(myPage);

                    file = new File(getExternalFilesDir("/"), "temp2.pdf");
//                    String path = Environment.getExternalStorageDirectory().getPath();
//                    File file = new File(path, "/temp2.pdf");

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