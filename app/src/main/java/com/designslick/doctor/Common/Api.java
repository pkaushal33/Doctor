package com.designslick.doctor.Common;

import com.designslick.doctor.Response.ResAddProfile;
import com.designslick.doctor.Response.ResCommon;
import com.designslick.doctor.Response.ResGetPatient;
import com.designslick.doctor.Response.ResGetPatientDetails;
import com.designslick.doctor.Response.ResGetUnpaidPayments;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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
    Call<ResAddProfile> addPatient(@Part("name") RequestBody name, @Part("dob") RequestBody dob,
                                   @Part("age") RequestBody age, @Part("gender") RequestBody gender, @Part("mobile") RequestBody mobile,
                                   @Part("address") RequestBody address, @Part("qualification") RequestBody qualification, @Part("occupation") RequestBody occupation,
                                   @Part("payable_amount") RequestBody payable_amount, @Part("payment_status") RequestBody payment_status,
                                   @Part("visit_date") RequestBody visit_date, @Part("family_code") RequestBody family_code,
                                   @Part("special_instruction") RequestBody special_instruction);



    @Multipart
    @POST("api.php?request=addPrescription")
    Call<ResAddProfile> addPatientVisit(@Part("patient_id") RequestBody id,@Part("visit_date") RequestBody visitedDate,
                                        @Part("payable_amount") RequestBody amount,@Part("payment_status") RequestBody payment_status,@Part("special_instruction") RequestBody special_instruction,
                                 @Part MultipartBody.Part report,@Part MultipartBody.Part prescription);

    @GET("api.php?request=getPatientList")
    Call<ResGetPatient> getPatient();

    @GET("api.php?request=getUnpaidPayments")
    Call<ResGetUnpaidPayments> getUnpaidPayments();

    @GET("api.php?request=getPatientHistory")
    Call<ResGetPatientDetails> getPatientHistory(@Query("id") String id);

    @FormUrlEncoded
    @POST("api.php?request=updatePayment")
    Call<ResCommon> updatePayment(@Field("id") String id);

    @FormUrlEncoded
    @POST("api.php?request=deletePrescription")
    Call<ResCommon> deletePatientVisit(@Field("id") String id);

    @FormUrlEncoded
    @POST("api.php?request=deletePatient")
    Call<ResCommon> deletePatient(@Field("id") String id);
}