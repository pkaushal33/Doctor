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
import com.designslick.doctor.PatientVisitDetails;
import com.designslick.doctor.R;
import com.designslick.doctor.Response.ResGetPatient;
import com.designslick.doctor.Response.ResGetPatientDetails;

import java.util.List;

/**
 * Created by kaushalpatel on 7/7/16.
 */
public class CustomVisitDetails extends RecyclerView.Adapter<CustomVisitDetails.VisitHistoryHolder> {

    private List<ResGetPatientDetails.data.unpaid> resGetPatientDetails;
    private Context mContext;

    @Override
    public VisitHistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View dueView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_visit_details_layout, parent, false);
        return new VisitHistoryHolder(dueView);
    }

    @Override
    public void onBindViewHolder(VisitHistoryHolder holder, int position) {

        holder.tvVisitDate.setText(resGetPatientDetails.get(position).getVisit_date());
        Glide.with(mContext)
                .load("http://designslick.com/doctor/image/"+resGetPatientDetails.get(position).getPrescription_file())
                .into(holder.ivPrescrption);

        Glide.with(mContext)
                .load("http://designslick.com/doctor/report/"+resGetPatientDetails.get(position).getReport_file())
                .into(holder.ivReport);


    }


    @Override
    public int getItemCount() {
        return resGetPatientDetails.size();
    }

    public CustomVisitDetails(PatientVisitDetails patientVisitDetails, List<ResGetPatientDetails.data.unpaid> resGetPatients){
        this.mContext=patientVisitDetails;
        this.resGetPatientDetails=resGetPatients;
    }

    public class VisitHistoryHolder extends RecyclerView.ViewHolder {
        public TextView tvVisitDate;
        public ImageView ivPrescrption,ivReport;

        public VisitHistoryHolder(View view) {
            super(view);
            tvVisitDate=(TextView)view.findViewById(R.id.tv_custom_visited_date);
            ivPrescrption=(ImageView) view.findViewById(R.id.iv_custom_visited_prescription);
            ivReport=(ImageView) view.findViewById(R.id.iv_custom_visited_report);
        }
    }

}
