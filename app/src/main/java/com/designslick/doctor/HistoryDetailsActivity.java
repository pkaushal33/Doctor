package com.designslick.doctor;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.designslick.doctor.Common.Api;
import com.designslick.doctor.Common.CustomDetailsAdapter;
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

public class HistoryDetailsActivity extends AppCompatActivity {

    private Utility utility;

    private TextView tvPatientName, tvPatientAge, tvPatientSex, tvPatientMobile, tvPatientDOB,
            tvPatientAddress, tvPatientQualification, tvPatientOccupation;

    private RecyclerView recyclerView;
    private List<ResGetPatientDetails.data.unpaid> data=new ArrayList<>();

    private String strPatientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);

        utility = new Utility(HistoryDetailsActivity.this);

        strPatientId=getIntent().getStringExtra("PatientId");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Patient History Details");
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

        tvPatientName = (TextView) findViewById(R.id.tv_details_name);
        tvPatientAge = (TextView) findViewById(R.id.tv_details_age);
        tvPatientSex = (TextView) findViewById(R.id.tv_details_sex);
        tvPatientMobile = (TextView) findViewById(R.id.tv_details_mobile);
        tvPatientDOB = (TextView) findViewById(R.id.tv_details_dob);
        tvPatientAddress = (TextView) findViewById(R.id.tv_details_address);
        tvPatientQualification = (TextView) findViewById(R.id.tv_details_qualification);
        tvPatientOccupation = (TextView) findViewById(R.id.tv_details_occupation);

        recyclerView=(RecyclerView)findViewById(R.id.rl_details_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        getPatientDetails();

    }

    private void getPatientDetails() {


        final ProgressDialog progressDialog = ProgressDialog.show(HistoryDetailsActivity.this, "", "Please Wait..");

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Utility.url).
                addConverterFactory(GsonConverterFactory.create()).
                client(Utility.addInterCeptor()).
                build();

        Api api = retrofit.create(Api.class);

        Call<ResGetPatientDetails> call = api.getPatientHistory(strPatientId);

        call.enqueue(new Callback<ResGetPatientDetails>() {
            @Override
            public void onResponse(Call<ResGetPatientDetails> call, Response<ResGetPatientDetails> response) {

                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

                if(response.body().getErrorcode()==1){

                    tvPatientName.setText(response.body().getData().get(0).getName());
                    tvPatientAge.setText(response.body().getData().get(0).getAge());
                    tvPatientSex.setText(response.body().getData().get(0).getGender());
                    tvPatientMobile.setText(response.body().getData().get(0).getMobile());
                    tvPatientDOB.setText(response.body().getData().get(0).getDob());
                    tvPatientAddress.setText(response.body().getData().get(0).getAddress());
                    tvPatientQualification.setText(response.body().getData().get(0).getQualification());
                    tvPatientOccupation.setText(response.body().getData().get(0).getOccupation());

                    data=response.body().data.get(0).getUnpaid();

                    recyclerView.setAdapter(new CustomDetailsAdapter(HistoryDetailsActivity.this,data));


                }
                else {
                    Utility.showalert(HistoryDetailsActivity.this,response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResGetPatientDetails> call, Throwable t) {

            }
        });

    }
}
