package edu.msu.steve702.ua_quality_assurance_platform.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import edu.msu.steve702.ua_quality_assurance_platform.R;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.AuditObject;

import static android.content.ContentValues.TAG;

/**
 * EditAuditListActivity Class
 * Handles querying the database and listing the audits for editing.
 */
public class EditAuditListActivity extends AppCompatActivity {

    /** the recycler view **/
    private RecyclerView recyclerView;
    /** the audit adapter **/
    private AuditAdapter adapter;
    /** the audit list **/
    private List<AuditObject> auditList;
    /** the searchview **/
    private SearchView searchView;
    /** database reference **/
    private FirebaseFirestore db;

    /**
     * Function for creating the activity
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_audit_list);


        recyclerView = findViewById(R.id.recyclerview_editaudit);
        searchView = findViewById(R.id.searchView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        auditList = new ArrayList<>();
        adapter = new AuditAdapter(this, auditList);

        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        // Search View Query Listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            // Called when submitquery is searched
            @Override
            public boolean onQueryTextSubmit(String query) {
                queryDatabase(query);
                return false;
            }

            //
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    /**
     * Function for querying the database
     * @param query the query by string
     */
    public void queryDatabase(String query) {
        db.collection("Audit")
                .whereEqualTo("auditNameObj", query)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            auditList.clear();

                            for (DocumentSnapshot doc: task.getResult()) {
                                AuditObject a = doc.toObject(AuditObject.class);
                                a.setId(doc.getId());
                                auditList.add(a);
                            }
                            adapter.notifyDataSetChanged();

                            if (auditList.isEmpty()) {
                                Toast.makeText(getApplicationContext(), "No Audits Found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}
