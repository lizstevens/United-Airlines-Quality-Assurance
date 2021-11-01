package edu.msu.steve702.ua_quality_assurance_platform;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
//import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import edu.msu.steve702.ua_quality_assurance_platform.data_objects.TabularDataObject;

public class UpdateTabularActvity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseFirestore db;

    private TabularDataObject techDataIntent;

    private EditText tdPartNumEdit, tdManufEdit, tdAtaEdit , tdRevLevelEdit, tdRevDateEdit, tdCommentsEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabular_data_update);

        techDataIntent = (TabularDataObject) getIntent().getSerializableExtra("technical-data");
        db = FirebaseFirestore.getInstance();

        // edit technical data table
        tdPartNumEdit = findViewById(R.id.row1_col1);
        tdManufEdit = findViewById(R.id.row1_col2);
        tdAtaEdit = findViewById(R.id.row1_col3);
        tdRevLevelEdit = findViewById(R.id.row1_col4);
        tdRevDateEdit = findViewById(R.id.row1_col5);
        tdCommentsEdit = findViewById(R.id.row1_col6);


        tdPartNumEdit.setText(techDataIntent.getTdPartNumObj());
        tdManufEdit.setText(techDataIntent.getTdManufObj());
        tdAtaEdit.setText(techDataIntent.getTdAtaObj());
        tdRevLevelEdit.setText(techDataIntent.getTdRevLevelObj());
        tdRevDateEdit.setText(techDataIntent.getTdRevDateObj());
        tdCommentsEdit.setText(techDataIntent.getTdCommentsObj());


//        findViewById(R.id.save_btn).setOnClickListener(this);
        findViewById(R.id.update_td).setOnClickListener(this);
        findViewById(R.id.switch_to_in_process_btn).setOnClickListener(this);
    }

    private void updateTechnicalData() {
        String tdPartNumObj = ((EditText)tdPartNumEdit).getText().toString();
        String tdManufObj = tdManufEdit.getText().toString();
        String tdAtaObj = tdAtaEdit.getText().toString();
        String tdRevLevelObj = tdRevLevelEdit.getText().toString();
        String tdRevDateObj = tdRevDateEdit.getText().toString();
        String tdCommentsObj = tdCommentsEdit.getText().toString();


        TabularDataObject techDataUpdate = new TabularDataObject(
                tdPartNumObj,
                tdManufObj,
                tdAtaObj,
                tdRevLevelObj,
                tdRevDateObj,
                tdCommentsObj
        );

        // override existing data using id
        db.collection("technical-data").document(techDataIntent.getId()).set(techDataUpdate)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UpdateTabularActvity.this, "Technical Data Updated", Toast.LENGTH_LONG).show();

                    }
                });
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.update_td:
                updateTechnicalData();
                break;
            case R.id.switch_to_in_process_btn:
                startActivity(new Intent(this, InProcessActivity.class));
                break;
        }

    }


}
