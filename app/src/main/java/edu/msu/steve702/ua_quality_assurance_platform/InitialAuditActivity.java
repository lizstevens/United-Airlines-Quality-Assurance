package edu.msu.steve702.ua_quality_assurance_platform;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import edu.msu.steve702.ua_quality_assurance_platform.data_objects.AuditObject;

public class InitialAuditActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText auditName, auditDate, vendorName, vendorNum, auditDescrip;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_audit);

        db = FirebaseFirestore.getInstance();

        auditName = findViewById(R.id.nameEdit);
        auditDate = findViewById(R.id.dateEdit);
        vendorName = findViewById(R.id.vendorNameEdit);
        vendorNum = findViewById(R.id.vendorNumEdit);
        auditDescrip = findViewById(R.id.descripEdit);


        findViewById(R.id.togglebutton).setOnClickListener(this);
        //findViewById(R.id.start_in_process).setOnClickListener(this);
    }

    private void saveAuditInfo (View view) {
        String auditNameObj = auditName.getText().toString();
        String auditDateObj = auditDate.getText().toString();
        String vendorNameObj = vendorName.getText().toString();
        String vendorNumObj = vendorNum.getText().toString();
        String auditDescripObj = auditDescrip.getText().toString();

        AuditObject auditObject = new AuditObject(auditNameObj,  auditDateObj, vendorNameObj, vendorNumObj, auditDescripObj);

        db.collection("Audit").document().set(auditObject)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(InitialAuditActivity.this, "Audit Information Added", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(InitialAuditActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });

    }


    public void onToggleClicked(View view) {
        // Is the toggle on?
        boolean on = ((ToggleButton) view).isChecked();

        if (on) {
            // Enable vibrate
        } else {
            // Disable vibrate
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.togglebutton:
                onToggleClicked(view);
                break;
//            case R.id.start_in_process:
//                startActivity(new Intent(this, InProcessActivity.class));
//                break;
//
        }

    }
}
