package com.example.mvpHistory.Model;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetCall {
    Callback<JsonObject> historyModel;


    public  void attach(Callback<JsonObject> historyModel){
        this.historyModel=historyModel;
    }

    public void makeCall(String state){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://chroniclingamerica.loc.gov/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SetUpCall setUpCall = retrofit.create(SetUpCall.class);
        Call<JsonObject> call = setUpCall.call(state,"json");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                historyModel.onResponse(call,  response);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                historyModel.onFailure(call,t);


            }
        });


    }
}
