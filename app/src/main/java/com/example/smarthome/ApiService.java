package com.example.smarthome;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {
    @FormUrlEncoded
    @POST("user")
    Call<Void> configureDevice(@Field("userId") String userId);
}