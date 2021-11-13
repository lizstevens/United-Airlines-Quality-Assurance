package edu.msu.steve702.ua_quality_assurance_platform.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.msu.steve702.ua_quality_assurance_platform.InProcessAdapter;
import edu.msu.steve702.ua_quality_assurance_platform.InProcessListActivity;
import edu.msu.steve702.ua_quality_assurance_platform.R;
import edu.msu.steve702.ua_quality_assurance_platform.UpdateInProcessActivity;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.AuditObject;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.CalibrationTableDataObject;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.ChecklistDataObject;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.InProcessObject;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.ROMTableDataObject;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.ShelfLifeTableDataObject;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.TechnicalTableDataObject;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.TraceabilityTableDataObject;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.TrainingTableDataObject;

import static android.content.ContentValues.TAG;

public class AuditAdapter extends RecyclerView.Adapter<AuditAdapter.AuditViewHolder>  {

    private Context mCtx;
    private List<AuditObject> auditList;
    public FirebaseFirestore db;
    private Intent intent;
    private String this_audit_id;

    public AuditAdapter(Context mCtx, List<AuditObject> auditList) {
        this.mCtx = mCtx;
        this.auditList = auditList;

        db = FirebaseFirestore.getInstance();
        intent = new Intent(mCtx, AuditActivity.class);
    }

    @NonNull
    @Override
    public AuditAdapter.AuditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AuditViewHolder(
                LayoutInflater.from(mCtx).inflate(R.layout.view_audit, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull AuditAdapter.AuditViewHolder holder, int position) {
        AuditObject product = auditList.get(position);

        holder.auditorName.setText("Auditor Name: " + product.getAuditNameObj());
        holder.date.setText("Date: " + product.getAuditDateObj());
        holder.location.setText("Location: " + product.getLocationObj());
        holder.auditTitle.setText("Audit Title: " + product.getAuditTitleObj());
        holder.auditNumber.setText("Audit Number: " + product.getAuditNumberObj());
        holder.vendorName.setText("Vendor Name: " + product.getVendorNameObj());
        holder.vendorNumber.setText("Vendor Number: " + product.getVendorNumObj());
        holder.description.setText("Description: " + product.getAuditDescripObj());
        holder.status.setText("Status: Some Status");
    }

    @Override
    public int getItemCount() {
        return auditList.size();
    }

    class AuditViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView auditorName, date, location, auditTitle, auditNumber;
        TextView vendorName, vendorNumber, description, status;

        public AuditViewHolder(View itemView) {
            super(itemView);

            auditorName = itemView.findViewById(R.id.textview_auditorName);
            date = itemView.findViewById(R.id.textview_date);
            location = itemView.findViewById(R.id.textview_location);
            auditTitle = itemView.findViewById(R.id.textview_auditTitle);
            auditNumber = itemView.findViewById(R.id.textview_auditNumber);
            vendorName = itemView.findViewById(R.id.textview_vendorName);
            vendorNumber = itemView.findViewById(R.id.textview_vendorNumber);
            description = itemView.findViewById(R.id.textview_description);
            status = itemView.findViewById(R.id.textview_status);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            AuditObject audit = auditList.get(getAbsoluteAdapterPosition());
            queryDB(audit);
        }

        private void queryDB(AuditObject audit) {
            this_audit_id = audit.getId();
            db.collection("Audit").document(this_audit_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        AuditObject auditObject = doc.toObject(AuditObject.class);

                        Toast.makeText(mCtx, "Query Audit Specs", Toast.LENGTH_SHORT).show();

                        intent.putExtra("audit_id", this_audit_id);
                        intent.putExtra("auditObject", auditObject);
                        queryInProcess();
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
        }

        private void queryInProcess() {
            db.collection("Audit").document(this_audit_id).collection("in-process").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<InProcessObject> inProcessObjectList = new ArrayList<>();

                        for (DocumentSnapshot doc: task.getResult()) {
                            InProcessObject p = doc.toObject(InProcessObject.class);
                            p.setId(doc.getId());
                            inProcessObjectList.add(p);
                        }

                        intent.putExtra("InProcessList", (Serializable) inProcessObjectList);

                        queryTechDataTable();
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });

        }

        private void queryTechDataTable() {
            db.collection("Audit").document(this_audit_id).collection("TechnicalDataTable").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<TechnicalTableDataObject> list = new ArrayList<>();

                        for (DocumentSnapshot doc: task.getResult()) {
                            TechnicalTableDataObject p = doc.toObject(TechnicalTableDataObject.class);
                            p.setId(doc.getId());
                            list.add(p);
                        }

                        if (list.size() == 1) {
                            intent.putExtra("TechnicalDataTable", list.get(0));
                        }

                        queryROMTable();
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });

        }

        public void queryROMTable() {
            db.collection("Audit").document(this_audit_id).collection("ROMTable").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<ROMTableDataObject> list = new ArrayList<>();

                        for (DocumentSnapshot doc: task.getResult()) {
                            ROMTableDataObject p = doc.toObject(ROMTableDataObject.class);
                            p.setId(doc.getId());
                            list.add(p);
                        }

                        if (list.size() == 1) {
                            intent.putExtra("ROMTableData", list.get(0));
                        }
                        queryCalibrationTable();
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
        }

        public void queryCalibrationTable() {
            db.collection("Audit").document(this_audit_id).collection("CalibrationTable").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<CalibrationTableDataObject> list = new ArrayList<>();

                        for (DocumentSnapshot doc: task.getResult()) {
                            CalibrationTableDataObject p = doc.toObject(CalibrationTableDataObject.class);
                            p.setId(doc.getId());
                            list.add(p);
                        }

                        if (list.size() == 1) {
                            intent.putExtra("CalibrationTableData", list.get(0));
                        }
                        queryTrainingTable();
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
        }

        public void queryTrainingTable() {
            db.collection("Audit").document(this_audit_id).collection("TrainingTable").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<TrainingTableDataObject> list = new ArrayList<>();

                        for (DocumentSnapshot doc: task.getResult()) {
                            TrainingTableDataObject p = doc.toObject(TrainingTableDataObject.class);
                            p.setId(doc.getId());
                            list.add(p);
                        }

                        if (list.size() == 1) {
                            intent.putExtra("TrainingTableData", list.get(0));
                        }
                        queryTraceabilityTable();
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
        }

        public void queryTraceabilityTable() {
            db.collection("Audit").document(this_audit_id).collection("TraceabilityTable").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<TraceabilityTableDataObject> list = new ArrayList<>();

                        for (DocumentSnapshot doc: task.getResult()) {
                            TraceabilityTableDataObject p = doc.toObject(TraceabilityTableDataObject.class);
                            p.setId(doc.getId());
                            list.add(p);
                        }

                        if (list.size() == 1) {
                            intent.putExtra("TraceabilityTableData", list.get(0));
                        }
                        queryShelfLifeTable();
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
        }

        public void queryShelfLifeTable() {
            db.collection("Audit").document(this_audit_id).collection("ShelfLifeTable").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<ShelfLifeTableDataObject> list = new ArrayList<>();

                        for (DocumentSnapshot doc: task.getResult()) {
                            ShelfLifeTableDataObject p = doc.toObject(ShelfLifeTableDataObject.class);
                            p.setId(doc.getId());
                            list.add(p);
                        }

                        if (list.size() == 1) {
                            intent.putExtra("ShelfLifeTableData", list.get(0));
                        }
                        mCtx.startActivity(intent);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
        }

        public void queryChecklist() {
            db.collection("Audit").document(this_audit_id).collection("Checklist").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<ChecklistDataObject> list = new ArrayList<>();

                        for (DocumentSnapshot doc: task.getResult()) {
                            ChecklistDataObject checklistDataObject = doc.toObject(ChecklistDataObject.class);
                            checklistDataObject.setId(doc.getId());
                            list.add(checklistDataObject);
                        }

                        if (list.size() == 1) {
                            intent.putExtra("ChecklistData", list.get(0));
                        }
                        mCtx.startActivity(intent);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
        }


    }

}
