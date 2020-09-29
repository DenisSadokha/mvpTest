package com.example.retrofitTest;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LinkForm {


    @GET("/api/v1/dicservice.json/lookup?")
    Call<JsonObject> word(@Query("key") String key,
                          @Query("lang") String lang,
                          @Query("text") String text);


}


