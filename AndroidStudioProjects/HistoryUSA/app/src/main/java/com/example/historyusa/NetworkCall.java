package com.example.historyusa;

import android.util.JsonToken;

import com.google.gson.JsonObject;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkCall {
    MainActivity mainActivity;

    public interface CallBackNet {
        void getRequest(String res);
    }

    public void setContext(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void makeConnectForCountRes(String state) {
                Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://chroniclingamerica.loc.gov/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LinkFormForUsa linkFormForUsa = retrofit.create(LinkFormForUsa.class);
        Call<JsonObject> call = linkFormForUsa.info(state, "json");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObject = response.body();
                String s = jsonObject.get("items").toString();
                CallBackNet callBackNet =(CallBackNet) mainActivity;
                callBackNet.getRequest(s);


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


    }
}
