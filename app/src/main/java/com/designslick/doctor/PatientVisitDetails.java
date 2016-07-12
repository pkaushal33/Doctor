package com.designslick.doctor;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.designslick.doctor.Common.Api;
import com.designslick.doctor.Common.CustomDetailsAdapter;
import com.designslick.doctor.Common.CustomHistoryAdapter;
import com.designslick.doctor.Common.CustomVisitDetails;
import com.designslick.doctor.Common.Utility;
import com.designslick.doctor.Response.ResGetPatient;
import com.designslick.doctor.Response.ResGetPatientDetails;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kaushalpatel on 7/7/16.
 */
public class PatientVisitDetails extends AppCompatActivity {


    private Utility utility;

    private RecyclerView recyclerView;
    private CustomVisitDetails customVisitDetails;

    private List<ResGetPatient.data> resGetPatients=new ArrayList<>();
    private List<ResGetPatientDetails.data.unpaid> data=new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;

    private String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_details);

        utility=new Utility(PatientVisitDetails.this);
        id=getIntent().getStringExtra("id");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Patient Visit Details");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
            toolbar.setTitleTextColor(Color.rgb(128, 206, 180));

        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView=(RecyclerView)findViewById(R.id.rv_patient_histroy_list);
        recyclerView.setLayoutManager(mLinearLayoutManager);

       // GetPatient();
        getPatientDetails();
    }

    private void GetPatient() {

        final ProgressDialog progressDialog = ProgressDialog.show(PatientVisitDetails.this, "", "Please Wait..");

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Utility.url).
                addConverterFactory(GsonConverterFactory.create()).
                client(Utility.addInterCeptor()).
                build();

        Api api = retrofit.create(Api.class);

        Call<ResGetPatient> call = api.getPatient();

        call.enqueue(new Callback<ResGetPatient>() {
            @Override
            public void onResponse(Call<ResGetPatient> call, Response<ResGetPatient> response) {

                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.body().errorcode == 1) {


                    resGetPatients=response.body().data;

                }

            }

            @Override
            public void onFailure(Call<ResGetPatient> call, Throwable t) {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        });

    }

    private void getPatientDetails() {


        final ProgressDialog progressDialog = ProgressDialog.show(PatientVisitDetails.this, "", "Please Wait..");

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Utility.url).
                addConverterFactory(GsonConverterFactory.create()).
                client(Utility.addInterCeptor()).
                build();

        Api api = retrofit.create(Api.class);

        Call<ResGetPatientDetails> call = api.getPatientHistory(id);

        call.enqueue(new Callback<ResGetPatientDetails>() {
            @Override
            public void onResponse(Call<ResGetPatientDetails> call, Response<ResGetPatientDetails> response) {

                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

                if(response.body().getErrorcode()==1){

                     data=response.body().data.get(0).getUnpaid();

                    customVisitDetails=new CustomVisitDetails(PatientVisitDetails.this, response.body().data.get(0).unpaid);

                    recyclerView.setAdapter(customVisitDetails);

                    customVisitDetails.notifyDataSetChanged();


                }
                else {
                    Utility.showalert(PatientVisitDetails.this,response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResGetPatientDetails> call, Throwable t) {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        });

    }

}
