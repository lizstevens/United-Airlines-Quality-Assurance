package edu.msu.steve702.ua_quality_assurance_platform.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import edu.msu.steve702.ua_quality_assurance_platform.ExcelParser;
import edu.msu.steve702.ua_quality_assurance_platform.ImageDisplayActivity;
import edu.msu.steve702.ua_quality_assurance_platform.MainActivity;
import edu.msu.steve702.ua_quality_assurance_platform.R;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.AuditObject;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.CalibrationTableDataObject;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.ChecklistDataObject;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.InProcessObject;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.ROMTableDataObject;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.ShelfLifeTableDataObject;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.TechnicalTableDataObject;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.TraceabilityTableDataObject;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.TrainingTableDataObject;
import edu.msu.steve702.ua_quality_assurance_platform.main_fragments.AuditPageAdapter;
import edu.msu.steve702.ua_quality_assurance_platform.main_fragments.AuditSpecFragment;
import edu.msu.steve702.ua_quality_assurance_platform.main_fragments.InProcessFragment;

import static android.content.ContentValues.TAG;

public class AuditActivity extends AppCompatActivity {
    String PDF = ".pdf";
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    AuditPageAdapter pageAdapter;
    TabItem tabAuditSpecs;
    TabItem tabChecklist;
    TabItem tabInProcess;
    TabItem tabTableData;
    String checklist_name;
    Button save_button;
    private ProgressDialog progressDialog;
    private Button auditSpecsSaveBtn;
    private Button inProcessSaveBtn;
    public Uri imageUri;
    public FirebaseFirestore db;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageRef;
    private AuditSpecFragment auditSpecFragment;
    private InProcessFragment inProcessFragment;

    private String audit_id;
    private Context context;

    private ChecklistDataObject checklist;
    private AuditObject auditObject;
    private TechnicalTableDataObject techObject;
    private ChecklistDataObject checklistDataObject;

    // Collection of photos associated with the audit
    private ArrayList<byte[]> photos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit);
        context = getApplicationContext();
        db = FirebaseFirestore.getInstance();

        toolbar = findViewById(R.id.toolbar);
        save_button = findViewById(R.id.saveButton);
        toolbar.setTitle(R.string.title);
        setSupportActionBar(toolbar);
        firebaseStorage = FirebaseStorage.getInstance();
        storageRef = firebaseStorage.getReference();
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAuditSpecs();
            }
        });

        tabLayout = findViewById(R.id.auditTabLayout);
        tabAuditSpecs = findViewById(R.id.auditSpecsTabItem);
        tabChecklist = findViewById(R.id.checklistTabItem);
        tabInProcess = findViewById(R.id.inProcessTabItem);
        tabTableData = findViewById(R.id.tableDataTabItem);
        viewPager = findViewById(R.id.imageViewPager);

        //name of the checklist that was selected from the previous view
        checklist_name = getIntent().getStringExtra("checklistName");
        if (getIntent().getExtras() != null & !getIntent().getExtras().containsKey("editing")) {
            try {
                ExcelParser parser = new ExcelParser(this);

                checklist = parser.readXLSXFile(checklist_name);
            } catch (IOException e) {
                Log.e("Failed to Parse Excel", "Error message: " + e.getMessage());
            }
        }else{
            checklist_name = "";
            Map<Integer, Map<Integer, String[]>> map = new HashMap<>();
            checklist = new ChecklistDataObject(0, map);
        }


        pageAdapter = new AuditPageAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), checklist_name, context, checklist);
        viewPager.setAdapter(pageAdapter);

        //For pre populating data
        Intent intent = getIntent();
        if (intent.getExtras() != null && intent.getExtras().containsKey("audit_id")) {
            audit_id = getIntent().getStringExtra("audit_id");
            pageAdapter.getAuditSpecFragment().setAuditObject((AuditObject) getIntent().getSerializableExtra("auditObject"));
            pageAdapter.getInProcessFragment().setInProcessList((List<InProcessObject>) getIntent().getSerializableExtra("InProcessList"));
            if (getIntent().getExtras().containsKey("TechnicalDataTable")) {
                pageAdapter.getTableDataFragment().setTechnicalTableDataObject((TechnicalTableDataObject) getIntent().getSerializableExtra("TechnicalDataTable"));
            }
            if (getIntent().getExtras().containsKey("ROMTableData")) {
                pageAdapter.getTableDataFragment().setRomTableDataObject((ROMTableDataObject) getIntent().getSerializableExtra("ROMTableData"));
            }
            if (getIntent().getExtras().containsKey("CalibrationTableData")) {
                pageAdapter.getTableDataFragment().setCalibrationTableDataObject((CalibrationTableDataObject) getIntent().getSerializableExtra("CalibrationTableData"));
            }
            if (getIntent().getExtras().containsKey("TrainingTableData")) {
                pageAdapter.getTableDataFragment().setTrainingTableDataObject((TrainingTableDataObject) getIntent().getSerializableExtra("TrainingTableData"));
            }
            if (getIntent().getExtras().containsKey("TraceabilityTableData")) {
                pageAdapter.getTableDataFragment().setTraceabilityTableDataObject((TraceabilityTableDataObject) getIntent().getSerializableExtra("TraceabilityTableData"));
            }
            if (getIntent().getExtras().containsKey("ShelfLifeTableData")) {
                pageAdapter.getTableDataFragment().setShelfLifeTableDataObject((ShelfLifeTableDataObject) getIntent().getSerializableExtra("ShelfLifeTableData"));
            }
            if (getIntent().getExtras().containsKey("ChecklistData")) {
                pageAdapter.getChecklistFragment().setChecklistDataObject((ChecklistDataObject) getIntent().getSerializableExtra("ChecklistData"));
                pageAdapter.getChecklistFragment().setChecklistId((Integer) getIntent().getIntExtra("checklistID",0));
            }
            if(getIntent().getExtras().containsKey("Photos")){
                ArrayList<String> photoRefs = getIntent().getStringArrayListExtra("Photos");

                for(String ref: photoRefs){
                    StorageReference photo = firebaseStorage.getReferenceFromUrl(ref);

                    final long ONE_MEGABYTE = 1024 * 1024;
                    photo.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            photos.add(bytes);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });
                }
            }
        }

//        if (savedInstanceState != null) {
//            // Restore the fragment's instance
//            auditSpecFragment = (AuditSpecFragment) getSupportFragmentManager().getFragment(savedInstanceState, "auditSpecsFragment");
//            inProcessFragment = (InProcessFragment) getSupportFragmentManager().getFragment(savedInstanceState, "inProcessFragment");
//        }


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // request permissions
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_audit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            // generate pdf
            case R.id.option1:
                Toast.makeText(this, "Clicked on " + item.getTitle(), Toast.LENGTH_SHORT).show();
//                List<InProcessObject> inProcessList = pageAdapter.getInProcessFragment().getInProcessList();
                try {
                    createChecklistPdf(pageAdapter.getChecklistFragment().getChecklistDataObject());
//                    createInProcessPdf(inProcessList);
//                    createTechTablePdf(techObject);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.option2:
                intent = new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
            case R.id.option3:
                try {
                    takePhoto();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;

            case R.id.option4:
                intent = new Intent(context, ImageDisplayActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("SIZE" , photos.size());
                try {
                    writeFiles();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startActivityForResult(intent, 0);
                return true;

            case R.id.option5:
                choosePicture();
                return true;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch(requestCode){
            case 0:
                onDeleteImage(data);
                break;
            case 1:
                onCaptureImageResult(data);
                break;

            case 2:
                if (resultCode == RESULT_OK) {
                    Uri imageUri = data.getData();
                    try {
                        Bitmap bitmap = (Bitmap) MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        onSelectImageResult(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;

            default:

                break;

        }

    }

    private void writeFiles() throws IOException {

        for(int i = 0; i < photos.size(); i++){
            String filename = "photo" + i +".jpeg";
            FileOutputStream file = null;
            try {
                file = openFileOutput(filename, Context.MODE_PRIVATE);
                file.write(photos.get(i));
            } catch (FileNotFoundException ex) {
                return;
            } catch (IOException ex) {
                return;
            } finally {
                file.close();
            }
        }

    }

    private void takePhoto() throws IOException {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Create the File where the photo should go
        File photoFile = createImageFile();


        if (photoFile != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            startActivityForResult(intent, 1);
        }
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 2);
    }

    private void onCaptureImageResult(Intent data){
        if (data != null){
            Uri imageUri = data.getData();
            String filePath = getRealPathFromURI(imageUri);
            Bitmap image = (Bitmap) BitmapFactory.decodeFile(filePath);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG,100,bytes);
            byte bb[] = bytes.toByteArray();
            //image.recycle();
            photos.add(bb);
        }

    }

    private void onSelectImageResult(Bitmap image){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG,100,bytes);
        byte bb[] = bytes.toByteArray();
        //image.recycle();
        photos.add(bb);
    }

    private void onDeleteImage(Intent data){
        if(data != null){
            ArrayList<Integer> toDelete = (ArrayList<Integer>) data.getIntegerArrayListExtra("result");

            for(Integer del : toDelete){
                photos.remove((int) del);
            }
        }

    }

    private File createImageFile() throws IOException {

        long timeStamp = System.currentTimeMillis();
        String imageFileName = "NAME_" + timeStamp;
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpeg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] projx = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, projx, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void uploadPhotos() {

        for(byte[] photo : photos){

            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading File....");
            progressDialog.show();

            final String randomKey = UUID.randomUUID().toString();
            // Create a reference
            StorageReference imageRef = storageRef.child("image/" + randomKey);

            imageRef.putBytes(photo)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Snackbar.make(findViewById(android.R.id.content),"Image Uploaded",Snackbar.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Failed Tp Upload", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            progressDialog.setMessage("Progress: " + (int) progressPercent + "%");
                        }
                    });

            referenceImage(imageRef);
        }


    }

    public void referenceImage(StorageReference ref){

        CollectionReference dbPhotos = db.collection("Audit").document(audit_id).collection("Photos");


        Map<String, String> photoMap = new HashMap<>();
        photoMap.put("URI", "gs://" + ref.getBucket() +  ref.getPath());
        // save in firestore
        dbPhotos.add(photoMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(AuditActivity.this, "Photos Added" , Toast.LENGTH_LONG).show();
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AuditActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });



    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        //getSupportFragmentManager().putFragment(outState, "auditSpecsFragment", auditSpecFragment);
        //getSupportFragmentManager().putFragment(outState, "inProcessFragment", inProcessFragment);
    }



    private void saveAuditSpecs() {
        pageAdapter.getAuditSpecFragment().bundleObject();
        AuditObject auditObject = pageAdapter.getAuditSpecFragment().getAuditObject();

        CollectionReference dbAuditSpecs = db.collection("Audit");

        if (audit_id != null) {
            // save to existing audit in firestore
            dbAuditSpecs.document(audit_id).set(auditObject, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(AuditActivity.this, "Audit Information Updated", Toast.LENGTH_LONG).show();

                    CollectionReference dbInProcessSheets = db.collection("Audit").document(audit_id).collection("in-process");
                    CollectionReference dbTechnicalTable = db.collection("Audit").document(audit_id).collection("TechnicalDataTable");
                    CollectionReference dbROMTable = db.collection("Audit").document(audit_id).collection("ROMTable");
                    CollectionReference dbCalibrationTable = db.collection("Audit").document(audit_id).collection("CalibrationTable");
                    CollectionReference dbTrainingTable = db.collection("Audit").document(audit_id).collection("TrainingTable");
                    CollectionReference dbTraceabilityTable = db.collection("Audit").document(audit_id).collection("TraceabilityTable");
                    CollectionReference dbShelfLifeTable = db.collection("Audit").document(audit_id).collection("ShelfLifeTable");
                    CollectionReference dbChecklist = db.collection("Audit").document(audit_id).collection("Checklist");
                    CollectionReference dbPhotos = db.collection("Audit").document(audit_id).collection("Photos");

                    //If audit_id is not null we have to adjust the data by deleting existing data and updating with new data from the fragments
                    if (audit_id != null) {
                        dbInProcessSheets.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                    for (DocumentSnapshot doc: task.getResult()) {
                                        doc.getReference().delete();
                                    }

                                    saveInProcess();
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });

                        dbChecklist.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                    for (DocumentSnapshot doc : task.getResult()) {
                                        doc.getReference().delete();
                                    }

                                    saveChecklist();
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });

                        dbPhotos.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                    for (DocumentSnapshot doc : task.getResult()) {
                                        doc.getReference().delete();
                                    }

                                    uploadPhotos();
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });

                        if (pageAdapter.getTableDataFragment().getTablePageAdapter() != null) {
                            //rebundle table data
                            pageAdapter.getTableDataFragment().bundleObjects();

                            dbTechnicalTable.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {

                                        for (DocumentSnapshot doc : task.getResult()) {
                                            doc.getReference().delete();
                                        }

                                        saveTechnicalDataTable();
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });

                            dbROMTable.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {

                                        for (DocumentSnapshot doc : task.getResult()) {
                                            doc.getReference().delete();
                                        }

                                        saveROMTable();
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });

                            dbCalibrationTable.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {

                                        for (DocumentSnapshot doc : task.getResult()) {
                                            doc.getReference().delete();
                                        }

                                        saveCalibrationTable();
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });

                            dbTrainingTable.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {

                                        for (DocumentSnapshot doc : task.getResult()) {
                                            doc.getReference().delete();
                                        }

                                        saveTrainingTable();
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });

                            dbTraceabilityTable.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {

                                        for (DocumentSnapshot doc : task.getResult()) {
                                            doc.getReference().delete();
                                        }

                                        saveTraceabilityTable();
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });

                            dbShelfLifeTable.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {

                                        for (DocumentSnapshot doc : task.getResult()) {
                                            doc.getReference().delete();
                                        }

                                        saveShelfLifeTable();
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });



                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AuditActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                }
            });
        } else {
            // save new audit in firestore
            dbAuditSpecs.add(auditObject).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    audit_id = documentReference.getId();
                    Toast.makeText(AuditActivity.this, "Audit Information Added", Toast.LENGTH_LONG).show();
                    saveInProcess();
                    saveChecklist();
                    uploadPhotos();
                    if (pageAdapter.getTableDataFragment().getTablePageAdapter() != null) {
                        pageAdapter.getTableDataFragment().bundleObjects();
                        saveTechnicalDataTable();
                        saveROMTable();
                        saveCalibrationTable();
                        saveTrainingTable();
                        saveTraceabilityTable();
                        saveShelfLifeTable();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AuditActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                }
            });
        }

    }


    private void saveInProcess() {
        //pageAdapter.getInProcessFragment().bundleObject();
        List<InProcessObject> inProcessList = pageAdapter.getInProcessFragment().getInProcessList();

        CollectionReference dbInProcessSheets = db.collection("Audit").document(audit_id).collection("in-process");

        // save in firestore - NOTE if there are no inprocess to save the collection will not be created
        for (InProcessObject inProcess: inProcessList) {
            dbInProcessSheets.add(inProcess).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(AuditActivity.this, "In-Process Sheets Added", Toast.LENGTH_LONG).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AuditActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                }
            });
        }

//        try {
//            createPdf(inProcessList);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

    }

    public void saveTechnicalDataTable() {
        if (pageAdapter.getTableDataFragment().getTablePageAdapter().getTechDataFragment().getFragmentView() != null) {
            TechnicalTableDataObject techObject = pageAdapter.getTableDataFragment().getTechnicalTableDataObject();

            CollectionReference dbTechnicalTable = db.collection("Audit").document(audit_id).collection("TechnicalDataTable");

            // save in firestore
            dbTechnicalTable.add(techObject).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(AuditActivity.this, "Technical Data Table Added", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AuditActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                }
            });
        }
    }

    public void saveROMTable() {
        if (pageAdapter.getTableDataFragment().getTablePageAdapter().getRomTableFragment().getFragmentView() != null) {
            ROMTableDataObject romObject = pageAdapter.getTableDataFragment().getRomTableDataObject();

            CollectionReference dbROMTable = db.collection("Audit").document(audit_id).collection("ROMTable");

            // save in firestore
            dbROMTable.add(romObject).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(AuditActivity.this, "ROM Table Added", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AuditActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                }
            });
        }
    }

    public void saveCalibrationTable() {
        if (pageAdapter.getTableDataFragment().getTablePageAdapter().getCalTableFragment().getFragmentView() != null) {
            CalibrationTableDataObject calObject = pageAdapter.getTableDataFragment().getCalibrationTableDataObject();

            CollectionReference dbCalibrationTable = db.collection("Audit").document(audit_id).collection("CalibrationTable");

            // save in firestore
            dbCalibrationTable.add(calObject).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(AuditActivity.this, "Calibration Table Added", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AuditActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                }
            });
        }
    }

    public void saveTrainingTable() {
        if (pageAdapter.getTableDataFragment().getTablePageAdapter().getTrainTableFragment().getFragmentView() != null) {
            TrainingTableDataObject trainObject = pageAdapter.getTableDataFragment().getTrainingTableDataObject();

            CollectionReference dbTrainingTable = db.collection("Audit").document(audit_id).collection("TrainingTable");

            // save in firestore
            dbTrainingTable.add(trainObject).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(AuditActivity.this, "Training Table Added", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AuditActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void saveTraceabilityTable() {
        if (pageAdapter.getTableDataFragment().getTablePageAdapter().getTraceTableFragment().getFragmentView() != null) {
            TraceabilityTableDataObject traceObject = pageAdapter.getTableDataFragment().getTraceabilityTableDataObject();

            CollectionReference dbTraceabilityTable = db.collection("Audit").document(audit_id).collection("TraceabilityTable");

            // save in firestore
            dbTraceabilityTable.add(traceObject).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(AuditActivity.this, "Traceability Table Added", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AuditActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                }
            });
        }
    }

    public void saveShelfLifeTable() {
        if (pageAdapter.getTableDataFragment().getTablePageAdapter().getShelfTableFragment().getFragmentView() != null) {
            ShelfLifeTableDataObject shelfObject = pageAdapter.getTableDataFragment().getShelfLifeTableDataObject();

            CollectionReference dbShelfLifeTable = db.collection("Audit").document(audit_id).collection("ShelfLifeTable");

            // save in firestore
            dbShelfLifeTable.add(shelfObject).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(AuditActivity.this, "Shelf Life Table Added", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AuditActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                }
            });
        }
    }


    // Save checklist to firestore database
    public void saveChecklist() {
        if(pageAdapter.getChecklistFragment().getQuestionAdapter() != null) {
            ChecklistDataObject checklistDataObject = pageAdapter.getChecklistFragment().getChecklistDataObject();

            CollectionReference dbChecklist = db.collection("Audit").document(audit_id).collection("Checklist");

            int checklistNum = checklistDataObject.getChecklistId();
            Map<String, Integer> Idmap = new HashMap<>();
            Idmap.put("checklistID", checklistNum);
            dbChecklist.add(Idmap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(AuditActivity.this, "Checklist Added" , Toast.LENGTH_LONG).show();
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AuditActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });


            for(int i=1; i < checklistDataObject.size() + 1; i++){

                String json_str = new Gson().toJson(checklistDataObject.get(i));

                //checklistDataObject.setMapString(json_str);

                Map<String, String> map = new HashMap<>();
                map.put(String.valueOf(i), json_str);

                // save in firestore
                dbChecklist.add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Toast.makeText(AuditActivity.this, "Checklist Added" , Toast.LENGTH_LONG).show();
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AuditActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


            }

        }
    }

    public void createChecklistPdf(ChecklistDataObject checklistDataObject) throws FileNotFoundException, IOException {
        if(pageAdapter.getChecklistFragment().getChecklistDataObject() != null) {
            String titleName = pageAdapter.getAuditSpecFragment().getAuditObject().getAuditTitleObj().replaceAll("[^a-zA-Z0-9]", "");
            String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
            File file = new File(pdfPath, titleName + PDF);
//            File file = new File(pdfPath,  "TestInProcess.pdf");

            OutputStream outputStream = new FileOutputStream(file);

            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            Drawable drawable = getDrawable(R.drawable.united_airlines_quality_assurance_logo);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapData = stream.toByteArray();

            ImageData imageData = ImageDataFactory.create(bitmapData);
            Image image = new Image(imageData);
            image.setHeight(40);
            image.setWidth(250);

            PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
            PdfFont bold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);

            Text header_text = new Text("Technical Operations Quality Assurance").setFont(font);
            Paragraph pdf_header = new Paragraph()
                    .add(header_text);

            document.add(image);
            document.add(pdf_header);

            // 600
            float columnWidth[] = {350f, 100f, 150f};
            Table table = new Table(columnWidth);

            Text column_1_chklist_header = new Text("General Information");
            Text column_2_answer = new Text("Yes / No / N/A");
            Text column_3_comment = new Text("Comment");

            Paragraph chklist_header_1 = new Paragraph()
                    .add(column_1_chklist_header);
            table.addCell(chklist_header_1);

            Paragraph chklist_header_2 = new Paragraph()
                    .add(column_2_answer);
            table.addCell(chklist_header_2);

            Paragraph chklist_header_3 = new Paragraph()
                    .add(column_3_comment);
            table.addCell(chklist_header_3);

            for(int i = 1; i <= checklistDataObject.size(); i++) {
                Map<Integer, String[]> number = checklistDataObject.get(i);
                for (Map.Entry<Integer, String[]> entry : number.entrySet()) {
                    List<String> indiv = Arrays.asList(entry.getValue());
                    for(String answer : indiv) {
                        table.addCell(answer).setFont(font);
                    }
                }
            }

            document.add(table);

            Paragraph space = new Paragraph("");
            document.add(space);

            createInProcessPdf(pageAdapter.getInProcessFragment().getInProcessList(), document);
            createTechTablePdf(pageAdapter.getTableDataFragment().getTechnicalTableDataObject(), document);
            createROMTablePdf(pageAdapter.getTableDataFragment().getRomTableDataObject(), document);
            createCalibrationPdf(pageAdapter.getTableDataFragment().getCalibrationTableDataObject(), document);
            createTrainingPdf(pageAdapter.getTableDataFragment().getTrainingTableDataObject(), document);
            createTraceabilityPdf(pageAdapter.getTableDataFragment().getTraceabilityTableDataObject(), document);
            createShelfLifePdf(pageAdapter.getTableDataFragment().getShelfLifeTableDataObject(), document);

            document.close();
            Toast.makeText(getApplicationContext(), "PDF Created", Toast.LENGTH_LONG).show();
        }
//        }
    }

    public void createInProcessPdf(List<InProcessObject> inProcessList, Document document) throws FileNotFoundException, IOException {
        if(pageAdapter.getInProcessFragment().getInProcessList() != null) {
            for (InProcessObject inProcessObject : inProcessList) {
                PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
                PdfFont bold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);

                float columnWidth[] = {150f, 150f, 150f, 150f};
                Table table1 = new Table(columnWidth);

                Text column_1_ip = new Text("Employee Name / ID").setFont(bold);
                Text column_2_ip = new Text("Part Number").setFont(bold);
                Text column_3_ip = new Text("Serial Number").setFont(bold);
                Text column_4_ip = new Text("Nomeclature").setFont(bold);

                Paragraph ip_header_1 = new Paragraph()
                        .add(column_1_ip);
                table1.addCell(ip_header_1);

                Paragraph ip_header_2 = new Paragraph()
                        .add(column_2_ip);
                table1.addCell(ip_header_2);

                Paragraph ip_header_3 = new Paragraph()
                        .add(column_3_ip);
                table1.addCell(ip_header_3);

                Paragraph ip_header_4 = new Paragraph()
                        .add(column_4_ip);
                table1.addCell(ip_header_4);

                // add cell
                table1.addCell(inProcessObject.getEmployeeNameObj()).setFont(font);;
                table1.addCell(inProcessObject.getPartNumberObj()).setFont(font);;
                table1.addCell(inProcessObject.getSerialNumberObj()).setFont(font);;
                table1.addCell(inProcessObject.getNomenclatureObj()).setFont(font);;

                float columnWidth2[] = {100f, 500f};
                Table table2 = new Table(columnWidth2);

                Text task_row_ip = new Text("Task: (include customer name)").setFont(bold);
                Paragraph ip_row3 = new Paragraph()
                        .add(task_row_ip).setMaxHeight(500f);
                table2.addCell(ip_row3);
                table2.addCell(inProcessObject.getTaskObj()).setMaxHeight(500f).setFont(font);

                float columnWidth3[] = {200f, 400f};
                Table table3 = new Table(columnWidth3);

                Text techspec_row_ip = new Text("Technical Specification - Record type of spec, revision date and revision level ").setFont(bold);
                Paragraph ip_row_ts = new Paragraph()
                        .add(techspec_row_ip).setMaxHeight(400f);
                table3.addCell(ip_row_ts);
                table3.addCell(inProcessObject.getTechSpecificationsObj()).setMaxHeight(400f).setFont(font);

                float columnWidth4[] = {200f, 400f};
                Table table4 = new Table(columnWidth4);

                Text tooling_row_ip = new Text("Tooling – Record ID and due date, if any, per box ").setFont(bold);
                Paragraph ip_row_tooling = new Paragraph()
                        .add(tooling_row_ip).setMaxHeight(400f);
                table4.addCell(ip_row_tooling);
                table4.addCell(inProcessObject.getToolingObj()).setMaxHeight(400f).setFont(font);

                float columnWidth5[] = {200f, 400f};
                Table table5 = new Table(columnWidth5);

                Text sl_row_ip = new Text("Shelf Life – Record Item and due date, if any, per box ").setFont(bold);
                Paragraph ip_sl_tooling = new Paragraph()
                        .add(sl_row_ip).setMaxHeight(400f);
                table5.addCell(ip_sl_tooling);
                table5.addCell(inProcessObject.getShelfLifeObj()).setMaxHeight(400f).setFont(font);

                float columnWidth6[] = {200f, 400f};
                Table table6 = new Table(columnWidth6);

                Text trace_row_ip = new Text("Traceability (outside of Stores)").setFont(bold);
                Paragraph ip_trace_tooling = new Paragraph()
                        .add(trace_row_ip).setMaxHeight(400f);
                table6.addCell(ip_trace_tooling);
                table6.addCell(inProcessObject.getTraceObj()).setMaxHeight(400f).setFont(font);

                float columnWidth7[] = {300f, 300f};
                Table table7 = new Table(columnWidth7);

                Text reqTraining_row_ip = new Text("Required Training").setFont(bold);
                Text date_row_ip = new Text("Date Qualified").setFont(bold);

                Paragraph ip_rt_tooling_1 = new Paragraph()
                        .add(reqTraining_row_ip).setMaxHeight(400f);

                Paragraph ip_rt_tooling_2 = new Paragraph()
                        .add(date_row_ip).setMaxHeight(400f);

                table7.addCell(ip_rt_tooling_1);
                table7.addCell(ip_rt_tooling_2);

                table7.addCell(inProcessObject.getReqTrainingObj()).setMaxHeight(400f).setFont(font);
                table7.addCell(inProcessObject.getTrainingDateObj()).setMaxHeight(400f).setFont(font);

                document.add(table1);
                document.add(table2);
                document.add(table3);
                document.add(table4);
                document.add(table5);
                document.add(table6);
                document.add(table7);

                Paragraph space = new Paragraph("");
                document.add(space);

            }
        }
        else {
            Toast.makeText(getApplicationContext(), "PDF not created", Toast.LENGTH_LONG).show();
        }

    }

    public void createTechTablePdf(TechnicalTableDataObject techObject, Document document) throws FileNotFoundException, IOException {
        if(pageAdapter.getTableDataFragment().getTechnicalTableDataObject() != null) {
            PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
            PdfFont bold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);

            float columnWidth_h[] = {600f};
            Table table_h = new Table(columnWidth_h);

            Text tech = new Text("TECHNICAL DATA ").setFont(bold);
            Paragraph tech_data = new Paragraph()
                    .add(tech);
            table_h.addCell(tech_data);

            float columnWidth[] = {200f, 200f, 200f, 200f, 200f, 200f};
            Table table = new Table(columnWidth);

            // add cell
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Part Number/Aircraft/Eng Effectively Num")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Manufacturer")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("ATA/Document ID")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Rev. Level")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Rev. Date")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Comments")).setFont(font));


            List<String> row1 = techObject.getRow1();
            for(String addRow1 : row1) {
                table.addCell(addRow1).setFont(font);
            }

            List<String> row2 = techObject.getRow2();
            for(String addRow2 : row2) {
                if(addRow2.equals("")) {
                    table.addCell(" ").setFont(font);
                }
                table.addCell(addRow2).setFont(font);
            }

            List<String> row3 = techObject.getRow3();
            for(String addRow3 : row3) {
                table.addCell(addRow3).setFont(font);
            }

            List<String> row4 = techObject.getRow4();
            for(String addRow4 : row4) {
                table.addCell(addRow4).setFont(font);
            }

            List<String> row5 = techObject.getRow5();
            for(String addRow5 : row5) {
                table.addCell(addRow5).setFont(font);
            }

            List<String> row6 = techObject.getRow6();
            for(String addRow6 : row6) {
                table.addCell(addRow6).setFont(font);
            }

            List<String> row7 = techObject.getRow7();
            for(String addRow7 : row7) {
                table.addCell(addRow7).setFont(font);
            }

            List<String> row8 = techObject.getRow8();
            for(String addRow8 : row8) {
                table.addCell(addRow8).setFont(font);
            }

            List<String> row9 = techObject.getRow9();
            for(String addRow9 : row9) {
                table.addCell(addRow9).setFont(font);
            }

            List<String> row10 = techObject.getRow10();
            for(String addRow10 : row10) {
                table.addCell(addRow10).setFont(font);
            }

            List<String> row11 = techObject.getRow11();
            for(String addRow11 : row11) {
                table.addCell(addRow11).setFont(font);
            }

            List<String> row12 = techObject.getRow12();
            for(String addRow12 : row12) {
                table.addCell(addRow12).setFont(font);
            }

            List<String> row13 = techObject.getRow13();
            for(String addRow13 : row13) {
                table.addCell(addRow13).setFont(font);
            }

            List<String> row14 = techObject.getRow14();
            for(String addRow14 : row14) {
                table.addCell(addRow14).setFont(font);
            }

            document.add(table_h);
            document.add(table);

            Paragraph space = new Paragraph("");
            document.add(space);
        }
        else {
            PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
            PdfFont bold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);

            float columnWidth_h[] = {600f};
            Table table_h = new Table(columnWidth_h);

            Text tech = new Text("TECHNICAL DATA ").setFont(bold);
            Paragraph tech_data = new Paragraph()
                    .add(tech);
            table_h.addCell(tech_data);

            float columnWidth[] = {200f, 200f, 200f, 200f, 200f, 200f};
            Table table = new Table(columnWidth);

            // add cell
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Part Number/Aircraft/Eng Effectively Num")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Manufacturer")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("ATA/Document ID")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Rev. Level")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Rev. Date")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Comments")).setFont(font));


            List<String> row1 = techObject.getRow1();
            for(String addRow1 : row1) {
                table.addCell(addRow1).setFont(font);
            }

            List<String> row2 = techObject.getRow2();
            for(String addRow2 : row2) {
                table.addCell(addRow2).setFont(font);
            }

            List<String> row3 = techObject.getRow3();
            for(String addRow3 : row3) {
                table.addCell(addRow3).setFont(font);
            }

            List<String> row4 = techObject.getRow4();
            for(String addRow4 : row4) {
                table.addCell(addRow4).setFont(font);
            }

            List<String> row5 = techObject.getRow5();
            for(String addRow5 : row5) {
                table.addCell(addRow5).setFont(font);
            }

            List<String> row6 = techObject.getRow6();
            for(String addRow6 : row6) {
                table.addCell(addRow6).setFont(font);
            }

            List<String> row7 = techObject.getRow7();
            for(String addRow7 : row7) {
                table.addCell(addRow7).setFont(font);
            }

            List<String> row8 = techObject.getRow8();
            for(String addRow8 : row8) {
                table.addCell(addRow8).setFont(font);
            }

            List<String> row9 = techObject.getRow9();
            for(String addRow9 : row9) {
                table.addCell(addRow9).setFont(font);
            }

            List<String> row10 = techObject.getRow10();
            for(String addRow10 : row10) {
                table.addCell(addRow10).setFont(font);
            }

            List<String> row11 = techObject.getRow11();
            for(String addRow11 : row11) {
                table.addCell(addRow11).setFont(font);
            }

            List<String> row12 = techObject.getRow12();
            for(String addRow12 : row12) {
                table.addCell(addRow12).setFont(font);
            }

            List<String> row13 = techObject.getRow13();
            for(String addRow13 : row13) {
                table.addCell(addRow13).setFont(font);
            }

            List<String> row14 = techObject.getRow14();
            for(String addRow14 : row14) {
                table.addCell(addRow14).setFont(font);
            }

            document.add(table_h);
            document.add(table);

            Paragraph space = new Paragraph("");
            document.add(space);
        }
    }

    public void createROMTablePdf(ROMTableDataObject romTableDataObject, Document document) throws FileNotFoundException, IOException {
        if(pageAdapter.getTableDataFragment().getRomTableDataObject() != null) {
            PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
            PdfFont bold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);

            float columnWidth_h[] = {600f};
            Table table_h = new Table(columnWidth_h);

            Text rom = new Text("RECORDS of MAINTENANCE (ROM)").setFont(bold);
            Paragraph rom_data = new Paragraph()
                    .add(rom);
            table_h.addCell(rom_data);

            float columnWidth[] = {200f, 200f, 200f, 200f, 200f, 200f};
            Table table = new Table(columnWidth);

            // add cell
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Doc Type")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Document #")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Part/PN/SN")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Date")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Specification incl. revision level & date")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Tech. Name")).setFont(font));

            List<String> row1 = romTableDataObject.getRow1();
            for(String addRow1 : row1) {
                table.addCell(addRow1).setFont(font);
            }

            List<String> row2 = romTableDataObject.getRow2();
            for(String addRow2 : row2) {
                table.addCell(addRow2).setFont(font);
            }

            List<String> row3 = romTableDataObject.getRow3();
            for(String addRow3 : row3) {
                table.addCell(addRow3).setFont(font);
            }

            List<String> row4 = romTableDataObject.getRow4();
            for(String addRow4 : row4) {
                table.addCell(addRow4).setFont(font);
            }

            List<String> row5 = romTableDataObject.getRow5();
            for(String addRow5 : row5) {
                table.addCell(addRow5).setFont(font);
            }

            List<String> row6 = romTableDataObject.getRow6();
            for(String addRow6 : row6) {
                table.addCell(addRow6).setFont(font);
            }

            List<String> row7 = romTableDataObject.getRow7();
            for(String addRow7 : row7) {
                table.addCell(addRow7).setFont(font);
            }

            List<String> row8 = romTableDataObject.getRow8();
            for(String addRow8 : row8) {
                table.addCell(addRow8).setFont(font);
            }

            List<String> row9 = romTableDataObject.getRow9();
            for(String addRow9 : row9) {
                table.addCell(addRow9).setFont(font);
            }

            List<String> row10 = romTableDataObject.getRow10();
            for(String addRow10 : row10) {
                table.addCell(addRow10).setFont(font);
            }

            document.add(table_h);
            document.add(table);

            Paragraph space = new Paragraph("");
            document.add(space);
        }
        else {
            PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
            PdfFont bold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);

            float columnWidth_h[] = {600f};
            Table table_h = new Table(columnWidth_h);

            Text rom = new Text("RECORDS of MAINTENANCE (ROM)").setFont(bold);
            Paragraph rom_data = new Paragraph()
                    .add(rom);
            table_h.addCell(rom_data);

            float columnWidth[] = {200f, 200f, 200f, 200f, 200f, 200f};
            Table table = new Table(columnWidth);

            // add cell
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Doc Type")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Document #")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Part/PN/SN")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Date")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Specification incl. revision level & date")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Tech. Name")).setFont(font));

            List<String> row1 = romTableDataObject.getRow1();
            for(String addRow1 : row1) {
                table.addCell(addRow1).setFont(font);
            }

            List<String> row2 = romTableDataObject.getRow2();
            for(String addRow2 : row2) {
                table.addCell(addRow2).setFont(font);
            }

            List<String> row3 = romTableDataObject.getRow3();
            for(String addRow3 : row3) {
                table.addCell(addRow3).setFont(font);
            }

            List<String> row4 = romTableDataObject.getRow4();
            for(String addRow4 : row4) {
                table.addCell(addRow4).setFont(font);
            }

            List<String> row5 = romTableDataObject.getRow5();
            for(String addRow5 : row5) {
                table.addCell(addRow5).setFont(font);
            }

            List<String> row6 = romTableDataObject.getRow6();
            for(String addRow6 : row6) {
                table.addCell(addRow6).setFont(font);
            }

            List<String> row7 = romTableDataObject.getRow7();
            for(String addRow7 : row7) {
                table.addCell(addRow7).setFont(font);
            }

            List<String> row8 = romTableDataObject.getRow8();
            for(String addRow8 : row8) {
                table.addCell(addRow8).setFont(font);
            }

            List<String> row9 = romTableDataObject.getRow9();
            for(String addRow9 : row9) {
                table.addCell(addRow9).setFont(font);
            }

            List<String> row10 = romTableDataObject.getRow10();
            for(String addRow10 : row10) {
                table.addCell(addRow10).setFont(font);
            }

            document.add(table_h);
            document.add(table);

            Paragraph space = new Paragraph("");
            document.add(space);
        }
    }

    public void createCalibrationPdf(CalibrationTableDataObject calibrationTableDataObject, Document document) throws FileNotFoundException, IOException {
        if(pageAdapter.getTableDataFragment().getCalibrationTableDataObject() != null) {
            PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
            PdfFont bold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);

            float columnWidth_h[] = {600f};
            Table table_h = new Table(columnWidth_h);

            Text calib = new Text("CALIBRATION").setFont(bold);
            Paragraph calib_data = new Paragraph()
                    .add(calib);
            table_h.addCell(calib_data);

            float columnWidth[] = {200f, 200f, 200f, 200f, 200f};
            Table table = new Table(columnWidth);

            // add cell
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Item")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("ID #")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("CAL Date")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("CAL Due")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("CAL by")).setFont(font));

            List<String> row1 = calibrationTableDataObject.getRow1();
            for(String addRow1 : row1) {
                table.addCell(addRow1).setFont(font);
            }

            List<String> row2 = calibrationTableDataObject.getRow2();
            for(String addRow2 : row2) {
                table.addCell(addRow2).setFont(font);
            }

            List<String> row3 = calibrationTableDataObject.getRow3();
            for(String addRow3 : row3) {
                table.addCell(addRow3).setFont(font);
            }

            List<String> row4 = calibrationTableDataObject.getRow4();
            for(String addRow4 : row4) {
                table.addCell(addRow4).setFont(font);
            }

            List<String> row5 = calibrationTableDataObject.getRow5();
            for(String addRow5 : row5) {
                table.addCell(addRow5).setFont(font);
            }

            List<String> row6 = calibrationTableDataObject.getRow6();
            for(String addRow6 : row6) {
                table.addCell(addRow6).setFont(font);
            }

            List<String> row7 = calibrationTableDataObject.getRow7();
            for(String addRow7 : row7) {
                table.addCell(addRow7).setFont(font);
            }

            List<String> row8 = calibrationTableDataObject.getRow8();
            for(String addRow8 : row8) {
                table.addCell(addRow8).setFont(font);
            }

            List<String> row9 = calibrationTableDataObject.getRow9();
            for(String addRow9 : row9) {
                table.addCell(addRow9).setFont(font);
            }

            List<String> row10 = calibrationTableDataObject.getRow10();
            for(String addRow10 : row10) {
                table.addCell(addRow10).setFont(font);
            }

            List<String> row11 = calibrationTableDataObject.getRow11();
            for(String addRow11 : row11) {
                table.addCell(addRow11).setFont(font);
            }

            List<String> row12 = calibrationTableDataObject.getRow12();
            for(String addRow12 : row12) {
                table.addCell(addRow12).setFont(font);
            }

            List<String> row13 = calibrationTableDataObject.getRow13();
            for(String addRow13 : row13) {
                table.addCell(addRow13).setFont(font);
            }

            List<String> row14 = calibrationTableDataObject.getRow14();
            for(String addRow14 : row14) {
                table.addCell(addRow14).setFont(font);
            }

            List<String> row15 = calibrationTableDataObject.getRow15();
            for(String addRow15 : row15) {
                table.addCell(addRow15).setFont(font);
            }

            document.add(table_h);
            document.add(table);

            Paragraph space = new Paragraph("");
            document.add(space);
        }
        else {
            PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
            PdfFont bold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);

            float columnWidth_h[] = {600f};
            Table table_h = new Table(columnWidth_h);

            Text calib = new Text("CALIBRATION").setFont(bold);
            Paragraph calib_data = new Paragraph()
                    .add(calib);
            table_h.addCell(calib_data);

            float columnWidth[] = {200f, 200f, 200f, 200f, 200f};
            Table table = new Table(columnWidth);

            // add cell
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Item")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("ID #")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("CAL Date")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("CAL Due")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("CAL by")).setFont(font));

            List<String> row1 = calibrationTableDataObject.getRow1();
            for(String addRow1 : row1) {
                table.addCell(addRow1).setFont(font);
            }

            List<String> row2 = calibrationTableDataObject.getRow2();
            for(String addRow2 : row2) {
                table.addCell(addRow2).setFont(font);
            }

            List<String> row3 = calibrationTableDataObject.getRow3();
            for(String addRow3 : row3) {
                table.addCell(addRow3).setFont(font);
            }

            List<String> row4 = calibrationTableDataObject.getRow4();
            for(String addRow4 : row4) {
                table.addCell(addRow4).setFont(font);
            }

            List<String> row5 = calibrationTableDataObject.getRow5();
            for(String addRow5 : row5) {
                table.addCell(addRow5).setFont(font);
            }

            List<String> row6 = calibrationTableDataObject.getRow6();
            for(String addRow6 : row6) {
                table.addCell(addRow6).setFont(font);
            }

            List<String> row7 = calibrationTableDataObject.getRow7();
            for(String addRow7 : row7) {
                table.addCell(addRow7).setFont(font);
            }

            List<String> row8 = calibrationTableDataObject.getRow8();
            for(String addRow8 : row8) {
                table.addCell(addRow8).setFont(font);
            }

            List<String> row9 = calibrationTableDataObject.getRow9();
            for(String addRow9 : row9) {
                table.addCell(addRow9).setFont(font);
            }

            List<String> row10 = calibrationTableDataObject.getRow10();
            for(String addRow10 : row10) {
                table.addCell(addRow10).setFont(font);
            }

            List<String> row11 = calibrationTableDataObject.getRow11();
            for(String addRow11 : row11) {
                table.addCell(addRow11).setFont(font);
            }

            List<String> row12 = calibrationTableDataObject.getRow12();
            for(String addRow12 : row12) {
                table.addCell(addRow12).setFont(font);
            }

            List<String> row13 = calibrationTableDataObject.getRow13();
            for(String addRow13 : row13) {
                table.addCell(addRow13).setFont(font);
            }

            List<String> row14 = calibrationTableDataObject.getRow14();
            for(String addRow14 : row14) {
                table.addCell(addRow14).setFont(font);
            }

            List<String> row15 = calibrationTableDataObject.getRow15();
            for(String addRow15 : row15) {
                table.addCell(addRow15).setFont(font);
            }

            document.add(table_h);
            document.add(table);

            Paragraph space = new Paragraph("");
            document.add(space);
        }
    }

    public void createTrainingPdf(TrainingTableDataObject trainingTableDataObject, Document document) throws FileNotFoundException, IOException {
        if(pageAdapter.getTableDataFragment().getTrainingTableDataObject() != null) {
            PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
            PdfFont bold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);

            float columnWidth_h[] = {600f};
            Table table_h = new Table(columnWidth_h);

            Text td = new Text("TRAINING (Non-Interviewed Personnel)").setFont(bold);
            Paragraph training_data_header = new Paragraph()
                    .add(td);
            table_h.addCell(training_data_header);

            float columnWidth[] = {200f, 200f, 200f, 200f};
            Table table = new Table(columnWidth);

            // add cell
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Technician Name/ID")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Part Number/Maintenance Performed")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Required Training")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("OJT/Qualified Date")).setFont(font));

            List<String> row1 = trainingTableDataObject.getRow1();
            for(String addRow1 : row1) {
                table.addCell(addRow1).setFont(font);
            }

            List<String> row2 = trainingTableDataObject.getRow2();
            for(String addRow2 : row2) {
                table.addCell(addRow2).setFont(font);
            }

            List<String> row3 = trainingTableDataObject.getRow3();
            for(String addRow3 : row3) {
                table.addCell(addRow3).setFont(font);
            }

            List<String> row4 = trainingTableDataObject.getRow4();
            for(String addRow4 : row4) {
                table.addCell(addRow4).setFont(font);
            }

            List<String> row5 = trainingTableDataObject.getRow5();
            for(String addRow5 : row5) {
                table.addCell(addRow5).setFont(font);
            }

            List<String> row6 = trainingTableDataObject.getRow6();
            for(String addRow6 : row6) {
                table.addCell(addRow6).setFont(font);
            }

            List<String> row7 = trainingTableDataObject.getRow7();
            for(String addRow7 : row7) {
                table.addCell(addRow7).setFont(font);
            }

            List<String> row8 = trainingTableDataObject.getRow8();
            for(String addRow8 : row8) {
                table.addCell(addRow8).setFont(font);
            }

            List<String> row9 = trainingTableDataObject.getRow9();
            for(String addRow9 : row9) {
                table.addCell(addRow9).setFont(font);
            }

            List<String> row10 = trainingTableDataObject.getRow10();
            for(String addRow10 : row10) {
                table.addCell(addRow10).setFont(font);
            }

            document.add(table_h);
            document.add(table);

            Paragraph space = new Paragraph("");
            document.add(space);
        }
        else {
            PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
            PdfFont bold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);

            float columnWidth_h[] = {600f};
            Table table_h = new Table(columnWidth_h);

            Text td = new Text("TRAINING (Non-Interviewed Personnel)").setFont(bold);
            Paragraph training_data_header = new Paragraph()
                    .add(td);
            table_h.addCell(training_data_header);

            float columnWidth[] = {200f, 200f, 200f, 200f};
            Table table = new Table(columnWidth);

            // add cell
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Technician Name/ID")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Part Number/Maintenance Performed")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Required Training")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("OJT/Qualified Date")).setFont(font));

            List<String> row1 = trainingTableDataObject.getRow1();
            for(String addRow1 : row1) {
                table.addCell(addRow1).setFont(font);
            }

            List<String> row2 = trainingTableDataObject.getRow2();
            for(String addRow2 : row2) {
                table.addCell(addRow2).setFont(font);
            }

            List<String> row3 = trainingTableDataObject.getRow3();
            for(String addRow3 : row3) {
                table.addCell(addRow3).setFont(font);
            }

            List<String> row4 = trainingTableDataObject.getRow4();
            for(String addRow4 : row4) {
                table.addCell(addRow4).setFont(font);
            }

            List<String> row5 = trainingTableDataObject.getRow5();
            for(String addRow5 : row5) {
                table.addCell(addRow5).setFont(font);
            }

            List<String> row6 = trainingTableDataObject.getRow6();
            for(String addRow6 : row6) {
                table.addCell(addRow6).setFont(font);
            }

            List<String> row7 = trainingTableDataObject.getRow7();
            for(String addRow7 : row7) {
                table.addCell(addRow7).setFont(font);
            }

            List<String> row8 = trainingTableDataObject.getRow8();
            for(String addRow8 : row8) {
                table.addCell(addRow8).setFont(font);
            }

            List<String> row9 = trainingTableDataObject.getRow9();
            for(String addRow9 : row9) {
                table.addCell(addRow9).setFont(font);
            }

            List<String> row10 = trainingTableDataObject.getRow10();
            for(String addRow10 : row10) {
                table.addCell(addRow10).setFont(font);
            }

            document.add(table_h);
            document.add(table);

            Paragraph space = new Paragraph("");
            document.add(space);
        }
    }

    public void createTraceabilityPdf(TraceabilityTableDataObject traceabilityTableDataObject, Document document) throws FileNotFoundException, IOException {
        if(pageAdapter.getTableDataFragment().getTraceabilityTableDataObject() != null) {
            PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
            PdfFont bold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);

            float columnWidth_h[] = {600f};
            Table table_h = new Table(columnWidth_h);

            Text trace = new Text("TRACEABILITY").setFont(bold);
            Paragraph trace_data_header = new Paragraph()
                    .add(trace);
            table_h.addCell(trace_data_header);

            float columnWidth[] = {200f, 200f, 200f, 200f, 200f};
            Table table = new Table(columnWidth);

            // add cell
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Product")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Part Number")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Batch/Lot #")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("P.O Number")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Supplier")).setFont(font));

            List<String> row1 = traceabilityTableDataObject.getRow1();
            for(String addRow1 : row1) {
                table.addCell(addRow1).setFont(font);
            }

            List<String> row2 = traceabilityTableDataObject.getRow2();
            for(String addRow2 : row2) {
                table.addCell(addRow2).setFont(font);
            }

            List<String> row3 = traceabilityTableDataObject.getRow3();
            for(String addRow3 : row3) {
                table.addCell(addRow3).setFont(font);
            }

            List<String> row4 = traceabilityTableDataObject.getRow4();
            for(String addRow4 : row4) {
                table.addCell(addRow4).setFont(font);
            }

            List<String> row5 = traceabilityTableDataObject.getRow5();
            for(String addRow5 : row5) {
                table.addCell(addRow5).setFont(font);
            }

            List<String> row6 = traceabilityTableDataObject.getRow6();
            for(String addRow6 : row6) {
                table.addCell(addRow6).setFont(font);
            }

            List<String> row7 = traceabilityTableDataObject.getRow7();
            for(String addRow7 : row7) {
                table.addCell(addRow7).setFont(font);
            }

            List<String> row8 = traceabilityTableDataObject.getRow8();
            for(String addRow8 : row8) {
                table.addCell(addRow8).setFont(font);
            }

            List<String> row9 = traceabilityTableDataObject.getRow9();
            for(String addRow9 : row9) {
                table.addCell(addRow9).setFont(font);
            }

            List<String> row10 = traceabilityTableDataObject.getRow10();
            for(String addRow10 : row10) {
                table.addCell(addRow10).setFont(font);
            }

            document.add(table_h);
            document.add(table);

            Paragraph space = new Paragraph("");
            document.add(space);
        }
        else {
            PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
            PdfFont bold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);

            float columnWidth_h[] = {600f};
            Table table_h = new Table(columnWidth_h);

            Text trace = new Text("TRACEABILITY").setFont(bold);
            Paragraph trace_data_header = new Paragraph()
                    .add(trace);
            table_h.addCell(trace_data_header);

            float columnWidth[] = {200f, 200f, 200f, 200f, 200f};
            Table table = new Table(columnWidth);

            // add cell
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Product")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Part Number")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Batch/Lot #")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("P.O Number")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Supplier")).setFont(font));

            List<String> row1 = traceabilityTableDataObject.getRow1();
            for(String addRow1 : row1) {
                table.addCell(addRow1).setFont(font);
            }

            List<String> row2 = traceabilityTableDataObject.getRow2();
            for(String addRow2 : row2) {
                table.addCell(addRow2).setFont(font);
            }

            List<String> row3 = traceabilityTableDataObject.getRow3();
            for(String addRow3 : row3) {
                table.addCell(addRow3).setFont(font);
            }

            List<String> row4 = traceabilityTableDataObject.getRow4();
            for(String addRow4 : row4) {
                table.addCell(addRow4).setFont(font);
            }

            List<String> row5 = traceabilityTableDataObject.getRow5();
            for(String addRow5 : row5) {
                table.addCell(addRow5).setFont(font);
            }

            List<String> row6 = traceabilityTableDataObject.getRow6();
            for(String addRow6 : row6) {
                table.addCell(addRow6).setFont(font);
            }

            List<String> row7 = traceabilityTableDataObject.getRow7();
            for(String addRow7 : row7) {
                table.addCell(addRow7).setFont(font);
            }

            List<String> row8 = traceabilityTableDataObject.getRow8();
            for(String addRow8 : row8) {
                table.addCell(addRow8).setFont(font);
            }

            List<String> row9 = traceabilityTableDataObject.getRow9();
            for(String addRow9 : row9) {
                table.addCell(addRow9).setFont(font);
            }

            List<String> row10 = traceabilityTableDataObject.getRow10();
            for(String addRow10 : row10) {
                table.addCell(addRow10).setFont(font);
            }

            document.add(table_h);
            document.add(table);

            Paragraph space = new Paragraph("");
            document.add(space);
        }
    }

    public void createShelfLifePdf(ShelfLifeTableDataObject shelfLifeTableDataObject, Document document) throws FileNotFoundException, IOException {
        if(pageAdapter.getTableDataFragment().getShelfLifeTableDataObject() != null) {
            PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
            PdfFont bold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);

            float columnWidth_h[] = {600f};
            Table table_h = new Table(columnWidth_h);

            Text shelf_life = new Text("SHELF LIFE").setFont(bold);
            Paragraph shelf_life_data_header = new Paragraph()
                    .add(shelf_life);
            table_h.addCell(shelf_life_data_header);

            float columnWidth[] = {200f, 200f, 200f, 200f, 200f};
            Table table = new Table(columnWidth);

            // add cell
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Item")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("P/N")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Lot/Batch #")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("DOM")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("DOE")).setFont(font));

            List<String> row1 = shelfLifeTableDataObject.getRow1();
            for(String addRow1 : row1) {
                table.addCell(addRow1).setFont(font);
            }

            List<String> row2 = shelfLifeTableDataObject.getRow2();
            for(String addRow2 : row2) {
                table.addCell(addRow2).setFont(font);
            }

            List<String> row3 = shelfLifeTableDataObject.getRow3();
            for(String addRow3 : row3) {
                table.addCell(addRow3).setFont(font);
            }

            List<String> row4 = shelfLifeTableDataObject.getRow4();
            for(String addRow4 : row4) {
                table.addCell(addRow4).setFont(font);
            }

            List<String> row5 = shelfLifeTableDataObject.getRow5();
            for(String addRow5 : row5) {
                table.addCell(addRow5).setFont(font);
            }

            List<String> row6 = shelfLifeTableDataObject.getRow6();
            for(String addRow6 : row6) {
                table.addCell(addRow6).setFont(font);
            }

            List<String> row7 = shelfLifeTableDataObject.getRow7();
            for(String addRow7 : row7) {
                table.addCell(addRow7).setFont(font);
            }

            List<String> row8 = shelfLifeTableDataObject.getRow8();
            for(String addRow8 : row8) {
                table.addCell(addRow8).setFont(font);
            }

            List<String> row9 = shelfLifeTableDataObject.getRow9();
            for(String addRow9 : row9) {
                table.addCell(addRow9).setFont(font);
            }

            List<String> row10 = shelfLifeTableDataObject.getRow10();
            for(String addRow10 : row10) {
                table.addCell(addRow10).setFont(font);
            }

            List<String> row11 = shelfLifeTableDataObject.getRow11();
            for(String addRow11 : row11) {
                table.addCell(addRow11).setFont(font);
            }

            List<String> row12 = shelfLifeTableDataObject.getRow12();
            for(String addRow12 : row12) {
                table.addCell(addRow12).setFont(font);
            }

            List<String> row13 = shelfLifeTableDataObject.getRow13();
            for(String addRow13 : row13) {
                table.addCell(addRow13).setFont(font);
            }

            List<String> row14 = shelfLifeTableDataObject.getRow14();
            for(String addRow14 : row14) {
                table.addCell(addRow14).setFont(font);
            }

            document.add(table_h);
            document.add(table);

            Paragraph space = new Paragraph("");
            document.add(space);
        }
        else {
            PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
            PdfFont bold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);

            float columnWidth_h[] = {600f};
            Table table_h = new Table(columnWidth_h);

            Text shelf_life = new Text("SHELF LIFE").setFont(bold);
            Paragraph shelf_life_data_header = new Paragraph()
                    .add(shelf_life);
            table_h.addCell(shelf_life_data_header);

            float columnWidth[] = {200f, 200f, 200f, 200f, 200f};
            Table table = new Table(columnWidth);

            // add cell
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Item")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("P/N")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Lot/Batch #")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("DOM")).setFont(font));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("DOE")).setFont(font));

            List<String> row1 = shelfLifeTableDataObject.getRow1();
            for(String addRow1 : row1) {
                table.addCell(addRow1).setFont(font);
            }

            List<String> row2 = shelfLifeTableDataObject.getRow2();
            for(String addRow2 : row2) {
                table.addCell(addRow2).setFont(font);
            }

            List<String> row3 = shelfLifeTableDataObject.getRow3();
            for(String addRow3 : row3) {
                table.addCell(addRow3).setFont(font);
            }

            List<String> row4 = shelfLifeTableDataObject.getRow4();
            for(String addRow4 : row4) {
                table.addCell(addRow4).setFont(font);
            }

            List<String> row5 = shelfLifeTableDataObject.getRow5();
            for(String addRow5 : row5) {
                table.addCell(addRow5).setFont(font);
            }

            List<String> row6 = shelfLifeTableDataObject.getRow6();
            for(String addRow6 : row6) {
                table.addCell(addRow6).setFont(font);
            }

            List<String> row7 = shelfLifeTableDataObject.getRow7();
            for(String addRow7 : row7) {
                table.addCell(addRow7).setFont(font);
            }

            List<String> row8 = shelfLifeTableDataObject.getRow8();
            for(String addRow8 : row8) {
                table.addCell(addRow8).setFont(font);
            }

            List<String> row9 = shelfLifeTableDataObject.getRow9();
            for(String addRow9 : row9) {
                table.addCell(addRow9).setFont(font);
            }

            List<String> row10 = shelfLifeTableDataObject.getRow10();
            for(String addRow10 : row10) {
                table.addCell(addRow10).setFont(font);
            }

            List<String> row11 = shelfLifeTableDataObject.getRow11();
            for(String addRow11 : row11) {
                table.addCell(addRow11).setFont(font);
            }

            List<String> row12 = shelfLifeTableDataObject.getRow12();
            for(String addRow12 : row12) {
                table.addCell(addRow12).setFont(font);
            }

            List<String> row13 = shelfLifeTableDataObject.getRow13();
            for(String addRow13 : row13) {
                table.addCell(addRow13).setFont(font);
            }

            List<String> row14 = shelfLifeTableDataObject.getRow14();
            for(String addRow14 : row14) {
                table.addCell(addRow14).setFont(font);
            }

            document.add(table_h);
            document.add(table);

            Paragraph space = new Paragraph("");
            document.add(space);
        }
    }
}
