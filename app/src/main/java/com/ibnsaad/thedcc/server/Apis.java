package com.ibnsaad.thedcc.server;

import com.google.gson.JsonObject;
import com.ibnsaad.thedcc.model.BodyAreasResponse;
import com.ibnsaad.thedcc.model.BulletinResponse;
import com.ibnsaad.thedcc.model.DrugsResponse;
import com.ibnsaad.thedcc.model.LoginResponse;
import com.ibnsaad.thedcc.model.Message;
import com.ibnsaad.thedcc.model.Photo;
import com.ibnsaad.thedcc.model.ProfileResponse;
import com.ibnsaad.thedcc.model.RegisterResponse;
import com.ibnsaad.thedcc.model.ResponseMessagesWithId;
import com.ibnsaad.thedcc.model.SymptomByIdResponse;
import com.ibnsaad.thedcc.model.User;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Apis {

    @GET("api/User/{id}")
    Call<User> getUser(@Path("id") String id);

    @POST("api/Auth/register")
    Call<RegisterResponse> registerNewUser(@Body JsonObject jsonObject);

    @POST("api/Auth/login")
    Call<LoginResponse> logIn(@Body JsonObject jsonObject);

    @GET("api/Users/12")
    Call<List<String>> getSpecialists();

    @GET("api/Users")
    Call<List<User>> getUsers(@Header("Authorization") String header);

    @GET("api/Users/getPaging")
    Call<List<User>> getUsersPaging(@Header("Authorization") String header,
                                    @Query("PageNumber") int PageNumber,
                                    @Query("PageSize") int PageSize,
                                    @Query("OrderBy") String OrderBy,
                                    @Query("Specialization") String Specialization

    );

    @GET("api/Users/getPaging")
    Call<List<User>> getUsersPagingPatient(@Header("Authorization") String header,
                                    @Query("PageNumber") int PageNumber,
                                    @Query("PageSize") int PageSize,
                                    @Query("OrderBy") String OrderBy

    );

    @GET("api/Users/{id}")
    Call<ProfileResponse> getProfile(@Header("Authorization") String header, @Path("id") String id);

    @PUT("api/Users/{id}")
    Call<ProfileResponse> updateProfile(@Header("Authorization") String header, @Path("id") String id, @Body JsonObject body);

    @Multipart
    @POST("api/users/{userId}/photos")
    Call<Photo> uploadImage(@Header("Authorization") String header,
                            @Path("userId") String userId,
                            @Part("Url") String Url,
                            @Part("Description") String Description,
                            @Part("DateAdded") String DateAdded,
                            @Part("PublicId") String PublicId,
                            @Part MultipartBody.Part image);


    @DELETE("api/users/{userId}/photos/{id}")
    Call<Void> deleteImage(@Header("Authorization") String header,
                           @Path("userId") String userId,
                           @Path("id") String id);

    @POST("api/users/{userId}/photos/{id}/setMain")
    Call<Void> setMainImage(@Header("Authorization") String header,
                            @Path("userId") String userId,
                            @Path("id") String id);

    @GET("api/Diagnosis/getspecil")
    Call<List<String>> getSpecializations(@Header("Authorization") String header);


    //for message with user id and message id
    @GET("api/users/{userId}/Messages/{id}")
    Call<ResponseMessagesWithId> getMessages(@Path("userId") String userId,
                                             @Path("id") int idMessage);

    //for sent message with content
    @POST("api/users/{userId}/Messages")
    Call<Message> senMessage(@Header("Authorization") String header, @Path("userId") String userId,
                             @Body Message message);

    @GET("api/users/{userId}/Messages/thread/{recipientId}")
    Call<List<Message>> getAllMessages(@Header("Authorization") String header, @Path("userId") String userId,
                                       @Path("recipientId") String recipientId);

    @POST("api/Users/{id}/like/{recipientId}")
    Call<Void> like(@Header("Authorization") String header
            , @Path("id") String userId, @Path("recipientId") String recipientId);


    // diagonsis

    @GET("api/Diagnosis/getBodyAreas")
    Call<List<BodyAreasResponse>> getBodyAreas(@Header("Authorization") String header);

    @GET("api/Diagnosis/GetAllDrugs")
    Call<List<DrugsResponse>> getAllDrugs(@Header("Authorization") String header);

    @GET("api/Diagnosis/GetDrugById")
    Call<DrugsResponse> getDrugById(@Header("Authorization") String header, @Query("drugId") int drugId);

    @GET("api/Diagnosis/GetDrugBySyptomId")
    Call<List<DrugsResponse>> getDrugBySyptomId(@Header("Authorization") String header, @Query("symptomId") int symptomId);

    @GET("api/Diagnosis/getAllBulletin")
    Call<List<BulletinResponse>> getAllBulletin(@Header("Authorization") String header);

    @GET("api/Diagnosis/GetSympotByBodyAreasId")
    Call<List<SymptomByIdResponse>> getSympotByBodyAreasId(@Header("Authorization") String header, @Query("BodyId") int BodyId);


    @GET("api/users/{userId}/Messages")
    Call<List<Message>> getChatsHeads(@Header("Authorization") String header, @Path("userId") String userId);


}
