package com.designslick.doctor;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.designslick.doctor.Common.Api;
import com.designslick.doctor.Common.NameAdapter;
import com.designslick.doctor.Common.Utility;
import com.designslick.doctor.Response.ResAddProfile;
import com.designslick.doctor.Response.ResGetPatient;
import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private Utility utility;

    private TextView tvAddPrescription;
    private EditText edProfileName, edProfileAge, edProfileMobile, edProfileDateofBirth, edProfileAddress,
            edProfileQualification, edProfileOccupation,edPayment,edSpecialInstruction;
    private RadioButton rdbProfileMale, rdbProfileFemale,rdbProfilePaid,rdbProfileUnpaid;
   private Button btnProfileSubmit;

    private String strProfileName, strProfileAge, strProfileSex, strProfileMobile, strProfileDateofBirth, strProfileAddress, strProfileQualification,
            strProfileOccupation,strProfilePaymentStatus, strProfileFamilyMember, strProfileAddPrescrption,strProfilePayment,
            strProfileVisitDate,strSpecialInstruction;

    private String iYear,iDay,iMonth;

    private RelativeLayout rel_signaturePad;
    private SignaturePad signaturePad;
    private Button btnOk,btnClear,btnCancel;
    private ImageView ivPrescription;

    private List<ResGetPatient.data> resGetPatients = new ArrayList<>();
    private NameAdapter nameAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        utility=new Utility(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Create Patient Profile");
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

        tvAddPrescription = (TextView) findViewById(R.id.tv_profile_addprescription);

        edProfileName = (EditText) findViewById(R.id.ed_profile_name);
        edProfileAge = (EditText) findViewById(R.id.ed_profile_age);
        edProfileMobile = (EditText) findViewById(R.id.ed_profile_mobile);
        edProfileDateofBirth = (EditText) findViewById(R.id.ed_profile_dateofbirth);
        edProfileAddress = (EditText) findViewById(R.id.ed_profile_address);
        edProfileQualification = (EditText) findViewById(R.id.ed_profile_qualification);
        edProfileOccupation = (EditText) findViewById(R.id.ed_profile_occupation);
        edPayment=(EditText)findViewById(R.id.ed_profile_payment);
        edSpecialInstruction=(EditText)findViewById(R.id.ed_profile_spinstruction);


        rdbProfileMale = (RadioButton) findViewById(R.id.rdb_profile_male);
        rdbProfileFemale = (RadioButton) findViewById(R.id.rdb_profile_female);
        rdbProfilePaid = (RadioButton) findViewById(R.id.rdb_profile_paid);
        rdbProfileUnpaid = (RadioButton) findViewById(R.id.rdb_profile_unpaid);

        ivPrescription=(ImageView)findViewById(R.id.iv_profile_prescription);


        rel_signaturePad=(RelativeLayout)findViewById(R.id.rel_signature_pad);

        signaturePad=(SignaturePad)findViewById(R.id.signature_pad);

        btnOk=(Button)findViewById(R.id.custom_ok);
        btnClear=(Button)findViewById(R.id.custom_clear);
        btnCancel=(Button)findViewById(R.id.custom_cancel);

        rel_signaturePad.setVisibility(View.GONE);



        GetPatient();

        Calendar calendar=Calendar.getInstance();
        strProfileVisitDate=calendar.get(Calendar.DAY_OF_MONTH)+"-"+calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.YEAR);

        btnProfileSubmit = (Button) findViewById(R.id.btn_profile_submit);

        String[] list = getResources().getStringArray(R.array.strProfileFamilyCharacter);

       // spProfileFamilyMember.setAdapter(new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.simple_list_item_1, list));

        btnProfileSubmit.setOnClickListener(this);
        edProfileDateofBirth.setOnClickListener(this);
        tvAddPrescription.setOnClickListener(this);

        btnOk.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

    }

    private void GetPatient() {

        if(Utility.isOnline()){
            final ProgressDialog progressDialog = ProgressDialog.show(ProfileActivity.this, "", "Please Wait..");
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Utility.url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(Utility.addInterCeptor())
                        .build();

                Api api = retrofit.create(Api.class);

                Call<ResGetPatient> getPatient = api.getPatient();

            getPatient.enqueue(new Callback<ResGetPatient>() {
                    @Override
                    public void onResponse(Call<ResGetPatient> call, Response<ResGetPatient> response) {
                       if(progressDialog.isShowing())
                           progressDialog.dismiss();
                        if (response.body().errorcode==1) {
                            resGetPatients = response.body().data;
                            nameAdapter=new NameAdapter(ProfileActivity.this, android.R.layout.simple_list_item_1, resGetPatients);
                            //edProfileFamilyRefernce.setAdapter(nameAdapter);

                        }
                        else {
                            //Utility.showalert(ProfileActivity.this,getString(R.string.msgResponseNull));
                        }

                    }

                    @Override
                    public void onFailure(Call<ResGetPatient> call, Throwable t) {

                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
                        t.printStackTrace();
                    }
                });

        }
        else {
            Utility.showalert(ProfileActivity.this,getString(R.string.msgNotInternet));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_profile_submit:

                AddPatientProfile();
                break;
            case R.id.ed_profile_dateofbirth:

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
                        edProfileDateofBirth.setText(iDay + "/" + iMonth + "/" + year);

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)).show();

                break;

            case R.id.tv_profile_addprescription:
                signaturePad.clear();
                rel_signaturePad.setVisibility(View.VISIBLE);
                break;


            case R.id.custom_ok:

                rel_signaturePad.setVisibility(View.GONE);
                strProfileAddPrescrption=getOutputFile().getPath();

                Bitmap bmp= BitmapFactory.decodeFile(strProfileAddPrescrption);
                ivPrescription.setImageBitmap(bmp);

                break;
            case R.id.custom_clear:

                signaturePad.clear();

                break;
            case R.id.custom_cancel:
                signaturePad.clear();
                rel_signaturePad.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    private void AddPatientProfile()
    {
        if(Utility.isOnline()){



            if (CheckData()) {
                final ProgressDialog progressDialog = ProgressDialog.show(ProfileActivity.this, "", "Please Wait..");
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Utility.url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(Utility.addInterCeptor())
                        .build();
                MultipartBody.Part part=null;
                if(Utility.isNotNull(strProfileAddPrescrption)) {
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), new File(strProfileAddPrescrption));
                    part = MultipartBody.Part.createFormData("prescription_file", strProfileAddPrescrption, requestBody);
                }
                Api api = retrofit.create(Api.class);

                RequestBody reqName=RequestBody.create(MediaType.parse("text/parse"),strProfileName);
                RequestBody reqDOB=RequestBody.create(MediaType.parse("text/parse"),strProfileDateofBirth);
                RequestBody reqAge=RequestBody.create(MediaType.parse("text/parse"),strProfileAge);
                RequestBody reqSex=RequestBody.create(MediaType.parse("text/parse"),strProfileSex);
                RequestBody reqMobile=RequestBody.create(MediaType.parse("text/parse"),strProfileMobile);
                RequestBody reqAddress=RequestBody.create(MediaType.parse("text/parse"),strProfileAddress);
                RequestBody reqOccupation=RequestBody.create(MediaType.parse("text/parse"),strProfileOccupation);
                RequestBody reqQualification=RequestBody.create(MediaType.parse("text/parse"),strProfileQualification);
                RequestBody reqpayment=RequestBody.create(MediaType.parse("text/parse"),strProfilePayment);
                RequestBody reqpaymentstatus=RequestBody.create(MediaType.parse("text/parse"),strProfilePaymentStatus);
                RequestBody reqvisitdate=RequestBody.create(MediaType.parse("text/parse"),strProfileVisitDate);
                RequestBody refamilyreference=RequestBody.create(MediaType.parse("text/parse"),"0");
                RequestBody reqSpecialInstruction=RequestBody.create(MediaType.parse("text/parse"),strSpecialInstruction);

                Call<ResAddProfile> addPatient = api.addPatient(reqName,reqDOB,reqAge,reqSex ,reqMobile,reqAddress
                        , reqQualification, reqOccupation ,reqpayment
                        ,reqpaymentstatus,reqvisitdate,refamilyreference,reqSpecialInstruction);

                addPatient.enqueue(new Callback<ResAddProfile>() {
                    @Override
                    public void onResponse(Call<ResAddProfile> call, Response<ResAddProfile> response) {

                        if(progressDialog.isShowing())
                            progressDialog.dismiss();

                        if (response.body().errorcode==1) {
                            Utility.showalert(ProfileActivity.this, response.body().message);
                            AlertDialog.Builder alert=new AlertDialog.Builder(ProfileActivity.this).
                                                            setMessage(response.body().message).
                                                            setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    dialog.dismiss();
                                                                    Intent inDashboard=new Intent(ProfileActivity.this,DashboardActivity.class);
                                                                    startActivity(inDashboard);
                                                                    finish();
                                                                }
                                                            });
                            alert.show();
                        }
                        else {
                            Utility.showalert(ProfileActivity.this, getString(R.string.msgResponseNull));
                        }
                    }


                    @Override
                    public void onFailure(Call<ResAddProfile> call, Throwable t) {
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
                        t.printStackTrace();
                    }
                });
            }
        }
        else {
            Utility.showalert(ProfileActivity.this,getString(R.string.msgNotInternet));
        }
    }

    private Uri getOutputFile(){

        File mediaStorageDir=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"Doctor");

        if(!mediaStorageDir.exists()){
            if(!mediaStorageDir.mkdirs()){
                mediaStorageDir.mkdirs();
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        File mediafile=new File(mediaStorageDir.getPath()+File.separator+"Img_"+timeStamp+".jpg");
        try {
            mediafile.createNewFile();

            Bitmap bitmap = signaturePad.getSignatureBitmap();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmaparray = bos.toByteArray();

            FileOutputStream fileOutputStream = new FileOutputStream(mediafile);
            fileOutputStream.write(bitmaparray);
            fileOutputStream.flush();
            fileOutputStream.close();

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return Uri.fromFile(mediafile);
    }
    public boolean CheckData() {

        strProfileName = edProfileName.getText().toString().trim();
        strProfileAge = edProfileAge.getText().toString().trim();
        if (rdbProfileMale.isChecked()) {
            strProfileSex = "1";
        }
        if (rdbProfileFemale.isChecked()) {
            strProfileSex = "2";
        }
        if (rdbProfilePaid.isChecked()) {
            strProfilePaymentStatus = "Paid";
        }
        if (rdbProfileUnpaid.isChecked()) {
            strProfilePaymentStatus = "Unpaid";
        }
        strProfileMobile = edProfileMobile.getText().toString().trim();
        strProfileDateofBirth = iYear+"-"+iMonth+"-"+iDay;
        strProfileAddress = edProfileAddress.getText().toString().trim();
        strProfileQualification = edProfileQualification.getText().toString().trim();
        strProfileOccupation = edProfileOccupation.getText().toString().trim();
      //  strProfileFamilyReference =""+resGetPatients.indexOf(edProfileFamilyRefernce.getText().toString());
        //strProfileFamilyMember = spProfileFamilyMember.getSelectedItem().toString();
        strProfilePayment=edPayment.getText().toString().trim();
        strSpecialInstruction=edSpecialInstruction.getText().toString().trim();

        if (!Utility.isNotNull(strProfileName)) {
            Utility.showalert(this, getString(R.string.msgProfileName));
            return false;
        } else if (!Utility.isNotNull(strProfileAge)) {
            Utility.showalert(this, getString(R.string.msgProfileAge));
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if(rel_signaturePad.getVisibility()==View.VISIBLE){
            rel_signaturePad.setVisibility(View.GONE);
        }
        else {
            super.onBackPressed();
        }
    }
}
