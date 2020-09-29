package com.example.mvpHistory.Model;

import com.example.mvpHistory.HistoryContract;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryModel implements HistoryContract.Model, Callback<JsonObject> {
    String result;
    HistoryContract.Presenter presenter;

    public HistoryModel(HistoryContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void loadCount(String text) {
        NetCall netCall = new NetCall();
        netCall.attach(this);
        netCall.makeCall(text);

    }


    @Override
    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
        JsonObject jsonObject = response.body();
        String s = jsonObject.get("totalItems").toString();
        presenter.onChange(s,1);


    }

    @Override
    public void onFailure(Call<JsonObject> call, Throwable t) {
        presenter.onChange(null, 0);

    }
}
