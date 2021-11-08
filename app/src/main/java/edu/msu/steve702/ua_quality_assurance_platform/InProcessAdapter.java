package edu.msu.steve702.ua_quality_assurance_platform;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;

import java.util.List;

import edu.msu.steve702.ua_quality_assurance_platform.activities.AuditAdapter;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.AuditObject;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.InProcessObject;

public class InProcessAdapter extends RecyclerView.Adapter<InProcessAdapter.InProcessViewHolder> {

    private Context mCtx;
    private List<InProcessObject> inProcessList;
    private String audit_id;

    public InProcessAdapter(Context mCtx, List<InProcessObject> inProcessList, String audit_id) {
        this.mCtx = mCtx;
        this.inProcessList = inProcessList;
        this.audit_id = audit_id;
    }

    @NonNull
    @Override
    public InProcessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InProcessViewHolder(
                LayoutInflater.from(mCtx).inflate(R.layout.view_in_process, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull InProcessViewHolder holder, int position) {
        InProcessObject product = inProcessList.get(position);

        holder.textViewEmpName.setText(product.getEmployeeNameObj());
        holder.textViewPartNum.setText(product.getPartNumberObj());
        holder.textViewSerialNum.setText(product.getSerialNumberObj());
        holder.textViewNomenclature.setText(product.getNomenclatureObj());
        holder.textViewTask.setText(product.getTaskObj());
        holder.textViewTechSpec.setText(product.getTechSpecificationsObj());
        holder.textViewTooling.setText(product.getToolingObj());
        holder.textViewShelfLife.setText(product.getShelfLifeObj());
        holder.textViewTrace.setText(product.getTraceObj());
        holder.textViewReqTraining.setText(product.getReqTrainingObj());
        holder.textViewTrainingDate.setText(product.getTrainingDateObj());


    }

    @Override
    public int getItemCount() {
        return inProcessList.size();
    }

    class InProcessViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textViewEmpName, textViewPartNum, textViewSerialNum, textViewNomenclature, textViewTask;
        TextView textViewTechSpec, textViewTooling, textViewShelfLife, textViewTrace, textViewReqTraining, textViewTrainingDate;

        public InProcessViewHolder(View itemView) {
            super(itemView);

            textViewEmpName = itemView.findViewById(R.id.textview_empName);
            textViewPartNum = itemView.findViewById(R.id.textview_partNum);
            textViewSerialNum = itemView.findViewById(R.id.textview_serialNum);
            textViewNomenclature = itemView.findViewById(R.id.textview_nomenclature);
            textViewTask = itemView.findViewById(R.id.textview_task);
            textViewTechSpec = itemView.findViewById(R.id.textview_techSpec);
            textViewTooling = itemView.findViewById(R.id.textview_tooling);
            textViewShelfLife = itemView.findViewById(R.id.textview_shelfLife);
            textViewTrace = itemView.findViewById(R.id.textview_trace);
            textViewReqTraining = itemView.findViewById(R.id.textview_reqTraining);
            textViewTrainingDate = itemView.findViewById(R.id.textview_reqTrainingDate);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            InProcessObject inProcess = inProcessList.get(getAbsoluteAdapterPosition());

            Intent intent = new Intent(mCtx, UpdateInProcessActivity.class);

            intent.putExtra("audit_id", audit_id);
            intent.putExtra("in-process", inProcess.getId());
            intent.putExtra("in-process", inProcess);
            mCtx.startActivity(intent);
        }
    }
}
