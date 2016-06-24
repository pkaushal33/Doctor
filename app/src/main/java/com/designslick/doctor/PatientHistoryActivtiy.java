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
import com.designslick.doctor.Common.CustomHistoryAdapter;
import com.designslick.doctor.Common.Utility;
import com.designslick.doctor.Response.ResGetPatient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kaushalpatel on 6/16/16.
 */
public class PatientHistoryActivtiy extends AppCompatActivity{

    private Utility utility;

    private RecyclerView recyclerView;
    private CustomHistoryAdapter customHistoryAdapter;

    private List<ResGetPatient.data> resGetPatients=new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;

    private SearchView edSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patienthistory);

        utility=new Utility(PatientHistoryActivtiy.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Patient History");
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
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView=(RecyclerView)findViewById(R.id.rv_patient_histroy_list);

        edSearch=(SearchView) findViewById(R.id.ed_search);


        recyclerView.setLayoutManager(mLinearLayoutManager);

        GetPatient();

        edSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                query=query.toLowerCase();
                List<ResGetPatient.data> filterData=new ArrayList<>();

                for (int i=0;i<resGetPatients.size();i++){

                    final String name=resGetPatients.get(i).getName().toLowerCase();
                    if (name.contains(query)){

                        filterData.add(resGetPatients.get(i));
                    }

                }


                recyclerView.setLayoutManager(new LinearLayoutManager(PatientHistoryActivtiy.this));
                customHistoryAdapter=new CustomHistoryAdapter(PatientHistoryActivtiy.this, filterData);
                recyclerView.setAdapter(customHistoryAdapter);
                customHistoryAdapter.notifyDataSetChanged();  // data set changed
                return true;

            }
        });
    }


    private void GetPatient() {

        final ProgressDialog progressDialog = ProgressDialog.show(PatientHistoryActivtiy.this, "", "Please Wait..");

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
                        customHistoryAdapter=new CustomHistoryAdapter(PatientHistoryActivtiy.this, response.body().data);

                        recyclerView.setAdapter(customHistoryAdapter);

                        customHistoryAdapter.notifyDataSetChanged();



                }

            }

            @Override
            public void onFailure(Call<ResGetPatient> call, Throwable t) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
