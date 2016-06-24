package com.designslick.doctor.Common;

import com.designslick.doctor.Response.ResAddProfile;
import com.designslick.doctor.Response.ResGetPatient;
import com.designslick.doctor.Response.ResGetPatientDetails;
import com.designslick.doctor.Response.ResGetUnpaidPayments;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by kaushalpatel on 6/1/16.
 */
public interface Api {

    @Multipart
    @POST("api.php?request=addPatient")
    Call<ResAddProfile> addPatient(@Part("name") String name, @Part("dob") String dob,
                                   @Part("age") String age, @Part("gender") String gender, @Part("mobile") String mobile,
                                   @Part("address") String address, @Part("qualification") String qualification, @Part("occupation") String occupation,
                                   @Part("family_code") String family_code,@Part("payable_amount") String payable_amount,@Part("payment_status") String payment_status,
                                   @Part("visit_date") String visit_date,@Part MultipartBody.Part report_file,@Part MultipartBody.Part prescription_file);



    @Multipart
    @POST("api.php?request=addPrescription")
    Call<ResAddProfile> addPatientVisit(@Part("patient_id") String name,
                                 @Part("payable_amount") String amount,@Part("payment_status") String payment_status,
                                 @Part MultipartBody.Part report,@Part MultipartBody.Part prescription);

    @GET("api.php?request=getPatientList")
    Call<ResGetPatient> getPatient();

    @GET("api.php?request=getUnpaidPayments")
    Call<ResGetUnpaidPayments> getUnpaidPayments();

    @GET("api.php?request=getPatientHistory")
    Call<ResGetPatientDetails> getPatientHistory(@Query("id") String id);
}