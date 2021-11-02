package edu.msu.steve702.ua_quality_assurance_platform.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.msu.steve702.ua_quality_assurance_platform.InProcessAdapter;
import edu.msu.steve702.ua_quality_assurance_platform.R;
import edu.msu.steve702.ua_quality_assurance_platform.UpdateInProcessActivity;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.AuditObject;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.InProcessObject;

public class AuditAdapter extends RecyclerView.Adapter<AuditAdapter.AuditViewHolder>  {

    private Context mCtx;
    private List<AuditObject> auditList;

    public AuditAdapter(Context mCtx, List<AuditObject> auditList) {
        this.mCtx = mCtx;
        this.auditList = auditList;
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
            Intent intent = new Intent(mCtx, AuditActivity.class);
            intent.putExtra("audit", audit);

            mCtx.startActivity(intent);
        }
    }

}
