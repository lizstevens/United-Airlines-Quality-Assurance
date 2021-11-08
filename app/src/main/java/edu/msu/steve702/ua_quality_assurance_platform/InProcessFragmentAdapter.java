package edu.msu.steve702.ua_quality_assurance_platform;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.msu.steve702.ua_quality_assurance_platform.data_objects.InProcessObject;
import edu.msu.steve702.ua_quality_assurance_platform.main_fragments.InProcessFragment;

public class InProcessFragmentAdapter extends RecyclerView.Adapter<InProcessFragmentAdapter.InProcessViewHolder>{
    private Context mCtx;
    private List<InProcessObject> inProcessList;
    private InProcessFragment fragment;

    public InProcessFragmentAdapter(Context mCtx, List<InProcessObject> inProcessList, InProcessFragment fragment) {
        this.mCtx = mCtx;
        this.inProcessList = inProcessList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public InProcessFragmentAdapter.InProcessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InProcessFragmentAdapter.InProcessViewHolder(
                LayoutInflater.from(mCtx).inflate(R.layout.view_in_process, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull InProcessFragmentAdapter.InProcessViewHolder holder, int position) {
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
            inProcessList.remove(getAbsoluteAdapterPosition());
            notifyDataSetChanged();
            fragment.setInProcessTable(getAbsoluteAdapterPosition(), inProcess);
        }
    }
}
