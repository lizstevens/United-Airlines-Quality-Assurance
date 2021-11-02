package edu.msu.steve702.ua_quality_assurance_platform;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;
import com.itextpdf.kernel.pdf.PdfViewerPreferences;

public class pdfActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);


        Bundle extras = getIntent().getExtras();

        if(extras != null) {

            String UriString = extras.getString("URI");

            PDFView pdfView = findViewById(R.id.pdfView);

            pdfView.fromUri(Uri.parse(UriString)).load();
        }





    }
}