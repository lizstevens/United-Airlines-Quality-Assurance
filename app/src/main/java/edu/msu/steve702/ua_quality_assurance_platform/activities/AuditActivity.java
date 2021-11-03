package edu.msu.steve702.ua_quality_assurance_platform.activities;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import edu.msu.steve702.ua_quality_assurance_platform.InProcessActivity;
import edu.msu.steve702.ua_quality_assurance_platform.InitialAuditActivity;
import edu.msu.steve702.ua_quality_assurance_platform.R;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.AuditObject;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.InProcessObject;
import edu.msu.steve702.ua_quality_assurance_platform.main_fragments.AuditPageAdapter;
import edu.msu.steve702.ua_quality_assurance_platform.main_fragments.AuditSpecFragment;
import edu.msu.steve702.ua_quality_assurance_platform.main_fragments.InProcessFragment;

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

    private AuditObject auditObject;
    private InProcessObject inProcessObject;

    private String auditSpecDoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit);

        if (savedInstanceState != null) {
            // Restore the fragment's instance
            auditSpecFragment = (AuditSpecFragment) getSupportFragmentManager().getFragment(savedInstanceState, "auditSpecsFragment");
            inProcessFragment = (InProcessFragment) getSupportFragmentManager().getFragment(savedInstanceState, "inProcessFragment");
        }

        auditSpecsSaveBtn = (Button) findViewById(R.id.saveAuditSpecs);
        inProcessSaveBtn = (Button) findViewById(R.id.saveInProcess);

        auditSpecsSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAuditSpecs(pageAdapter.getAuditSpecFragment().getView());
            }
        });

        inProcessSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveInProcess(pageAdapter.getInProcessFragment().getView());
            }
        });


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
//            saveAuditSpecs(pageAdapter.getAuditSpecFragment().getView());
//            saveInProcess(pageAdapter.getInProcessFragment().getView());

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



    private void saveAuditSpecs(View view) {
        EditText auditName = view.findViewById(R.id.nameEdit);
        EditText auditDate = view.findViewById(R.id.dateEdit);
        EditText location = view.findViewById(R.id.locationEdit);
        EditText auditTitle = view.findViewById(R.id.auditTitleEdit);
        EditText auditNumber = view.findViewById(R.id.auditNumberEdit);
        EditText vendorName = view.findViewById(R.id.vendorNameEdit);
        EditText vendorNum = view.findViewById(R.id.vendorNumEdit);
        EditText auditDescrip = view.findViewById(R.id.descripEdit);

        String auditNameObj = auditName.getText().toString();
        String auditDateObj = auditDate.getText().toString();
        String locationObj = location.getText().toString();
        String auditTitleObj = auditTitle.getText().toString();
        String auditNumberObj = auditNumber.getText().toString();
        String vendorNameObj = vendorName.getText().toString();
        String vendorNumObj = vendorNum.getText().toString();
        String auditDescripObj = auditDescrip.getText().toString();

        CollectionReference dbAuditSpecs = db.collection("Audit");

        AuditObject auditObject = new AuditObject(
                auditNameObj,
                auditDateObj,
                locationObj,
                auditTitleObj,
                auditNumberObj,
                vendorNameObj,
                vendorNumObj,
                auditDescripObj
        );

        // save in firestore
        dbAuditSpecs.add(auditObject).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                auditSpecDoc = documentReference.getId();
                Toast.makeText(AuditActivity.this, "Audit Information Added", Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AuditActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }


    private void saveInProcess(View view) {
        // edit text name
        EditText employeeNameEdit = view.findViewById(R.id.empNameText);
        // edit text part number
        EditText partNumberEdit = view.findViewById(R.id.partNumText);
        // edit text serial number
        EditText serialNumberEdit = view.findViewById(R.id.serialNumText);
        // edit text nomenclature
        EditText nomenclatureEdit = view.findViewById(R.id.nomenText);
        // edit text task
        EditText taskEdit = view.findViewById(R.id.taskText);
        // edit text techSpecifications
        EditText techSpecificationsEdit = view.findViewById(R.id.techSpecText);
        // edit text tooling
        EditText toolingEdit = view.findViewById(R.id.toolingText);
        // edit text shelfLife
        EditText shelfLifeEdit = view.findViewById(R.id.shelfLifeText);
        // edit text traceability
        EditText traceEdit = view.findViewById(R.id.traceText);
        // edit text reqTraining
        EditText reqTrainingEdit = view.findViewById(R.id.reqTrainingText);
        // edit text trainingDate
        EditText trainingDateEdit = view.findViewById(R.id.dateText);

        String employeeNameObj = employeeNameEdit.getText().toString();
        String partNumberObj = partNumberEdit.getText().toString();
        String serialNumberObj = serialNumberEdit.getText().toString();
        String nomenclatureObj = nomenclatureEdit.getText().toString();
        String taskObj = taskEdit.getText().toString();
        String techSpecificationsObj = techSpecificationsEdit.getText().toString();
        String toolingObj = toolingEdit.getText().toString();
        String shelfLifeObj = shelfLifeEdit.getText().toString();
        String traceObj = traceEdit.getText().toString();
        String reqTrainingObj = reqTrainingEdit.getText().toString();
        String trainingDateObj = trainingDateEdit.getText().toString();


//        String inProcessRef = db.collection("Audit").document().getId();
        CollectionReference dbInProcessSheets = db.collection("Audit").document(auditSpecDoc).collection("in-process");


        InProcessObject inProcess = new InProcessObject(
                employeeNameObj,
                partNumberObj,
                serialNumberObj,
                nomenclatureObj,
                taskObj,
                techSpecificationsObj,
                toolingObj,
                shelfLifeObj,
                traceObj,
                reqTrainingObj,
                trainingDateObj
        );

        // save in firestore
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
