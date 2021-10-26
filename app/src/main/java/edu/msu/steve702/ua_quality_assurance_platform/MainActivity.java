package edu.msu.steve702.ua_quality_assurance_platform;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    private Button newAuditButton;
    private Button editAuditButton;
    private StorageReference storageRef;
    private ProgressDialog progressDialog;
    public Uri imageUri;

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

    public void onGetRegulations(View view){
        Intent httpIntent = new Intent(Intent.ACTION_VIEW);
        httpIntent.setData(Uri.parse("http://35.9.22.101"));

        startActivity(httpIntent);

    }
}