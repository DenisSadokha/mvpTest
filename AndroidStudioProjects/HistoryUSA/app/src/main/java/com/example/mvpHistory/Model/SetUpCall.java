package com.example.mvpHistory.Model;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SetUpCall {
    @GET("/search/titles/results/")
    Call<JsonObject> call(@Query("terms") String sate,
                          @Query("format") String format);


}
