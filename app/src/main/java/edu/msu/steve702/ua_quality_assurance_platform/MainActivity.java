package edu.msu.steve702.ua_quality_assurance_platform;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private Button auditButton;
    private Button viewAndUpdateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auditButton = (Button)findViewById(R.id.createAudit);
        auditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInProcessActivity();

            }
        });

//        viewAndUpdateButton = (Button)findViewById(R.id.updateAudit);
//        viewAndUpdateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openUpdateInProcessActivity();
//
//            }
//        });
    }

    public void openInProcessActivity() {
        Intent intent = new Intent(this, InProcessActivity.class);
        startActivity(intent);
    }

//    public void openUpdateInProcessActivity() {
//        Intent intent = new Intent(this, UpdateInProcessActivity.class);
//        startActivity(intent);
//    }
}