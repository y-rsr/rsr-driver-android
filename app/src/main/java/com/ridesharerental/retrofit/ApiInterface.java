package com.ridesharerental.retrofit;

import com.ridesharerental.pojo.DeleteMessage;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by user65 on 12/12/2017.
 */

public interface ApiInterface
{
    @GET("http://coreteam.casperon.com/wadyland/json/search_data")
    Call<ResponseBody> post_amenties();


    @GET("http://www.shopieasy.co.in/home/app/MobileHomeController/getHomePageDetails/")
    Call<ResponseBody> chan();



    @GET("app/driver/find_a_car")
    Call<ResponseBody> find_cars1();


    @FormUrlEncoded
    @POST("app/driver/find_a_car")
    Call<ResponseBody> find_cars(@HeaderMap HashMap<String,String> header, @FieldMap HashMap<String, String> data);


    @FormUrlEncoded
    @POST("app/driver/register")
    Call<ResponseBody> user_register(@FieldMap HashMap<String, String> data);

    @FormUrlEncoded
    @POST("app/driver/login")
    Call<ResponseBody> driver_Login(@FieldMap HashMap<String, String> data);


    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("https://graph.facebook.com/me?fields=id,name,picture,email")
    Call<ResponseBody> fb_login(@Query("access_token")String key);



    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("app/driver/profile")
    Call<ResponseBody> show_driver_profile(@HeaderMap HashMap<String,String> map);


    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("app/driver/more_filter")
    Call<ResponseBody> show_filter_data(@HeaderMap HashMap<String,String> map);


    @FormUrlEncoded
    @POST("app/driver/find_a_car")
    Call<ResponseBody> apply_filter(@HeaderMap HashMap<String,String> header,@FieldMap HashMap<String, String> data);


    @FormUrlEncoded
    @POST("app/driver/change_password")
    Call<ResponseBody> post_changPassword(@HeaderMap Map<String, String> headers, @FieldMap HashMap<String, String> data);



    @FormUrlEncoded
    @POST("app/driver/reset_password")
    Call<ResponseBody> post_ResetPassword(@FieldMap HashMap<String, String> data);




    @FormUrlEncoded
    @POST("app/driver/save_profile")
    Call<ResponseBody> post_saveProfile(@HeaderMap Map<String, String> headers, @FieldMap HashMap<String, String> data);


    @Multipart
    @POST("app/driver/save_profile")
    Call<ResponseBody> uploadImage(@HeaderMap HashMap<String, String> header, @Part MultipartBody.Part filePart);


    @Multipart
    @POST("app/driver/save_licence_image")
    Call<ResponseBody> uploadprofile_document(@HeaderMap HashMap<String, String> header, @Part MultipartBody.Part filePart);


    @Multipart
    @POST("app/driver/save_licence_image")
    Call<ResponseBody> upload_document(@HeaderMap HashMap<String, String> header, @Part MultipartBody.Part filePart);


    @FormUrlEncoded
    @POST("app/driver/car")
    Call<ResponseBody> Car_details(@FieldMap HashMap<String, String> data);


    @FormUrlEncoded
    @POST("app/driver/proceed_booking")
    Call<ResponseBody> proceed_Booking(@HeaderMap HashMap<String,String> header, @FieldMap HashMap<String, String> data);

    @FormUrlEncoded
    @POST("app/driver/pricing")
    Call<ResponseBody> pricing(@HeaderMap HashMap<String,String> header, @FieldMap HashMap<String, String> data);



    @FormUrlEncoded
    @POST("app/driver/proceed_payment")
    Call<ResponseBody> Payment(@HeaderMap HashMap<String,String> header, @FieldMap HashMap<String, String> data);


    @FormUrlEncoded
    @POST("app/driver/proceed_extend_payment")
    Call<ResponseBody> Extent_Payment(@HeaderMap HashMap<String,String> header, @FieldMap HashMap<String, String> data);


    @GET("app/driver/check_cc_status")
    Call<ResponseBody> check_cc_status(@HeaderMap HashMap<String,String> header);


    @FormUrlEncoded
    @POST("app/driver/reservations")
    Call<ResponseBody> Reservations(@HeaderMap HashMap<String,String> header, @FieldMap HashMap<String, String> data);


    @GET("app/driver/reservations")
    Call<ResponseBody> Reservations_check(@HeaderMap HashMap<String,String> header);

    @GET("app/driver/transactions")
    Call<ResponseBody> My_Transaction(@HeaderMap HashMap<String,String> header);



    @GET("app/driver/card_details")
    Call<ResponseBody> show_credit_card(@HeaderMap HashMap<String,String> header);


    @FormUrlEncoded
    @POST("app/driver/extend_pricing")
    Call<ResponseBody> extent_date(@HeaderMap HashMap<String,String> header, @FieldMap HashMap<String, String> data);


    @FormUrlEncoded
    @POST("app/driver/proceed_extend_booking")
    Call<ResponseBody> extent_proceed_booking(@HeaderMap HashMap<String,String> header, @FieldMap HashMap<String, String> data);


    @FormUrlEncoded
    @POST("app/driver/extend_details")
    Call<ResponseBody> show_extent_details(@HeaderMap HashMap<String,String> header, @FieldMap HashMap<String, String> data);


    @GET("app/driver/card_details")
    Call<ResponseBody> view_card_details(@HeaderMap HashMap<String,String> header);

    @FormUrlEncoded
    @POST("app/driver/add_card_details")
    Call<ResponseBody> add_card_details(@HeaderMap HashMap<String,String> header, @FieldMap HashMap<String, String> data);


    @GET
    public Call<ResponseBody> profilePicture(@Url String url);



    @GET("app/driver/inbox")
    Call<ResponseBody> list_inbox(@HeaderMap HashMap<String,String> header);

    @GET("app/driver/inbox_admin")
    Call<ResponseBody> inbox_admin(@HeaderMap HashMap<String,String> header);

    @GET("app/driver/inbox_direct")
    Call<ResponseBody> inbox_direct(@HeaderMap HashMap<String,String> header);

    @GET("app/driver/inbox_direct_admin")
    Call<ResponseBody> inbox_direct_admin(@HeaderMap HashMap<String,String> header);

    @FormUrlEncoded
    @POST("app/driver/admin_message")
    Call<ResponseBody> admin_message(@HeaderMap HashMap<String,String> header, @FieldMap HashMap<String, String> data);

    @FormUrlEncoded
    @POST("app/driver/direct_message")
    Call<ResponseBody> direct_message(@HeaderMap HashMap<String,String> header, @FieldMap HashMap<String, String> data);

    @FormUrlEncoded
    @POST("app/driver/direct_admin_message")
    Call<ResponseBody> direct_admin_message(@HeaderMap HashMap<String,String> header, @FieldMap HashMap<String, String> data);

    @FormUrlEncoded
    @POST("app/driver/message")
    Call<ResponseBody> show_chat_message(@HeaderMap HashMap<String,String> header, @FieldMap HashMap<String, String> data);


    @GET("app/driver/ambassador")
    Call<ResponseBody> ambasador_program(@HeaderMap HashMap<String,String> header);


    @FormUrlEncoded
    @POST("app/driver/send_message")
    Call<ResponseBody> send_message(@HeaderMap HashMap<String,String> header, @FieldMap HashMap<String, String> data);


    @FormUrlEncoded
    @POST("app/driver/send_admin_message")
    Call<ResponseBody> send_admin_message(@HeaderMap HashMap<String,String> header, @FieldMap HashMap<String, String> data);

    @Multipart
    @POST("app/driver/send_admin_message")
    Call<ResponseBody> send_admin_message_attachment(@HeaderMap HashMap<String, String> header, @Part MultipartBody.Part filePart, @PartMap HashMap<String, RequestBody> data);


    @FormUrlEncoded
    @POST("app/driver/send_direct_message")
    Call<ResponseBody> send_direct_message(@HeaderMap HashMap<String,String> header, @FieldMap HashMap<String, String> data);

    @FormUrlEncoded
    @POST("app/driver/send_direct_admin_message")
    Call<ResponseBody> send_direct_admin_message(@HeaderMap HashMap<String,String> header, @FieldMap HashMap<String, String> data);

    @Multipart
    @POST("app/driver/send_direct_admin_message")
    Call<ResponseBody> send_direct_admin_message_attachment(@HeaderMap HashMap<String, String> header, @Part MultipartBody.Part filePart, @PartMap HashMap<String, RequestBody> data);



    @FormUrlEncoded
    @POST("app/driver/start_new_direct_conversation")
    Call<ResponseBody> start_new_direct_conversation(@HeaderMap HashMap<String,String> header, @FieldMap HashMap<String, String> data);


    @FormUrlEncoded
    @POST("app/driver/delete_messages")
    Call<ResponseBody> delete_messages(@HeaderMap HashMap<String, String> header, @FieldMap HashMap<String, String> data);


    //@FormUrlEncoded
//    @POST("app/driver/delete_messages")
//    Call<ResponseBody> delete_messages(@HeaderMap HashMap<String, String> header,@Body DeleteMessage deleteMessage);

    public String delete_messages = "app/driver/delete_messages";

    @FormUrlEncoded
    @POST("app/driver/delete_admin_messages")
    Call<ResponseBody> delete_admin_messages(@HeaderMap HashMap<String, String> header, @FieldMap HashMap<String, String> data);

    @FormUrlEncoded
    @POST("app/driver/delete_direct_messages")
    Call<ResponseBody> delete_direct_messages(@HeaderMap HashMap<String, String> header, @FieldMap HashMap<String, String> data);

    @FormUrlEncoded
    @POST("app/driver/start_new_direct_admin_conversation")
    Call<ResponseBody> start_new_direct_admin_conversation(@HeaderMap HashMap<String,String> header, @FieldMap HashMap<String, String> data);


    @FormUrlEncoded
    @POST("app/driver/contact_us")
    Call<ResponseBody> save_contact_us(@HeaderMap HashMap<String,String> header, @FieldMap HashMap<String, String> data);



    @GET("app/driver/contract_history")
    Call<ResponseBody> contract_history(@HeaderMap HashMap<String,String> header);



    @FormUrlEncoded
    @POST("app/driver/wishlist")
    Call<ResponseBody> add_wish_list(@HeaderMap HashMap<String,String> header, @FieldMap HashMap<String, String> data);

    @GET("app/driver/wishlists")
    Call<ResponseBody> show_wish_list(@HeaderMap HashMap<String,String> header);



    @FormUrlEncoded
    @POST("app/driver/add_review")
    Call<ResponseBody> add_review(@HeaderMap HashMap<String,String> header, @FieldMap HashMap<String, String> data);


    @FormUrlEncoded
    @POST("app/driver/review")
    Call<ResponseBody> show_review(@HeaderMap HashMap<String,String> header, @FieldMap HashMap<String, String> data);



    @FormUrlEncoded
    @POST("app/driver/send_referral_mail")
    Call<ResponseBody> share_link(@HeaderMap HashMap<String,String> header, @FieldMap HashMap<String, String> data);



    @FormUrlEncoded
    @POST("app/driver/payment")
    Call<ResponseBody> paypal_driver_payment(@HeaderMap HashMap<String,String> header, @FieldMap HashMap<String, String> data);


    @FormUrlEncoded
    @POST("app/driver/extend_payment")
    Call<ResponseBody> extent_paypal_driver_payment(@HeaderMap HashMap<String,String> header, @FieldMap HashMap<String, String> data);


    @FormUrlEncoded
    @POST("app/driver/sent_otp")
    Call<ResponseBody> send_otp(@FieldMap HashMap<String, String> data);


    @FormUrlEncoded
    @POST("app/driver/logout")
    Call<ResponseBody> send_logout(@FieldMap HashMap<String, String> data);


    @FormUrlEncoded
    @POST("app/driver/sample_tree")
    Call<ResponseBody> genomic_tree(@HeaderMap HashMap<String,String> header,@FieldMap HashMap<String, String> data);



    @FormUrlEncoded
    @POST("app/driver/save_amb_paypal_id")
    Call<ResponseBody> update_paypal(@HeaderMap HashMap<String,String> header,@FieldMap HashMap<String, String> data);



    @FormUrlEncoded
    @POST("app/driver/tree_change")
    Call<ResponseBody> updated_tree(@HeaderMap HashMap<String,String> header,@FieldMap HashMap<String, String> data);


    @FormUrlEncoded
    @POST("app/driver/update_location")
    Call<ResponseBody> location_update(@HeaderMap HashMap<String,String> header,@FieldMap HashMap<String, String> data);


    @FormUrlEncoded
    @POST("app/driver/save_driver_claim")
    Call<ResponseBody> save_driver_clam(@FieldMap HashMap<String, String> data);


    @FormUrlEncoded
    @POST("app/driver/save_claimant_claim")
    Call<ResponseBody> save_claimant_clam(@FieldMap HashMap<String, String> data);


    @GET("app/driver/leave_review")
    Call<ResponseBody> leave_review(@HeaderMap HashMap<String,String> header);


    @GET("maps/api/place/autocomplete/json?")
    Call<ResponseBody> google_place_serach(@Query("types") String types, @Query("key") String key, @Query("input") String input);


    @GET("maps/api/place/details/json?")
    Call<ResponseBody> GetAddressFrom_LatLong_url(@Query("key") String key, @Query("placeid") String placeid);

    @Multipart
    @POST("app/driver/upload_signature_image")
    Call<ResponseBody> upload_driver_sign(@HeaderMap HashMap<String, String> header, @Part MultipartBody.Part filePart);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("app/driver/resend_email_verification")
    Call<ResponseBody> resend_email_verification(@HeaderMap HashMap<String, String> map);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("app/driver/resend_verification_message")
    Call<ResponseBody> resend_verification_message(@HeaderMap HashMap<String, String> map);

    @FormUrlEncoded
    @POST("app/driver/send_verification_message")
    Call<ResponseBody> send_verification_message(@HeaderMap HashMap<String, String> header, @FieldMap HashMap<String, String> data);

    @FormUrlEncoded
    @POST("app/driver/verify_mobile")
    Call<ResponseBody> verify_mobile(@HeaderMap HashMap<String, String> header, @FieldMap HashMap<String, String> data);
}
