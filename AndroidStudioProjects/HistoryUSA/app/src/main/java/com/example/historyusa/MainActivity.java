package com.example.historyusa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import okhttp3.Call;
import okhttp3.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements NetworkCall.CallBackNet {
    TextView tvInfoCity;
    Button bEnter;
    EditText etNameCity, etDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvInfoCity=findViewById(R.id.tvCityInfo);
        bEnter=findViewById(R.id.bMainFind);
        etNameCity = findViewById(R.id.etCity);
        etDate = findViewById(R.id.etDate);
        bEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkCall networkCall = new NetworkCall();
                networkCall.setContext(MainActivity.this);
                networkCall.makeConnectForCountRes(etNameCity.getText().toString());
            }
        });
    }

    @Override
    public void getRequest(String s) {
        tvInfoCity.setText(s);
    }

}