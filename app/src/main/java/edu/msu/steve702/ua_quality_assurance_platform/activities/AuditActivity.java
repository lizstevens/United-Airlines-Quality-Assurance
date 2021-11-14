package edu.msu.steve702.ua_quality_assurance_platform.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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

    public FirebaseFirestore db;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageRef;
    private AuditSpecFragment auditSpecFragment;
    private InProcessFragment inProcessFragment;

    private String audit_id;
    private Context context;

    private ChecklistDataObject checklist;

    private EditText titleEdit, employeeNameEdit, partNumberEdit , serialNumberEdit ,nomenclatureEdit ,taskEdit;
    private EditText techSpecificationsEdit , toolingEdit , shelfLifeEdit, traceEdit, reqTrainingEdit, trainingDateEdit;


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

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_audit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, "Clicked on " + item.getTitle(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()){
            //generate pdf
            case R.id.option1:
                //saveAuditSpecs();
                return true;
            //upload photo
            case R.id.option2:
                takePhoto();
                return true;
            //take photo
            case R.id.option3:

                return true;
            //return home
            case R.id.option4:
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
            onCaptureImageResult(data);
        }
    }
    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 2);
    }

    private void onCaptureImageResult(Intent data){
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG,90,bytes);
        byte bb[] = bytes.toByteArray();
        uploadPhoto(bb);
    }

    private void uploadPhoto(byte[] bb) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading File....");
        progressDialog.show();

        final String randomKey = UUID.randomUUID().toString();
        // Create a reference
        StorageReference imageRef = storageRef.child("image/" + randomKey);

        imageRef.putBytes(bb)
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
            dbAuditSpecs.document(audit_id).set(auditObject, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void avoid) {
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

                        //rebundle table data
                        pageAdapter.getTableDataFragment().bundleObjects();

                        dbTechnicalTable.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                    for (DocumentSnapshot doc: task.getResult()) {
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

                                    for (DocumentSnapshot doc: task.getResult()) {
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

                                    for (DocumentSnapshot doc: task.getResult()) {
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

                                    for (DocumentSnapshot doc: task.getResult()) {
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

                                    for (DocumentSnapshot doc: task.getResult()) {
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

                                    for (DocumentSnapshot doc: task.getResult()) {
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

                                    for (DocumentSnapshot doc: task.getResult()) {
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
                map.put("Section " + i, json_str);

                // save in firestore
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


            }




        }
    }

    // this function allows user to create a pdf to store locally
    public void createPdf(List<InProcessObject> inProcessList) throws FileNotFoundException {
//        InProcessObject test = inProcessList.get(0);
        // edit text name
        employeeNameEdit = findViewById(R.id.empNameText);
        // edit text part number
        partNumberEdit = findViewById(R.id.partNumText);
        // edit text serial number
        serialNumberEdit = findViewById(R.id.serialNumText);
        // edit text nomenclature
        nomenclatureEdit = findViewById(R.id.nomenText);
        // edit text task
        taskEdit = findViewById(R.id.taskText);
        // edit text techSpecifications
        techSpecificationsEdit = findViewById(R.id.techSpecText);
        // edit text tooling
        toolingEdit = findViewById(R.id.toolingText);
        // edit text shelfLife
        shelfLifeEdit = findViewById(R.id.shelfLifeText);
        // edit text traceability
        traceEdit = findViewById(R.id.traceText);
        // edit text reqTraining
        reqTrainingEdit = findViewById(R.id.reqTrainingText);
        // edit text trainingDate
        trainingDateEdit = findViewById(R.id.dateText);

        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath, titleEdit.getText() + ".pdf");

        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        Drawable drawable = getDrawable(R.drawable.united_airlines_quality_assurance_logo);
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitmapData = stream.toByteArray();

        ImageData imageData = ImageDataFactory.create(bitmapData);
        Image image = new Image(imageData);
        image.setHeight(80);
        image.setWidth(500);

        Paragraph paragraph = new Paragraph("Technical Operations Quality Assurance");

        float columnWidth[] = {200f, 200f};
        Table table = new Table(columnWidth);

        // add cell
        table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Employee Name: ")));
        table.addCell(employeeNameEdit.getText().toString());

        table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Part Number: ")));
        table.addCell(partNumberEdit.getText().toString());

        table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Serial Number: ")));
        table.addCell(serialNumberEdit.getText().toString());

        table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Nomeclature: ")));
        table.addCell(nomenclatureEdit.getText().toString());

        table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Task: ")));
        table.addCell(taskEdit.getText().toString());

        table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Technical Specifications: ")));
        table.addCell(techSpecificationsEdit.getText().toString());

        table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Tooling: ")));
        table.addCell(toolingEdit.getText().toString());

        table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Shelf Life: ")));
        table.addCell(shelfLifeEdit.getText().toString());

        table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Traceability: ")));
        table.addCell(traceEdit.getText().toString());

        table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Required Training: ")));
        table.addCell(reqTrainingEdit.getText().toString());

        table.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY).add(new Paragraph("Date Qualified: ")));
        table.addCell(trainingDateEdit.getText().toString());


        document.add(image);
        document.add(paragraph);
        document.add(table);

        document.close();
        Toast.makeText(getApplicationContext(), "PDF Created", Toast.LENGTH_LONG).show();
    }
}
