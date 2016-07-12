package com.designslick.doctor;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.designslick.doctor.Common.Api;
import com.designslick.doctor.Common.CustomHistoryAdapter;
import com.designslick.doctor.Common.Utility;
import com.designslick.doctor.Response.ResGetPatient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kaushalpatel on 6/16/16.
 */
public class PatientHistoryActivtiy extends AppCompatActivity implements View.OnClickListener{

    private Utility utility;

    private RecyclerView recyclerView;
    private CustomHistoryAdapter customHistoryAdapter;

    private List<ResGetPatient.data> resGetPatients=new ArrayList<>();
    private List<ResGetPatient.data> searchData=new ArrayList<>();

    private LinearLayoutManager mLinearLayoutManager;
    private EditText edFromDate,edToDate;
    private ImageView ivSearch;

    private SearchView edSearch;
    private String strFromDate,strToDate,iYear,iDay,iMonth;

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
        edFromDate=(EditText)findViewById(R.id.ed_histroy_fromdate);
        edToDate=(EditText)findViewById(R.id.ed_histroy_todate);

        ivSearch=(ImageView)findViewById(R.id.iv_history_search);

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
                    final String mobile=resGetPatients.get(i).getMobile().toString();
                    if (name.contains(query) || mobile.contains(query)){

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

        edFromDate.setOnClickListener(this);
        edToDate.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.ed_histroy_fromdate:

                Calendar calendar = Calendar.getInstance();


                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        iYear=String.valueOf(year);
                        if((monthOfYear+1)<10){
                            iMonth="0"+String.valueOf(monthOfYear+1);
                        }else {
                            iMonth=String.valueOf(monthOfYear+1);
                        }
                        if(dayOfMonth<10){
                            iDay="0"+String.valueOf(dayOfMonth);
                        }
                        else {
                            iDay=String.valueOf(dayOfMonth);
                        }
                        edFromDate.setText(iDay + "/" + iMonth + "/" + year);

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();


                break;

            case R.id.ed_histroy_todate:

                Calendar calendar1 = Calendar.getInstance();


                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        iYear=String.valueOf(year);
                        if((monthOfYear+1)<10){
                            iMonth="0"+String.valueOf(monthOfYear+1);
                        }else {
                            iMonth=String.valueOf(monthOfYear+1);
                        }
                        if(dayOfMonth<10){
                            iDay="0"+String.valueOf(dayOfMonth);
                        }
                        else {
                            iDay=String.valueOf(dayOfMonth);
                        }
                        edToDate.setText(iDay + "/" + iMonth + "/" + year);

                    }
                }, calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH)).show();


                break;

            case R.id.iv_history_search:

                strFromDate=edFromDate.getText().toString().trim();
                strToDate=edToDate.getText().toString().trim();

                if(!Utility.isNotNull(strFromDate)){
                    Utility.showalert(PatientHistoryActivtiy.this,getString(R.string.strNOFromDate));
                } else if(!Utility.isNotNull(strToDate)){
                    Utility.showalert(PatientHistoryActivtiy.this,getString(R.string.strNOToDate));
                } else {

                    searchData.clear();
                    for (int i=0;i<resGetPatients.size();i++){
                        Date FromDate=Utility.stringToDate(strFromDate);
                        Date ToDate=Utility.stringToDate(strToDate);
                        Date dbDate=Utility.timezoneToDate(resGetPatients.get(i).created_date);

                        if(FromDate.before(dbDate) && ToDate.before(dbDate)){
                            searchData.add(resGetPatients.get(i));
                        }
                    }

                    recyclerView.setAdapter(new CustomHistoryAdapter(this,searchData));
                    customHistoryAdapter.notifyDataSetChanged();

                }
                break;

        }
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
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
