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

import edu.msu.steve702.ua_quality_assurance_platform.data_objects.TabularDataObject;

public class TechnicalDataListActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private TechnicalDataAdapter adapter;
    private List<TabularDataObject> techDataList;
    private ProgressBar progressBar;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_td_list);

        progressBar = findViewById(R.id.progressbar);

        recyclerView = findViewById(R.id.recyclerview_td);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        techDataList = new ArrayList<>();
        adapter = new TechnicalDataAdapter(this, techDataList);

        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        // get all in the in processs sheets in the collection
        db.collection("technical-data").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        progressBar.setVisibility(View.GONE);
                        // not empty
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // get the list of documents
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            // display in recycler view
                            for (DocumentSnapshot d : list) {

                                TabularDataObject p = d.toObject(TabularDataObject.class);
                                p.setId(d.getId());
                                techDataList.add(p);
                            }

                            adapter.notifyDataSetChanged();
                        }
                    }
                });

        findViewById(R.id.back_to_main).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_to_main:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }


    }
}
