package edu.msu.steve702.ua_quality_assurance_platform;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private Button newAuditButton;
    private Button editAuditButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newAuditButton = (Button)findViewById(R.id.createAudit);
        newAuditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInProcessActivity();
            }
        });

        editAuditButton = (Button)findViewById(R.id.editAudit);
        editAuditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startListOptionsActivity();
            }
        });
    }

    public void openInProcessActivity() {
        Intent intent = new Intent(this, InProcessActivity.class);
        startActivity(intent);
    }

    public void startListOptionsActivity() {
        startActivity(new Intent(this, InProcessListActivity.class));
    }
}