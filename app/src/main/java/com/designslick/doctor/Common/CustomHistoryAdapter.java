package com.designslick.doctor.Common;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.designslick.doctor.HistoryDetailsActivity;
import com.designslick.doctor.PatientHistoryActivtiy;
import com.designslick.doctor.R;
import com.designslick.doctor.Response.ResGetPatient;

import java.util.List;

/**
 * Created by kaushalpatel on 5/31/16.
 */
public class CustomHistoryAdapter extends RecyclerView.Adapter<CustomHistoryAdapter.HistoryHolder> {

    private List<ResGetPatient.data> resGetPatients;
    private Context mContext;

    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View dueView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_history_layout, parent, false);
        return new HistoryHolder(dueView);
    }

    @Override
    public void onBindViewHolder(HistoryHolder holder, int position) {

        holder.tvName.setText(resGetPatients.get(position).getName());
        holder.tvmobile.setText(resGetPatients.get(position).getMobile());
        holder.tvName.setOnClickListener(clicklistener);
        holder.tvName.setTag(holder);

    }

    View.OnClickListener clicklistener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            HistoryHolder holder = (HistoryHolder) v.getTag();
            int position = holder.getAdapterPosition();

            Intent inDetails=new Intent(mContext,HistoryDetailsActivity.class);
            inDetails.putExtra("PatientId",resGetPatients.get(position).getId());
            mContext.startActivity(inDetails);
        }
    };

    @Override
    public int getItemCount() {
        return resGetPatients.size();
    }

    public CustomHistoryAdapter(PatientHistoryActivtiy patientHistoryActivtiy, List<ResGetPatient.data> resGetPatients){
        this.mContext=patientHistoryActivtiy;
        this.resGetPatients=resGetPatients;
    }

    public class HistoryHolder extends RecyclerView.ViewHolder {
        public TextView tvName,tvmobile;

        public HistoryHolder(View view) {
            super(view);
            tvName=(TextView)view.findViewById(R.id.tv_custom_name);
            tvmobile=(TextView)view.findViewById(R.id.tv_custom_mobile);
        }
    }



}
