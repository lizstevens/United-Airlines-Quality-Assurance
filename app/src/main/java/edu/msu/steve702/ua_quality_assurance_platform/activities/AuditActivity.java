package edu.msu.steve702.ua_quality_assurance_platform.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
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
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import edu.msu.steve702.ua_quality_assurance_platform.ExcelParser;
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
        //storageRef = firebaseStorage.getReference();
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
        viewPager = findViewById(R.id.viewPager);

        //name of the checklist that was selected from the previous view
        checklist_name = getIntent().getStringExtra("checklistName");
        if (getIntent().getExtras() != null & !getIntent().getExtras().containsKey("editing")) {
            try {
                ExcelParser parser = new ExcelParser(this);

                checklist = parser.readXLSXFile(checklist_name);
            } catch (IOException e) {
                Log.e("Failed to Parse Excel", "Error message: " + e.getMessage());
            }
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
                }
                return true;
            case R.id.option2:
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            //onCaptureImageResult(data);
        }
    }
//    private void takePhoto() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, 2);
//    }
//
//    private void onCaptureImageResult(Intent data){
//        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        thumbnail.compress(Bitmap.CompressFormat.JPEG,90,bytes);
//        byte bb[] = bytes.toByteArray();
//        uploadPhoto(bb);
//    }
//
//    private void uploadPhoto(byte[] bb) {
//
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setTitle("Uploading File....");
//        progressDialog.show();
//
//        final String randomKey = UUID.randomUUID().toString();
//        // Create a reference
//        StorageReference imageRef = storageRef.child("image/" + randomKey);
//
//        imageRef.putBytes(bb)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        Snackbar.make(findViewById(android.R.id.content),"Image Uploaded",Snackbar.LENGTH_LONG).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        progressDialog.dismiss();
//                        Toast.makeText(getApplicationContext(),"Failed Tp Upload", Toast.LENGTH_LONG).show();
//                    }
//                })
//                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                        double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
//                        progressDialog.setMessage("Progress: " + (int) progressPercent + "%");
//                    }
//                });
//
//    }

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

            Map<String, String> map = new HashMap<>();
            map.put("checklistName", checklist_name);
            dbChecklist.add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
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

                map = new HashMap<>();
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

    // this function allows user to create a pdf to store locally

    public void createChecklistPdf(ChecklistDataObject checklistDataObject) throws FileNotFoundException {
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
            image.setHeight(80);
            image.setWidth(500);

            Paragraph paragraph = new Paragraph("Technical Operations Quality Assurance");

            document.add(image);
            document.add(paragraph);

            Paragraph tech_data_header = new Paragraph("Checklist 8");
            document.add(tech_data_header);

            float columnWidth[] = {200f, 200f};
            Table table = new Table(columnWidth);

            for(int i = 1; i <= checklistDataObject.size(); i++) {
                Map<Integer, String[]> number = checklistDataObject.get(i);
                for (Map.Entry<Integer, String[]> entry : number.entrySet()) {
                    List<String> indiv = Arrays.asList(entry.getValue());
                    for(String answer : indiv) {
                        table.addCell(answer);
                    }
                }
            }

            document.add(table);

            Paragraph space = new Paragraph("");
            document.add(space);

            createInProcessPdf(pageAdapter.getInProcessFragment().getInProcessList(), document);
            createTechTablePdf(pageAdapter.getTableDataFragment().getTechnicalTableDataObject(), document);
            createROMTablePdf(pageAdapter.getTableDataFragment().getRomTableDataObject(), document);
//            createCalibrationPdf(pageAdapter.getTableDataFragment().getCalibrationTableDataObject(), document);
//            createTrainingPdf(pageAdapter.getTableDataFragment().getTrainingTableDataObject(), document);
//            createTraceabilityPdf(pageAdapter.getTableDataFragment().getTraceabilityTableDataObject(), document);
//            createShelfLifePdf(pageAdapter.getTableDataFragment().getShelfLifeTableDataObject(), document);

            document.close();
            Toast.makeText(getApplicationContext(), "PDF Created", Toast.LENGTH_LONG).show();
        }
//        else {
//            Paragraph tech_data_header = new Paragraph("Checklist 8");
//            document.add(tech_data_header);
//
//            float columnWidth[] = {200f, 200f};
//            Table table = new Table(columnWidth);
//
//            for(int i = 1; i <= checklistDataObject.size(); i++) {
//                Map<Integer, String[]> number = checklistDataObject.get(i);
//                for (Map.Entry<Integer, String[]> entry : number.entrySet()) {
//                    List<String> indiv = Arrays.asList(entry.getValue());
//                    for(String answer : indiv) {
//                        table.addCell(answer);
//                    }
//                }
//            }
//
//            document.add(table);
//
//            Paragraph space = new Paragraph("");
//            document.add(space);
//
//            createChecklistPdf(pageAdapter.getChecklistFragment().getChecklistDataObject(), document);
//            createTechTablePdf(pageAdapter.getTableDataFragment().getTechnicalTableDataObject(), document);
//            createROMTablePdf(pageAdapter.getTableDataFragment().getRomTableDataObject(), document);
//        }
    }

    public void createInProcessPdf(List<InProcessObject> inProcessList, Document document) throws FileNotFoundException {
//        List<InProcessObject> inProcessList = pageAdapter.getInProcessFragment().getInProcessList();
        if(pageAdapter.getInProcessFragment().getInProcessList() != null) {
//            String titleName = pageAdapter.getAuditSpecFragment().getAuditObject().getAuditTitleObj().replaceAll("[^a-zA-Z0-9]", "");
//            String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
//            File file = new File(pdfPath, titleName + PDF);
////            File file = new File(pdfPath,  "TestInProcess.pdf");
//
//            OutputStream outputStream = new FileOutputStream(file);
//
//            PdfWriter writer = new PdfWriter(file);
//            PdfDocument pdfDocument = new PdfDocument(writer);
//            Document document = new Document(pdfDocument);
//
//            Drawable drawable = getDrawable(R.drawable.united_airlines_quality_assurance_logo);
//            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//            byte[] bitmapData = stream.toByteArray();
//
//            ImageData imageData = ImageDataFactory.create(bitmapData);
//            Image image = new Image(imageData);
//            image.setHeight(80);
//            image.setWidth(500);
//
//            Paragraph paragraph = new Paragraph("Technical Operations Quality Assurance");
//
//            document.add(image);
//            document.add(paragraph);

            for (InProcessObject inProcessObject : inProcessList) {
                float columnWidth[] = {200f, 200f};
                Table table = new Table(columnWidth);

                // add cell
                table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Employee Name: ")));
                table.addCell(inProcessObject.getEmployeeNameObj());

                table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Part Number: ")));
                table.addCell(inProcessObject.getPartNumberObj());

                table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Serial Number: ")));
                table.addCell(inProcessObject.getSerialNumberObj());

                table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Nomeclature: ")));
                table.addCell(inProcessObject.getNomenclatureObj());

                table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Task: ")));
                table.addCell(inProcessObject.getTaskObj());

                table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Technical Specifications: ")));
                table.addCell(inProcessObject.getTechSpecificationsObj());

                table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Tooling: ")));
                table.addCell(inProcessObject.getToolingObj());

                table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Shelf Life: ")));
                table.addCell(inProcessObject.getShelfLifeObj());

                table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Traceability: ")));
                table.addCell(inProcessObject.getTraceObj());

                table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Required Training: ")));
                table.addCell(inProcessObject.getReqTrainingObj());

                table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Date Qualified: ")));
                table.addCell(inProcessObject.getTrainingDateObj());

                document.add(table);
//
//                Paragraph space = new Paragraph("");
//                document.add(space);
            }
//            createChecklistPdf(pageAdapter.getChecklistFragment().getChecklistDataObject(), document);
//            createTechTablePdf(pageAdapter.getTableDataFragment().getTechnicalTableDataObject(), document);
//            createROMTablePdf(pageAdapter.getTableDataFragment().getRomTableDataObject(), document);
//            createCalibrationPdf(pageAdapter.getTableDataFragment().getCalibrationTableDataObject(), document);
//            createTrainingPdf(pageAdapter.getTableDataFragment().getTrainingTableDataObject(), document);
//            createTraceabilityPdf(pageAdapter.getTableDataFragment().getTraceabilityTableDataObject(), document);
//            createShelfLifePdf(pageAdapter.getTableDataFragment().getShelfLifeTableDataObject(), document);

//            document.close();
//            Toast.makeText(getApplicationContext(), "PDF Created", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "PDF not created", Toast.LENGTH_LONG).show();
        }

    }

    public void createTechTablePdf(TechnicalTableDataObject techObject, Document document) throws FileNotFoundException {
        if(pageAdapter.getTableDataFragment().getTechnicalTableDataObject() != null) {
            Paragraph tech_data_header = new Paragraph("TECHNICAL DATA");
            document.add(tech_data_header);

            float columnWidth[] = {200f, 200f, 200f, 200f, 200f, 200f};
            Table table = new Table(columnWidth);

            // add cell
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Part Number/Aircraft/Eng Effectively Num")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Manufacturer")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("ATA/Document ID")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Rev. Level")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Rev. Date")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Comments")));


            List<String> row1 = techObject.getRow1();
            for(String addRow1 : row1) {
                table.addCell(addRow1);
            }

            List<String> row2 = techObject.getRow2();
            for(String addRow2 : row2) {
                table.addCell(addRow2);
            }

            List<String> row3 = techObject.getRow3();
            for(String addRow3 : row3) {
                table.addCell(addRow3);
            }

            List<String> row4 = techObject.getRow4();
            for(String addRow4 : row4) {
                table.addCell(addRow4);
            }

            List<String> row5 = techObject.getRow5();
            for(String addRow5 : row5) {
                table.addCell(addRow5);
            }

            List<String> row6 = techObject.getRow6();
            for(String addRow6 : row6) {
                table.addCell(addRow6);
            }

            List<String> row7 = techObject.getRow7();
            for(String addRow7 : row7) {
                table.addCell(addRow7);
            }

            List<String> row8 = techObject.getRow8();
            for(String addRow8 : row8) {
                table.addCell(addRow8);
            }

            List<String> row9 = techObject.getRow9();
            for(String addRow9 : row9) {
                table.addCell(addRow9);
            }

            List<String> row10 = techObject.getRow10();
            for(String addRow10 : row10) {
                table.addCell(addRow10);
            }

            List<String> row11 = techObject.getRow11();
            for(String addRow11 : row11) {
                table.addCell(addRow11);
            }

            List<String> row12 = techObject.getRow12();
            for(String addRow12 : row12) {
                table.addCell(addRow12);
            }

            List<String> row13 = techObject.getRow13();
            for(String addRow13 : row13) {
                table.addCell(addRow13);
            }

            List<String> row14 = techObject.getRow14();
            for(String addRow14 : row14) {
                table.addCell(addRow14);
            }

            document.add(table);
        }
        else {
            Paragraph tech_data_header = new Paragraph("TECHNICAL DATA");
            document.add(tech_data_header);

            float columnWidth[] = {200f, 200f, 200f, 200f, 200f, 200f};
            Table table = new Table(columnWidth);

            // add cell
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Part Number/Aircraft/Eng Effectively Num: ")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Manufacturer: ")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("ATA/Document ID: ")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Rev. Level: ")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Rev. Date: ")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Comments: ")));

            List<String> row1 = techObject.getRow1();
            for(String addRow1 : row1) {
                table.addCell(addRow1);
            }

            List<String> row2 = techObject.getRow2();
            for(String addRow2 : row2) {
                table.addCell(addRow2);
            }

            List<String> row3 = techObject.getRow3();
            for(String addRow3 : row3) {
                table.addCell(addRow3);
            }

            List<String> row4 = techObject.getRow4();
            for(String addRow4 : row4) {
                table.addCell(addRow4);
            }

            List<String> row5 = techObject.getRow5();
            for(String addRow5 : row5) {
                table.addCell(addRow5);
            }

            List<String> row6 = techObject.getRow6();
            for(String addRow6 : row6) {
                table.addCell(addRow6);
            }

            List<String> row7 = techObject.getRow7();
            for(String addRow7 : row7) {
                table.addCell(addRow7);
            }

            List<String> row8 = techObject.getRow8();
            for(String addRow8 : row8) {
                table.addCell(addRow8);
            }

            List<String> row9 = techObject.getRow9();
            for(String addRow9 : row9) {
                table.addCell(addRow9);
            }

            List<String> row10 = techObject.getRow10();
            for(String addRow10 : row10) {
                table.addCell(addRow10);
            }

            List<String> row11 = techObject.getRow11();
            for(String addRow11 : row11) {
                table.addCell(addRow11);
            }

            List<String> row12 = techObject.getRow12();
            for(String addRow12 : row12) {
                table.addCell(addRow12);
            }

            List<String> row13 = techObject.getRow13();
            for(String addRow13 : row13) {
                table.addCell(addRow13);
            }

            List<String> row14 = techObject.getRow14();
            for(String addRow14 : row14) {
                table.addCell(addRow14);
            }

            document.add(table);
        }
    }

    public void createROMTablePdf(ROMTableDataObject romTableDataObject, Document document) throws FileNotFoundException {
        if(pageAdapter.getTableDataFragment().getRomTableDataObject() != null) {
            Paragraph rom_data_header = new Paragraph("RECORDS of MAINTENANCE (ROM)");
            document.add(rom_data_header);

            float columnWidth[] = {200f, 200f, 200f, 200f, 200f, 200f};
            Table table = new Table(columnWidth);

            // add cell
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Doc Type")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Document #")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Part/PN/SN")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Date")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Specification incl. revision level & date")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Tech. Name")));

            List<String> row1 = romTableDataObject.getRow1();
            for(String addRow1 : row1) {
                table.addCell(addRow1);
            }

            List<String> row2 = romTableDataObject.getRow2();
            for(String addRow2 : row2) {
                table.addCell(addRow2);
            }

            List<String> row3 = romTableDataObject.getRow3();
            for(String addRow3 : row3) {
                table.addCell(addRow3);
            }

            List<String> row4 = romTableDataObject.getRow4();
            for(String addRow4 : row4) {
                table.addCell(addRow4);
            }

            List<String> row5 = romTableDataObject.getRow5();
            for(String addRow5 : row5) {
                table.addCell(addRow5);
            }

            List<String> row6 = romTableDataObject.getRow6();
            for(String addRow6 : row6) {
                table.addCell(addRow6);
            }

            List<String> row7 = romTableDataObject.getRow7();
            for(String addRow7 : row7) {
                table.addCell(addRow7);
            }

            List<String> row8 = romTableDataObject.getRow8();
            for(String addRow8 : row8) {
                table.addCell(addRow8);
            }

            List<String> row9 = romTableDataObject.getRow9();
            for(String addRow9 : row9) {
                table.addCell(addRow9);
            }

            List<String> row10 = romTableDataObject.getRow10();
            for(String addRow10 : row10) {
                table.addCell(addRow10);
            }

            document.add(table);
        }
        else {
            Paragraph tech_data_header = new Paragraph("Technical Data");
            document.add(tech_data_header);

            float columnWidth[] = {200f, 200f, 200f, 200f, 200f, 200f};
            Table table = new Table(columnWidth);

            // add cell
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Doc Type")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Document #")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Part/PN/SN")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Date")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Specification incl. revision level & date")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Tech. Name")));

            List<String> row1 = romTableDataObject.getRow1();
            for(String addRow1 : row1) {
                table.addCell(addRow1);
            }

            List<String> row2 = romTableDataObject.getRow2();
            for(String addRow2 : row2) {
                table.addCell(addRow2);
            }

            List<String> row3 = romTableDataObject.getRow3();
            for(String addRow3 : row3) {
                table.addCell(addRow3);
            }

            List<String> row4 = romTableDataObject.getRow4();
            for(String addRow4 : row4) {
                table.addCell(addRow4);
            }

            List<String> row5 = romTableDataObject.getRow5();
            for(String addRow5 : row5) {
                table.addCell(addRow5);
            }

            List<String> row6 = romTableDataObject.getRow6();
            for(String addRow6 : row6) {
                table.addCell(addRow6);
            }

            List<String> row7 = romTableDataObject.getRow7();
            for(String addRow7 : row7) {
                table.addCell(addRow7);
            }

            List<String> row8 = romTableDataObject.getRow8();
            for(String addRow8 : row8) {
                table.addCell(addRow8);
            }

            List<String> row9 = romTableDataObject.getRow9();
            for(String addRow9 : row9) {
                table.addCell(addRow9);
            }

            List<String> row10 = romTableDataObject.getRow10();
            for(String addRow10 : row10) {
                table.addCell(addRow10);
            }

            document.add(table);
        }
    }

    public void createCalibrationPdf(CalibrationTableDataObject calibrationTableDataObject, Document document) throws FileNotFoundException {
        if(pageAdapter.getTableDataFragment().getCalibrationTableDataObject() != null) {
            Paragraph calib_data_header = new Paragraph("CALIBRATION");
            document.add(calib_data_header);

            float columnWidth[] = {200f, 200f, 200f, 200f, 200f};
            Table table = new Table(columnWidth);

            // add cell
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Item")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("ID #")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("CAL Date")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("CAL Due")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("CAL by")));

            List<String> row1 = calibrationTableDataObject.getRow1();
            for(String addRow1 : row1) {
                table.addCell(addRow1);
            }

            List<String> row2 = calibrationTableDataObject.getRow2();
            for(String addRow2 : row2) {
                table.addCell(addRow2);
            }

            List<String> row3 = calibrationTableDataObject.getRow3();
            for(String addRow3 : row3) {
                table.addCell(addRow3);
            }

            List<String> row4 = calibrationTableDataObject.getRow4();
            for(String addRow4 : row4) {
                table.addCell(addRow4);
            }

            List<String> row5 = calibrationTableDataObject.getRow5();
            for(String addRow5 : row5) {
                table.addCell(addRow5);
            }

            List<String> row6 = calibrationTableDataObject.getRow6();
            for(String addRow6 : row6) {
                table.addCell(addRow6);
            }

            List<String> row7 = calibrationTableDataObject.getRow7();
            for(String addRow7 : row7) {
                table.addCell(addRow7);
            }

            List<String> row8 = calibrationTableDataObject.getRow8();
            for(String addRow8 : row8) {
                table.addCell(addRow8);
            }

            List<String> row9 = calibrationTableDataObject.getRow9();
            for(String addRow9 : row9) {
                table.addCell(addRow9);
            }

            List<String> row10 = calibrationTableDataObject.getRow10();
            for(String addRow10 : row10) {
                table.addCell(addRow10);
            }

            List<String> row11 = calibrationTableDataObject.getRow11();
            for(String addRow11 : row11) {
                table.addCell(addRow11);
            }

            List<String> row12 = calibrationTableDataObject.getRow12();
            for(String addRow12 : row12) {
                table.addCell(addRow12);
            }

            List<String> row13 = calibrationTableDataObject.getRow13();
            for(String addRow13 : row13) {
                table.addCell(addRow13);
            }

            List<String> row14 = calibrationTableDataObject.getRow14();
            for(String addRow14 : row14) {
                table.addCell(addRow14);
            }

            List<String> row15 = calibrationTableDataObject.getRow15();
            for(String addRow15 : row15) {
                table.addCell(addRow15);
            }

            document.add(table);
        }
        else {
            Paragraph calib_data_header = new Paragraph("CALIBRATION");
            document.add(calib_data_header);

            float columnWidth[] = {200f, 200f, 200f, 200f, 200f};
            Table table = new Table(columnWidth);

            // add cell
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Item")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("ID #")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("CAL Date")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("CAL Due")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("CAL by")));

            List<String> row1 = calibrationTableDataObject.getRow1();
            for(String addRow1 : row1) {
                table.addCell(addRow1);
            }

            List<String> row2 = calibrationTableDataObject.getRow2();
            for(String addRow2 : row2) {
                table.addCell(addRow2);
            }

            List<String> row3 = calibrationTableDataObject.getRow3();
            for(String addRow3 : row3) {
                table.addCell(addRow3);
            }

            List<String> row4 = calibrationTableDataObject.getRow4();
            for(String addRow4 : row4) {
                table.addCell(addRow4);
            }

            List<String> row5 = calibrationTableDataObject.getRow5();
            for(String addRow5 : row5) {
                table.addCell(addRow5);
            }

            List<String> row6 = calibrationTableDataObject.getRow6();
            for(String addRow6 : row6) {
                table.addCell(addRow6);
            }

            List<String> row7 = calibrationTableDataObject.getRow7();
            for(String addRow7 : row7) {
                table.addCell(addRow7);
            }

            List<String> row8 = calibrationTableDataObject.getRow8();
            for(String addRow8 : row8) {
                table.addCell(addRow8);
            }

            List<String> row9 = calibrationTableDataObject.getRow9();
            for(String addRow9 : row9) {
                table.addCell(addRow9);
            }

            List<String> row10 = calibrationTableDataObject.getRow10();
            for(String addRow10 : row10) {
                table.addCell(addRow10);
            }

            List<String> row11 = calibrationTableDataObject.getRow11();
            for(String addRow11 : row11) {
                table.addCell(addRow11);
            }

            List<String> row12 = calibrationTableDataObject.getRow12();
            for(String addRow12 : row12) {
                table.addCell(addRow12);
            }

            List<String> row13 = calibrationTableDataObject.getRow13();
            for(String addRow13 : row13) {
                table.addCell(addRow13);
            }

            List<String> row14 = calibrationTableDataObject.getRow14();
            for(String addRow14 : row14) {
                table.addCell(addRow14);
            }

            List<String> row15 = calibrationTableDataObject.getRow15();
            for(String addRow15 : row15) {
                table.addCell(addRow15);
            }

            document.add(table);
        }
    }

    public void createTrainingPdf(TrainingTableDataObject trainingTableDataObject, Document document) throws FileNotFoundException {
        if(pageAdapter.getTableDataFragment().getTrainingTableDataObject() != null) {
            Paragraph training_data_header = new Paragraph("TRAINING (Non-Interviewed Personnel)");
            document.add(training_data_header);

            float columnWidth[] = {200f, 200f, 200f, 200f};
            Table table = new Table(columnWidth);

            // add cell
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Technician Name/ID")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Part Number/Maintenance Performed")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Required Training")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("OJT/Qualified Date")));

            List<String> row1 = trainingTableDataObject.getRow1();
            for(String addRow1 : row1) {
                table.addCell(addRow1);
            }

            List<String> row2 = trainingTableDataObject.getRow2();
            for(String addRow2 : row2) {
                table.addCell(addRow2);
            }

            List<String> row3 = trainingTableDataObject.getRow3();
            for(String addRow3 : row3) {
                table.addCell(addRow3);
            }

            List<String> row4 = trainingTableDataObject.getRow4();
            for(String addRow4 : row4) {
                table.addCell(addRow4);
            }

            List<String> row5 = trainingTableDataObject.getRow5();
            for(String addRow5 : row5) {
                table.addCell(addRow5);
            }

            List<String> row6 = trainingTableDataObject.getRow6();
            for(String addRow6 : row6) {
                table.addCell(addRow6);
            }

            List<String> row7 = trainingTableDataObject.getRow7();
            for(String addRow7 : row7) {
                table.addCell(addRow7);
            }

            List<String> row8 = trainingTableDataObject.getRow8();
            for(String addRow8 : row8) {
                table.addCell(addRow8);
            }

            List<String> row9 = trainingTableDataObject.getRow9();
            for(String addRow9 : row9) {
                table.addCell(addRow9);
            }

            List<String> row10 = trainingTableDataObject.getRow10();
            for(String addRow10 : row10) {
                table.addCell(addRow10);
            }

            document.add(table);
        }
        else {
            Paragraph training_data_header = new Paragraph("TRAINING (Non-Interviewed Personnel)");
            document.add(training_data_header);

            float columnWidth[] = {200f, 200f, 200f, 200f};
            Table table = new Table(columnWidth);

            // add cell
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Technician Name/ID")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Part Number/Maintenance Performed")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Required Training")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("OJT/Qualified Date")));

            List<String> row1 = trainingTableDataObject.getRow1();
            for(String addRow1 : row1) {
                table.addCell(addRow1);
            }

            List<String> row2 = trainingTableDataObject.getRow2();
            for(String addRow2 : row2) {
                table.addCell(addRow2);
            }

            List<String> row3 = trainingTableDataObject.getRow3();
            for(String addRow3 : row3) {
                table.addCell(addRow3);
            }

            List<String> row4 = trainingTableDataObject.getRow4();
            for(String addRow4 : row4) {
                table.addCell(addRow4);
            }

            List<String> row5 = trainingTableDataObject.getRow5();
            for(String addRow5 : row5) {
                table.addCell(addRow5);
            }

            List<String> row6 = trainingTableDataObject.getRow6();
            for(String addRow6 : row6) {
                table.addCell(addRow6);
            }

            List<String> row7 = trainingTableDataObject.getRow7();
            for(String addRow7 : row7) {
                table.addCell(addRow7);
            }

            List<String> row8 = trainingTableDataObject.getRow8();
            for(String addRow8 : row8) {
                table.addCell(addRow8);
            }

            List<String> row9 = trainingTableDataObject.getRow9();
            for(String addRow9 : row9) {
                table.addCell(addRow9);
            }

            List<String> row10 = trainingTableDataObject.getRow10();
            for(String addRow10 : row10) {
                table.addCell(addRow10);
            }

            document.add(table);
        }
    }

    public void createTraceabilityPdf(TraceabilityTableDataObject traceabilityTableDataObject, Document document) throws FileNotFoundException {
        if(pageAdapter.getTableDataFragment().getTraceabilityTableDataObject() != null) {
            Paragraph trace_data_header = new Paragraph("TRACEABILITY");
            document.add(trace_data_header);

            float columnWidth[] = {200f, 200f, 200f, 200f, 200f};
            Table table = new Table(columnWidth);

            // add cell
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Product")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Part Number")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Batch/Lot #")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("P.O Number")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Supplier")));

            List<String> row1 = traceabilityTableDataObject.getRow1();
            for(String addRow1 : row1) {
                table.addCell(addRow1);
            }

            List<String> row2 = traceabilityTableDataObject.getRow2();
            for(String addRow2 : row2) {
                table.addCell(addRow2);
            }

            List<String> row3 = traceabilityTableDataObject.getRow3();
            for(String addRow3 : row3) {
                table.addCell(addRow3);
            }

            List<String> row4 = traceabilityTableDataObject.getRow4();
            for(String addRow4 : row4) {
                table.addCell(addRow4);
            }

            List<String> row5 = traceabilityTableDataObject.getRow5();
            for(String addRow5 : row5) {
                table.addCell(addRow5);
            }

            List<String> row6 = traceabilityTableDataObject.getRow6();
            for(String addRow6 : row6) {
                table.addCell(addRow6);
            }

            List<String> row7 = traceabilityTableDataObject.getRow7();
            for(String addRow7 : row7) {
                table.addCell(addRow7);
            }

            List<String> row8 = traceabilityTableDataObject.getRow8();
            for(String addRow8 : row8) {
                table.addCell(addRow8);
            }

            List<String> row9 = traceabilityTableDataObject.getRow9();
            for(String addRow9 : row9) {
                table.addCell(addRow9);
            }

            List<String> row10 = traceabilityTableDataObject.getRow10();
            for(String addRow10 : row10) {
                table.addCell(addRow10);
            }

            document.add(table);
        }
        else {
            Paragraph trace_data_header = new Paragraph("TRACEABILITY");
            document.add(trace_data_header);

            float columnWidth[] = {200f, 200f, 200f, 200f, 200f};
            Table table = new Table(columnWidth);

            // add cell
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Product")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Part Number")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Batch/Lot #")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("P.O Number")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Supplier")));

            List<String> row1 = traceabilityTableDataObject.getRow1();
            for(String addRow1 : row1) {
                table.addCell(addRow1);
            }

            List<String> row2 = traceabilityTableDataObject.getRow2();
            for(String addRow2 : row2) {
                table.addCell(addRow2);
            }

            List<String> row3 = traceabilityTableDataObject.getRow3();
            for(String addRow3 : row3) {
                table.addCell(addRow3);
            }

            List<String> row4 = traceabilityTableDataObject.getRow4();
            for(String addRow4 : row4) {
                table.addCell(addRow4);
            }

            List<String> row5 = traceabilityTableDataObject.getRow5();
            for(String addRow5 : row5) {
                table.addCell(addRow5);
            }

            List<String> row6 = traceabilityTableDataObject.getRow6();
            for(String addRow6 : row6) {
                table.addCell(addRow6);
            }

            List<String> row7 = traceabilityTableDataObject.getRow7();
            for(String addRow7 : row7) {
                table.addCell(addRow7);
            }

            List<String> row8 = traceabilityTableDataObject.getRow8();
            for(String addRow8 : row8) {
                table.addCell(addRow8);
            }

            List<String> row9 = traceabilityTableDataObject.getRow9();
            for(String addRow9 : row9) {
                table.addCell(addRow9);
            }

            List<String> row10 = traceabilityTableDataObject.getRow10();
            for(String addRow10 : row10) {
                table.addCell(addRow10);
            }

            document.add(table);
        }
    }

    public void createShelfLifePdf(ShelfLifeTableDataObject shelfLifeTableDataObject, Document document) throws FileNotFoundException {
        if(pageAdapter.getTableDataFragment().getShelfLifeTableDataObject() != null) {
            Paragraph shelf_life_data_header = new Paragraph("SHELF LIFE");
            document.add(shelf_life_data_header);

            float columnWidth[] = {200f, 200f, 200f, 200f, 200f};
            Table table = new Table(columnWidth);

            // add cell
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Item")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("P/N")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Lot/Batch #")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("DOM")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("DOE")));

            List<String> row1 = shelfLifeTableDataObject.getRow1();
            for(String addRow1 : row1) {
                table.addCell(addRow1);
            }

            List<String> row2 = shelfLifeTableDataObject.getRow2();
            for(String addRow2 : row2) {
                table.addCell(addRow2);
            }

            List<String> row3 = shelfLifeTableDataObject.getRow3();
            for(String addRow3 : row3) {
                table.addCell(addRow3);
            }

            List<String> row4 = shelfLifeTableDataObject.getRow4();
            for(String addRow4 : row4) {
                table.addCell(addRow4);
            }

            List<String> row5 = shelfLifeTableDataObject.getRow5();
            for(String addRow5 : row5) {
                table.addCell(addRow5);
            }

            List<String> row6 = shelfLifeTableDataObject.getRow6();
            for(String addRow6 : row6) {
                table.addCell(addRow6);
            }

            List<String> row7 = shelfLifeTableDataObject.getRow7();
            for(String addRow7 : row7) {
                table.addCell(addRow7);
            }

            List<String> row8 = shelfLifeTableDataObject.getRow8();
            for(String addRow8 : row8) {
                table.addCell(addRow8);
            }

            List<String> row9 = shelfLifeTableDataObject.getRow9();
            for(String addRow9 : row9) {
                table.addCell(addRow9);
            }

            List<String> row10 = shelfLifeTableDataObject.getRow10();
            for(String addRow10 : row10) {
                table.addCell(addRow10);
            }

            List<String> row11 = shelfLifeTableDataObject.getRow11();
            for(String addRow11 : row11) {
                table.addCell(addRow11);
            }

            List<String> row12 = shelfLifeTableDataObject.getRow12();
            for(String addRow12 : row12) {
                table.addCell(addRow12);
            }

            List<String> row13 = shelfLifeTableDataObject.getRow13();
            for(String addRow13 : row13) {
                table.addCell(addRow13);
            }

            List<String> row14 = shelfLifeTableDataObject.getRow14();
            for(String addRow14 : row14) {
                table.addCell(addRow14);
            }

            document.add(table);
        }
        else {
            Paragraph shelf_life_data_header = new Paragraph("SHELF LIFE");
            document.add(shelf_life_data_header);

            float columnWidth[] = {200f, 200f, 200f, 200f, 200f};
            Table table = new Table(columnWidth);

            // add cell
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Item")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("P/N")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Lot/Batch #")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("DOM")));
            table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("DOE")));

            List<String> row1 = shelfLifeTableDataObject.getRow1();
            for(String addRow1 : row1) {
                table.addCell(addRow1);
            }

            List<String> row2 = shelfLifeTableDataObject.getRow2();
            for(String addRow2 : row2) {
                table.addCell(addRow2);
            }

            List<String> row3 = shelfLifeTableDataObject.getRow3();
            for(String addRow3 : row3) {
                table.addCell(addRow3);
            }

            List<String> row4 = shelfLifeTableDataObject.getRow4();
            for(String addRow4 : row4) {
                table.addCell(addRow4);
            }

            List<String> row5 = shelfLifeTableDataObject.getRow5();
            for(String addRow5 : row5) {
                table.addCell(addRow5);
            }

            List<String> row6 = shelfLifeTableDataObject.getRow6();
            for(String addRow6 : row6) {
                table.addCell(addRow6);
            }

            List<String> row7 = shelfLifeTableDataObject.getRow7();
            for(String addRow7 : row7) {
                table.addCell(addRow7);
            }

            List<String> row8 = shelfLifeTableDataObject.getRow8();
            for(String addRow8 : row8) {
                table.addCell(addRow8);
            }

            List<String> row9 = shelfLifeTableDataObject.getRow9();
            for(String addRow9 : row9) {
                table.addCell(addRow9);
            }

            List<String> row10 = shelfLifeTableDataObject.getRow10();
            for(String addRow10 : row10) {
                table.addCell(addRow10);
            }

            List<String> row11 = shelfLifeTableDataObject.getRow11();
            for(String addRow11 : row11) {
                table.addCell(addRow11);
            }

            List<String> row12 = shelfLifeTableDataObject.getRow12();
            for(String addRow12 : row12) {
                table.addCell(addRow12);
            }

            List<String> row13 = shelfLifeTableDataObject.getRow13();
            for(String addRow13 : row13) {
                table.addCell(addRow13);
            }

            List<String> row14 = shelfLifeTableDataObject.getRow14();
            for(String addRow14 : row14) {
                table.addCell(addRow14);
            }

            document.add(table);
        }
    }
}
