package edu.msu.steve702.ua_quality_assurance_platform;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import edu.msu.steve702.ua_quality_assurance_platform.data_objects.AuditObject;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.InProcessObject;

public class InProcessListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private InProcessAdapter adapter;
    private List<InProcessObject> inProcessList;
    private ProgressBar progressBar;

    private FirebaseFirestore db;

    private String audit_id;

    private AuditObject auditObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_process_list);

        Intent intent = getIntent();

        if (intent.getExtras() != null && intent.getExtras().containsKey("audit_id")) {
            audit_id = getIntent().getStringExtra("audit_id");
        }

        progressBar = findViewById(R.id.progressbar);

        recyclerView = findViewById(R.id.recyclerview_inProcess);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        inProcessList = new ArrayList<>();
        adapter = new InProcessAdapter(this, inProcessList, audit_id);

        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        // get all in the in processs sheets in the collection

        db.collection("Audit").document(audit_id).collection("in-process").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        progressBar.setVisibility(View.GONE);
                        // not empty
                        if(!queryDocumentSnapshots.isEmpty()){
                            // get the list of documents
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            // display in recycler view
                            for(DocumentSnapshot d : list){

                                InProcessObject p = d.toObject(InProcessObject.class);
                                p.setId(d.getId());
                                inProcessList.add(p);
                            }

                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }


}
