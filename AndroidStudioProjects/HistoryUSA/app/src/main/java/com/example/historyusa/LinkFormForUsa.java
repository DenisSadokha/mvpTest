package com.example.historyusa;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LinkFormForUsa {
    @GET("/search/titles/results/")
    Call<JsonObject> info(@Query("terms") String state,
                          @Query("format") String format);




}
