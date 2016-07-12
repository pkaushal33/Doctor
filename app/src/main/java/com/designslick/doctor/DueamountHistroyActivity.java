package com.designslick.doctor;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.designslick.doctor.Common.Api;
import com.designslick.doctor.Common.CustomDueAdapter;
import com.designslick.doctor.Common.CustomDueHistoryAdapter;
import com.designslick.doctor.Common.DueAmount;
import com.designslick.doctor.Common.Utility;
import com.designslick.doctor.Response.ResCommon;
import com.designslick.doctor.Response.ResGetUnpaidPayments;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DueamountHistroyActivity extends AppCompatActivity {

    private Utility utility;
    private RecyclerView recyclerView;
    private List<ResGetUnpaidPayments.data> dueAmounts;
    private List<ResGetUnpaidPayments.data.unpaid> dueAmountsUnpaid;
    private int Id,DeleteCount=0;
    private Button btnSubmit;
    private String strDeleteId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dueamount);

        Id=getIntent().getIntExtra("PatientId",0);

        utility=new Utility(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Due Amount");
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP|ActionBar.DISPLAY_SHOW_TITLE);
            toolbar.setTitleTextColor(Color.rgb(128,206,180));

        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView=(RecyclerView)findViewById(R.id.rv_dueamount_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnSubmit=(Button)findViewById(R.id.btn_dueamount_submit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i=0;i<dueAmountsUnpaid.size();i++){
                    Log.d("Checked",dueAmountsUnpaid.get(i).getChecked()+"");
                    if(dueAmountsUnpaid.get(i).getChecked()){
                        strDeleteId=dueAmounts.get(Id).unpaid.get(i).getId()+",";
                        DeleteCount++;
                    }
                }

                if(DeleteCount>0){

                        strDeleteId=strDeleteId.substring(0,strDeleteId.lastIndexOf(","));

                    updatePayment();
                }else {
                    Utility.showalert(DueamountHistroyActivity.this,"Please atleast one select for payment.");
                }
            }
        });

        getUnpaidPatientList();

    }

    private void getUnpaidPatientList() {

        final ProgressDialog progressDialog = ProgressDialog.show(DueamountHistroyActivity.this, "", "Please Wait..");

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Utility.url).
                addConverterFactory(GsonConverterFactory.create()).
                client(Utility.addInterCeptor()).
                build();

        Api api = retrofit.create(Api.class);

        Call<ResGetUnpaidPayments> call = api.getUnpaidPayments();

        call.enqueue(new Callback<ResGetUnpaidPayments>() {
            @Override
            public void onResponse(Call<ResGetUnpaidPayments> call, Response<ResGetUnpaidPayments> response) {

                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

                if(response.body().getErrorcode()==1){

                    dueAmounts=response.body().data;
                    dueAmountsUnpaid=response.body().data.get(Id).unpaid;

                    recyclerView.setAdapter(new CustomDueHistoryAdapter(DueamountHistroyActivity.this,response.body().data,response.body().data.get(Id).unpaid,Id));

                }else {
                    Utility.showalert(DueamountHistroyActivity.this,response.body().getMessage());
                }

            }

            @Override
            public void onFailure(Call<ResGetUnpaidPayments> call, Throwable t) {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        });



    }

    private void updatePayment() {

        final ProgressDialog progressDialog = ProgressDialog.show(DueamountHistroyActivity.this, "", "Please Wait..");

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Utility.url).
                addConverterFactory(GsonConverterFactory.create()).
                client(Utility.addInterCeptor()).
                build();

        Api api = retrofit.create(Api.class);

        Call<ResCommon> call = api.updatePayment(strDeleteId);

        call.enqueue(new Callback<ResCommon>() {
            @Override
            public void onResponse(Call<ResCommon> call, Response<ResCommon> response) {

                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

                Log.d("Response",response.body().getErrorcode()+"");

                if(response.body().getErrorcode()==1){

                    AlertDialog.Builder alertDialog=new AlertDialog.Builder(DueamountHistroyActivity.this)
                            .setMessage(response.body().getMessage())
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent inDueAmount=new Intent(DueamountHistroyActivity.this,DueamountActivity.class);
                                    startActivity(inDueAmount);
                                    finish();
                                }
                            });
                alertDialog.show();

                }else {
                    Utility.showalert(DueamountHistroyActivity.this,response.body().getMessage());
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
    public void onBackPressed() {
        super.onBackPressed();
    }
}
