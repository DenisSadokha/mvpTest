package com.example.to_dolist;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import inteface.OnRecClick;
import inteface.UpdateCountCallback;
import io.realm.Realm;
import recycler_for_task.TaskData;
import unfulfilled_task_recycler.LostTaskAdapter;

public class PopupWindowTask extends PopupWindow implements OnRecClick {
    Context context;
    RecyclerView recyclerView;
    private int day, month, year;
    Realm realm;
    TextView tvStatus;
    Activity activity;
    UpdateCountCallback updateCountCallback;

    public PopupWindowTask(Context context, int day, int month, int year, Realm realm, Activity activity, UpdateCountCallback updateCountCallback) {
        this.context = context;
        this.day = day;
        this.month = month;
        this.year = year;
        this.realm = realm;
        this.activity=activity;
        this.updateCountCallback=updateCountCallback;


    }

    public PopupWindow onCreatePopup() {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.popup_layout, null);
        recyclerView = view.findViewById(R.id.recLostTask);

        tvStatus = view.findViewById(R.id.tvStatus);

      LostTaskAdapter  lostTaskAdapter = new LostTaskAdapter(realm.where(TaskData.class)
                .equalTo("day", day)
                .equalTo("month", month)
                .equalTo("year", year)
                .equalTo("check",false)
                .equalTo("lose",false)
                .findAll(), context, activity, updateCountCallback);
         lostTaskAdapter.setOnRecListener(this);
        if(lostTaskAdapter.getItemCount()<=0){
            tvStatus.setText(R.string.list_clear);
            tvStatus.setTextSize(30);
            tvStatus.setTextColor(Color.BLACK);
        } else {
            tvStatus.setText("");
        }
        recyclerView.setAdapter(lostTaskAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        return new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, 600, true);
    }
     public   int getCountTask(){
         LostTaskAdapter lostTaskAdapter = new LostTaskAdapter(realm.where(TaskData.class)
                 .equalTo("day", day)
                 .equalTo("month", month)
                 .equalTo("year", year)
                 .equalTo("check",false)
                 .equalTo("lose",false)
                 .findAll(), context, activity, updateCountCallback);
         return lostTaskAdapter.getItemCount();
     }


    @Override
    public void onRecClickListener() {

    }


    public void emptyList() {
        if (tvStatus != null) {
            tvStatus.setText(R.string.list_clear);
            tvStatus.setTextSize(30);
            tvStatus.setTextColor(Color.BLACK);
        }
    }
}
