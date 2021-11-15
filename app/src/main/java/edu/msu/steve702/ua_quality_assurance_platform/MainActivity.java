package edu.msu.steve702.ua_quality_assurance_platform;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.OnProgressListener;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FilenameFilter;

import edu.msu.steve702.ua_quality_assurance_platform.activities.CheckListListActivity;
import edu.msu.steve702.ua_quality_assurance_platform.activities.EditAuditListActivity;


public class MainActivity extends AppCompatActivity {

    private Button newAuditButton;
    private Button editAuditButton;
    private Button uploadImageButton;

    private FileObserver observer;
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
                startNewAuditActivity();
            }
        });

        editAuditButton = (Button)findViewById(R.id.editAudit);
        editAuditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEditAuditActivity();
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

    public void startNewAuditActivity() {
        Intent intent = new Intent(this, CheckListListActivity.class);
        startActivity(intent);
    }

    public void startEditAuditActivity() {
        startActivity(new Intent(this, EditAuditListActivity.class));
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


    @Override
    protected void onResume() {
        super.onResume();
        File f = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);


        File[] files = f.listFiles(new FilenameFilter(){
            @Override
            public boolean accept(File file, String s) {
                return s.matches("[0-9]{4}-[0-3][0-9]-[0-9]{2}[R0-9]*.pdf");
            }
        });

        LinearLayout layout = (LinearLayout) findViewById(R.id.FAAData);

        // Check if table already exists
        if(findViewById(R.id.RegTable) != null){
            layout.removeView(findViewById(R.id.RegTable));
        }

        if(files.length > 0){


            // Add table layout

            TableLayout tl = new TableLayout(this);

            tl.setId(R.id.RegTable);

            // Add header
            TableRow header = new TableRow(this);

            header.setBackgroundColor(getColor(R.color.UnitedBlue));

            TextView file = new TextView(this);
            file.setText("Airworthiness Directives");
            file.setTextColor(getColor(android.R.color.white));
            file.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            header.addView(file);


            TextView delete = new TextView(this);
            delete.setText("Delete");
            delete.setTextColor(getColor(android.R.color.white));
            delete.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            header.addView(delete);



            tl.addView(header);

            for(int i=0; i < files.length; i++){
                TableRow tableRow = new TableRow(this);
                TextView fileName = new TextView(this);
                fileName.setText(files[i].getName());


                int finalI = i;
                fileName.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {


                        Intent intent = new Intent(MainActivity.this, pdfActivity.class);
                        intent.putExtra("URI", Uri.fromFile(files[finalI]).toString());
                        startActivity(intent);



                    }
                });


                tableRow.addView(fileName);

                Button button = new Button(this);
                button.setText("Delete");

                button.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {

                        // Delete associated file in downloads directory
                        File deleteFile = files[finalI];

                        boolean deleted = deleteFile.delete();

                        // Restart activity
                        onResume();


                    }
                });

                tableRow.addView(button);

                tl.addView(tableRow);
            }

            layout.addView(tl);
        }

        


    }

    public void onGetRegulations(View view){
        Intent httpIntent = new Intent(Intent.ACTION_VIEW);
        httpIntent.setData(Uri.parse("http://35.9.22.101"));



        startActivity(httpIntent);



    }
}