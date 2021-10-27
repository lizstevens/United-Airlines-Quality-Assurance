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
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.OnProgressListener;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;

import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    private Button newAuditButton;
    private Button editAuditButton;
    private Button uploadImageButton;
//    private FirebaseStorage storage;
//    private StorageReference storageRef;
    public Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        storage = FirebaseStorage.getInstance();
//        storageRef = storage.getReference();
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
//        uploadImageButton = findViewById(R.id.uploadImage);
//
//        uploadImageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                choosePicture();
//            }
//        });

    }

//    public void openInProcessActivity() {
////        Intent intent = new Intent(this, InProcessActivity.class);
////        startActivity(intent);
////    }

    public void openInProcessActivity() {
        Intent intent = new Intent(this, InitialAuditActivity.class);
        startActivity(intent);
    }

    public void startListOptionsActivity() {
        startActivity(new Intent(this, CheckListListActivity.class));
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
//            imageUri = data.getData();
//            uploadImage(imageUri);
//        }
//    }
//
//    private void uploadImage(Uri imageUri) {
//
//        final ProgressDialog pd =new ProgressDialog(this);
//        pd.setTitle("Uploading Image...");
//        pd.show();
//
//        final String randomKey = UUID.randomUUID().toString();
//        // Create a reference
//        StorageReference imageRef = storageRef.child("image/" + randomKey);
//
//        // While the file names are the same, the references point to different files
//        imageRef.getName().equals(imageRef.getName());    // true
//        imageRef.getPath().equals(imageRef.getPath());    // false
//
//        imageRef.putFile(imageUri)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        Snackbar.make(findViewById(android.R.id.content),"Image Uploaded",Snackbar.LENGTH_LONG).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        pd.dismiss();
//                        Toast.makeText(getApplicationContext(),"Failed Tp Upload", Toast.LENGTH_LONG).show();
//                    }
//                })
//                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                        double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
//                        pd.setMessage("Progress: " + (int) progressPercent + "%");
//                    }
//                });
//
//    }
//
    public void onGetRegulations(View view){
        Intent httpIntent = new Intent(Intent.ACTION_VIEW);
        httpIntent.setData(Uri.parse("http://35.9.22.101"));

        startActivity(httpIntent);

    }
}