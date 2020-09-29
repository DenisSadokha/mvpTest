package com.example.historyusa;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mvpHistory.View.SearchActivity;
import com.example.retrofitTest.MainRetrof;

public class FirstActivity extends AppCompatActivity  {
    Button bRetrof, bHist, bMvp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity);
      bRetrof=findViewById(R.id.bRetrofit);
      bHist= findViewById(R.id.bHistoryUSA);
      bMvp=findViewById(R.id.bMvp);
      bMvp.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(FirstActivity.this, SearchActivity.class);
              startActivity(intent);
          }
      });
      bHist.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(FirstActivity.this,MainActivity.class);
              startActivity(intent);
          }
      });
      bRetrof.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(FirstActivity.this, MainRetrof.class);
              startActivity(intent);
          }
      });
    }


}
