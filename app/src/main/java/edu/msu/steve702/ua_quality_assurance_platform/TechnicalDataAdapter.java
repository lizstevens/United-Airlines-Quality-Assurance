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

import edu.msu.steve702.ua_quality_assurance_platform.data_objects.TabularDataObject;

public class TechnicalDataAdapter extends RecyclerView.Adapter<TechnicalDataAdapter.TechnicalDataViewHolder> {

    private Context mCtx;
    private List<TabularDataObject> techDataList;

    public TechnicalDataAdapter(Context mCtx, List<TabularDataObject> techDataList) {
        this.mCtx = mCtx;
        this.techDataList = techDataList;
    }

    @NonNull
    @Override
    public TechnicalDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TechnicalDataViewHolder(
                LayoutInflater.from(mCtx).inflate(R.layout.view_tech_data, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull TechnicalDataViewHolder holder, int position) {
        TabularDataObject product = techDataList.get(position);

        holder.textviewTdPartNum.setText(product.getTdPartNumObj());
        holder.textviewTdManuf.setText(product.getTdManufObj());
        holder.textviewTdAta.setText(product.getTdAtaObj());
        holder.textviewTdRevLevel.setText(product.getTdRevLevelObj());
        holder.textviewTdRevLevelDate.setText(product.getTdRevDateObj());
        holder.textviewTdComments.setText(product.getTdCommentsObj());


    }

    @Override
    public int getItemCount() {
        return techDataList.size();
    }

    class TechnicalDataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textviewTdPartNum, textviewTdManuf, textviewTdAta, textviewTdRevLevel, textviewTdRevLevelDate, textviewTdComments;

        public TechnicalDataViewHolder(View itemView) {
            super(itemView);

            textviewTdPartNum = itemView.findViewById(R.id.textview_tdPartNum);
            textviewTdManuf = itemView.findViewById(R.id.textview_tdManuf);
            textviewTdAta = itemView.findViewById(R.id.textview_tdAta);
            textviewTdRevLevel = itemView.findViewById(R.id.textview_tdRevLevel);
            textviewTdRevLevelDate = itemView.findViewById(R.id.textview_tdRevLevelDate);
            textviewTdComments = itemView.findViewById(R.id.textview_tdComments);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            TabularDataObject techData = techDataList.get(getAbsoluteAdapterPosition());
            Intent intent = new Intent(mCtx, UpdateTabularActvity.class);
            intent.putExtra("technical-data", techData);

            mCtx.startActivity(intent);
        }
    }
}
