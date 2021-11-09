package edu.msu.steve702.ua_quality_assurance_platform.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Trace;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.List;

import edu.msu.steve702.ua_quality_assurance_platform.InProcessActivity;
import edu.msu.steve702.ua_quality_assurance_platform.InitialAuditActivity;
import edu.msu.steve702.ua_quality_assurance_platform.R;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.AuditObject;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.CalibrationTableDataObject;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.InProcessObject;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.ROMTableDataObject;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.ShelfLifeTableDataObject;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.TechnicalTableDataObject;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.TraceabilityTableDataObject;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.TrainingTableDataObject;
import edu.msu.steve702.ua_quality_assurance_platform.main_fragments.AuditPageAdapter;
import edu.msu.steve702.ua_quality_assurance_platform.main_fragments.AuditSpecFragment;
import edu.msu.steve702.ua_quality_assurance_platform.main_fragments.InProcessFragment;
import edu.msu.steve702.ua_quality_assurance_platform.main_fragments.TableDataFragment;

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

    private Button auditSpecsSaveBtn;
    private Button inProcessSaveBtn;

    public FirebaseFirestore db;

    private AuditSpecFragment auditSpecFragment;
    private InProcessFragment inProcessFragment;

    private String audit_id;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit);
        context = getApplicationContext();
        db = FirebaseFirestore.getInstance();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title);
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.auditTabLayout);
        tabAuditSpecs = findViewById(R.id.auditSpecsTabItem);
        tabChecklist = findViewById(R.id.checklistTabItem);
        tabInProcess = findViewById(R.id.inProcessTabItem);
        tabTableData = findViewById(R.id.tableDataTabItem);
        viewPager = findViewById(R.id.viewPager);

        //name of the checklist that was selected from the previous view
        checklist_name = getIntent().getStringExtra("checklistName");
        pageAdapter = new AuditPageAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), checklist_name, context);
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
        String option1 = getString(R.string.save);
        String option2 = getString(R.string.generate_pdf);
        String option3 = getString(R.string.take_photo);
        String option4 = getString(R.string.upload_photo);

        Toast.makeText(this, "Clicked on " + item.getTitle(), Toast.LENGTH_SHORT).show();
        if (option1.equals(item.getTitle().toString())) {
            saveAuditSpecs();
        } else if (option2.equals(item.getTitle().toString())) {

        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, "auditSpecsFragment", auditSpecFragment);
        getSupportFragmentManager().putFragment(outState, "inProcessFragment", inProcessFragment);
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

                    pageAdapter.getTableDataFragment().bundleObjects();
                    saveTechnicalDataTable();
                    saveROMTable();
                    saveCalibrationTable();
                    saveTrainingTable();
                    saveTraceabilityTable();
                    saveShelfLifeTable();
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

//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.saveAuditSpecs:
//                saveAuditSpecs(pageAdapter.getAuditSpecFragment().getView());
//                break;
//            case R.id.saveInProcess:
//                saveInProcess(pageAdapter.getInProcessFragment().getView());
//                break;
//        }
//    }
}
