package com.ibnsaad.thedcc.network.RetrofitNetwork;

import com.google.gson.JsonObject;
import com.ibnsaad.thedcc.model.LoginRespons;
import com.ibnsaad.thedcc.model.ProfileResponse;
import com.ibnsaad.thedcc.model.RegisterResponse;
import com.ibnsaad.thedcc.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Apis {
    @GET("api/User/{id}")
    Call<User> getUser(@Path("id") int id);

    @POST("api/Auth/register")
    Call<RegisterResponse> registerNewUser(@Body JsonObject jsonObject);

    @POST("api/Auth/login")
    Call<LoginRespons> logIn(@Body JsonObject jsonObject);

    @GET("api/Users/12")
    Call<List<String>> getSpecialists();

    @GET("api/Users")
    Call<List<User>> getUsers(@Header("Authorization") String header);

    @GET("api/Users/getPaging")
    Call<List<User>> getUsersPaging(@Header("Authorization") String header, @Query("PageNumber") int PageNumber, @Query("PageSize") int PageSize);

    @GET("/api/Users/{id}")
    Call<ProfileResponse> getProfile(@Header("Authorization") String header,@Path("id") int id);
}
