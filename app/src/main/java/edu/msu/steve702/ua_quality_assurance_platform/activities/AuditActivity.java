package edu.msu.steve702.ua_quality_assurance_platform.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.InProcessObject;
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

        //For pre populating data
        Intent intent = getIntent();
        if (intent.getExtras() != null && intent.getExtras().containsKey("audit_id")) {
            audit_id = getIntent().getStringExtra("audit_id");
        }

        if (savedInstanceState != null) {
            // Restore the fragment's instance
            auditSpecFragment = (AuditSpecFragment) getSupportFragmentManager().getFragment(savedInstanceState, "auditSpecsFragment");
            inProcessFragment = (InProcessFragment) getSupportFragmentManager().getFragment(savedInstanceState, "inProcessFragment");
        }

//        auditSpecsSaveBtn = (Button) findViewById(R.id.saveAuditSpecs);
//        inProcessSaveBtn = (Button) findViewById(R.id.saveInProcess);
//
//        auditSpecsSaveBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                saveAuditSpecs();
//            }
//        });
//
//        inProcessSaveBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                saveInProcess();
//            }
//        });


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
        pageAdapter = new AuditPageAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), checklist_name);
        viewPager.setAdapter(pageAdapter);


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

        // save in firestore
        for (InProcessObject inProcess: inProcessList) {
            dbInProcessSheets.add(inProcess).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(AuditActivity.this, "In-Process Data Added", Toast.LENGTH_LONG).show();

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
