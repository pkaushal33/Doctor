package com.designslick.doctor.Common;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.designslick.doctor.HistoryDetailsActivity;
import com.designslick.doctor.PatientHistoryActivtiy;
import com.designslick.doctor.R;
import com.designslick.doctor.Response.ResGetPatient;
import com.designslick.doctor.Response.ResGetPatientDetails;

import java.util.List;

/**
 * Created by kaushalpatel on 5/31/16.
 */
public class CustomDetailsAdapter extends RecyclerView.Adapter<CustomDetailsAdapter.HistoryHolder> {

    private List<ResGetPatientDetails.data.unpaid> resGetPatientDetails;
    private Context mContext;

    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View dueView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_details_layout, parent, false);
        return new HistoryHolder(dueView);
    }

    @Override
    public void onBindViewHolder(HistoryHolder holder, int position) {

        holder.tvVisitedDate.setText(resGetPatientDetails.get(position).getVisit_date());

        Glide.with(mContext)
                .load("http://designslick.com/doctor/image/"+resGetPatientDetails.get(position).getPrescription_file())
                .into(holder.ivPrescription);

        Glide.with(mContext)
                .load("http://designslick.com/doctor/image/"+resGetPatientDetails.get(position).getReport_file())
                .into(holder.ivReport);


        holder.tvVisitedDate.setOnClickListener(clicklistener);
        holder.ivPrescription.setOnClickListener(clicklistener);
        holder.ivReport.setOnClickListener(clicklistener);

    }

    View.OnClickListener clicklistener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

                Utility.showalert(mContext,v.getId()+"");

        }
    };

    @Override
    public int getItemCount() {
        return resGetPatientDetails.size();
    }

    public CustomDetailsAdapter(HistoryDetailsActivity patientHistoryActivtiy, List<ResGetPatientDetails.data.unpaid> resGetPatients){
        this.mContext=patientHistoryActivtiy;
        this.resGetPatientDetails=resGetPatients;
    }

    public class HistoryHolder extends RecyclerView.ViewHolder {
        public TextView tvVisitedDate;
        public ImageView ivPrescription,ivReport;

        public HistoryHolder(View view) {
            super(view);
            tvVisitedDate=(TextView)view.findViewById(R.id.tv_custom_visited_date);
            ivPrescription=(ImageView)view.findViewById(R.id.iv_custom_prescription_image);
            ivReport=(ImageView)view.findViewById(R.id.iv_custom_report);

        }
    }



}
