package com.example.mvpHistory.View;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.historyusa.R;
import com.example.mvpHistory.HistoryContract;
import com.example.mvpHistory.Presenter.HistoryPresenter;

public class SearchActivity extends AppCompatActivity implements HistoryContract.View {
    Button bSearch;
    EditText etSearch;
    TextView tvSearchRes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        final HistoryPresenter presenter = new HistoryPresenter(this);
        bSearch=findViewById(R.id.bSearch);
        tvSearchRes=findViewById(R.id.tvSearchRes);
        etSearch=findViewById(R.id.etSearch);
        bSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onButtonWasClicked(etSearch.getText().toString());


            }
        });

    }

    @Override
    public void showResult(String result) {
        tvSearchRes.setText(result);

    }

    @Override
    public void showSuccess() {
        Toast.makeText(this,"success",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showError() {
        Toast.makeText(this,"error",Toast.LENGTH_SHORT).show();

    }
}
