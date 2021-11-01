package edu.msu.steve702.ua_quality_assurance_platform;

import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import edu.msu.steve702.ua_quality_assurance_platform.data_objects.InProcessObject;

public class InProcessListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private InProcessAdapter adapter;
    private List<InProcessObject> inProcessList;
    private ProgressBar progressBar;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_process_list);

        progressBar = findViewById(R.id.progressbar);

        recyclerView = findViewById(R.id.recyclerview_inProcess);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        inProcessList = new ArrayList<>();
        adapter = new InProcessAdapter(this, inProcessList);

        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        // get all in the in processs sheets in the collection

//        db.collection("in-process").get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//                        progressBar.setVisibility(View.GONE);
//                        // not empty
//                        if(!queryDocumentSnapshots.isEmpty()){
//                            // get the list of documents
//                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
//
//                            // display in recycler view
//                            for(DocumentSnapshot d : list){
//
//                                DataObject p = d.toObject(DataObject.class);
//                                p.setId(d.getId());
//                                inProcessList.add(p);
//                            }
//
//                            adapter.notifyDataSetChanged();
//                        }
//                    }
//                });
    }


}
