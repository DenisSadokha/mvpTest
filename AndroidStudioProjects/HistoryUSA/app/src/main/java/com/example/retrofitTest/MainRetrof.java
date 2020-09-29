package com.example.retrofitTest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.historyusa.R;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainRetrof extends AppCompatActivity {
    private Button bFind, bWiki;
    private TextView tvResponse;
    private EditText etWord;
    private final String TAG = MainRetrof.class.getCanonicalName();
    private final String KEY = "dict.1.1.20200913T154503Z.c2caccc791a04bb3.8e89fa3beacb4c7e0daa0ae9085d42366847a059";
    private final String LANG = "ru-ru";
    private Retrofit retrofit;
    LinkForm linkForml;
    private Retrofit retrofit2;
    private final String URL = "https://dictionary.yandex.net/";


    private String word;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retrof_test);
        bFind = findViewById(R.id.bRetFind);
        bWiki= findViewById(R.id.bRetWiki);
        tvResponse = findViewById(R.id.tvRetResponse);
        etWord = findViewById(R.id.etRetEnter);
        bFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                word = etWord.getText().toString();
                retrofit = new Retrofit.Builder()
                        .baseUrl(URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                linkForml = retrofit.create(LinkForm.class);
                try {


                    Call<JsonObject> request = linkForml.word(KEY, LANG, word);
                    request.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            JsonObject res = response.body();
                            System.out.println(response.body());
                            Log.d(TAG, " " + response.code());

                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Log.d(TAG, "error");

                        }
                    });

                } catch (RuntimeException e) {
                    e.printStackTrace();
                    Log.d("NEWTAG", e.getMessage());
                }
            }


        });
        bWiki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://en.wikipedia.org/")
                        .build();
                ReqForWiki reqForWiki = retrofit.create(ReqForWiki.class);
                try {



                Call<Response> call = reqForWiki.call(etWord.getText().toString());
               call.enqueue(new Callback<Response>() {
                   @Override
                   public void onResponse(Call<Response> call, Response<Response> response) {
                       Log.d("WIKI", "responce  "+ response.code());
                       tvResponse.setText(response.code());
                   }

                   @Override
                   public void onFailure(Call<Response> call, Throwable t) {
                       Log.d("WIKI", "fail");


                   }
               });

                } catch (Exception e){
                    System.out.println( e.getMessage());
                }
            }
        });


    }
}
