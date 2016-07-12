package com.designslick.doctor.Common;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.designslick.doctor.HistoryDetailsActivity;
import com.designslick.doctor.PatientHistoryActivtiy;
import com.designslick.doctor.PatientVisitDetails;
import com.designslick.doctor.R;
import com.designslick.doctor.Response.ResCommon;
import com.designslick.doctor.Response.ResGetPatient;
import com.designslick.doctor.Response.ResGetPatientDetails;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kaushalpatel on 5/31/16.
 */
public class CustomDetailsAdapter extends RecyclerView.Adapter<CustomDetailsAdapter.HistoryHolder> {

    private List<ResGetPatientDetails.data.unpaid> resGetPatientDetails;
    private Context mContext;
    private String strPatientId;

    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View dueView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_details_layout, parent, false);
        return new HistoryHolder(dueView);
    }

    @Override
    public void onBindViewHolder(HistoryHolder holder, int position) {

        holder.tvVisitedDate.setText(resGetPatientDetails.get(position).getVisit_date());


        holder.tvVisitedDate.setOnClickListener(clicklistener);
        holder.ivDelete.setOnClickListener(clicklistener);


        holder.tvVisitedDate.setTag(holder);
        holder.ivDelete.setTag(holder);


    }

    View.OnClickListener clicklistener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            HistoryHolder holder = (HistoryHolder) v.getTag();
            int position = holder.getAdapterPosition();
            switch (v.getId()){
                case R.id.tv_custom_visited_date_to:

                    Intent inDetails=new Intent(mContext, PatientVisitDetails.class);
                    inDetails.putExtra("id",strPatientId);
                    mContext.startActivity(inDetails);

                    break;

                case R.id.iv_custom_visited_delete:
                    Log.d("Histroy Id",resGetPatientDetails.get(position).getId()+"");
                    deletePrescription(position,String.valueOf(resGetPatientDetails.get(position).getId()));
                    break;
            }

        }
    };

    public void deletePrescription(final int position, String id){
        final ProgressDialog progressDialog = ProgressDialog.show(mContext, "", "Please Wait..");

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Utility.url).
                addConverterFactory(GsonConverterFactory.create()).
                client(Utility.addInterCeptor()).
                build();

        Api api = retrofit.create(Api.class);

        Call<ResCommon> call = api.deletePatientVisit(id);

        call.enqueue(new Callback<ResCommon>() {
            @Override
            public void onResponse(Call<ResCommon> call, Response<ResCommon> response) {

                if (progressDialog.isShowing())
                    progressDialog.dismiss();
               if(response.body().getErrorcode()==1){

                   resGetPatientDetails.remove(position);
                   notifyDataSetChanged();

               }


            }

            @Override
            public void onFailure(Call<ResCommon> call, Throwable t) {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return resGetPatientDetails.size();
    }

    public CustomDetailsAdapter(HistoryDetailsActivity patientHistoryActivtiy, List<ResGetPatientDetails.data.unpaid> resGetPatients,String strPatientId){
        this.mContext=patientHistoryActivtiy;
        this.resGetPatientDetails=resGetPatients;
        this.strPatientId=strPatientId;
    }

    public class HistoryHolder extends RecyclerView.ViewHolder {
        public TextView tvVisitedDate;
        public ImageView ivDelete;

        public HistoryHolder(View view) {
            super(view);
            tvVisitedDate=(TextView)view.findViewById(R.id.tv_custom_visited_date_to);
            ivDelete=(ImageView)view.findViewById(R.id.iv_custom_visited_delete);

        }
    }



}
