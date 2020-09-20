package com.example.to_dolist;

import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DateFormat;
import java.util.Calendar;

import dialogs.ChooseDate;
import io.realm.Realm;
import recycler_for_task.DataUpdate;
import recycler_for_task.TaskDataHelper;

public class MainActivity extends AppCompatActivity implements ChooseDate.DateChoose {
    BottomNavigationView bnv;
    Realm realm;
    int backPressed;
    Integer day;
    public final static String LOG_TAG = "MyNewTag";
    int month;
    int year;
    Bundle bundle;
    int whatsFragment = 1;
    private Calendar calendarDate;
    boolean loadFragment(Fragment fragment, Bundle bundle) {

        if (fragment != null) {
            fragment.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_main, fragment)
                    .commit();

            return true;

        }
        return false;
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("day", day);
        outState.putInt("month", month);
        outState.putInt("year", year);
        outState.putInt("fragment", whatsFragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.init(this);
        Log.d("RELOAD", "onCreate");
        realm = Realm.getInstance(DataUpdate.getDefaultConfig());
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        day = today.monthDay;
        month = today.month;
        year = today.year;
        if (savedInstanceState != null) {
            day = savedInstanceState.getInt("day");
            month = savedInstanceState.getInt("month");
            year = savedInstanceState.getInt("year");
            whatsFragment = savedInstanceState.getInt("fragment");

        }
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        calendarDate = Calendar.getInstance();
        bnv = findViewById(R.id.navigation);
        bundle = new Bundle();
        bundle.putInt("day", day);
        bundle.putInt("month", month);
        bundle.putInt("year", year);
        switch (whatsFragment) {
            case 1:
                loadFragment(new ToDoList(), bundle);
                break;
            case 2:
                loadFragment(new UnfulfilledTask(), bundle);
                break;
            case 3:
                loadFragment(new Settings(), bundle);
                break;
        }

        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.task:
                        whatsFragment = 1;
                        fragment = new ToDoList();

                        break;
//                    case R.id.settings_frag:
//                        whatsFragment = 3;
//                        fragment = new Settings();
//                        break;
                    case R.id.unfulfilledTask:
                        whatsFragment = 2;
                        fragment = new UnfulfilledTask();
                        break;

                }
                return loadFragment(fragment, bundle);

            }
        });
    }

    public void dateStart(int day, int month, int year) {
        ChooseDate chooseDate = new ChooseDate(day, month, year);
        chooseDate.show(getSupportFragmentManager(), "pickerD");
    }

    @Override
    public void onBackPressed() {
        if(backPressed==1){
            super.onBackPressed();
            backPressed=0;
        } else if(backPressed==0){
            backPressed++;
            Toast.makeText(getApplicationContext(),"Нажмите ещё раз, чтобы выйти",Toast.LENGTH_SHORT).show();

        }


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                backPressed=0;
            }
        },2000);
    }

    @Override
    public void chooseDate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
        bundle = new Bundle();
        bundle.putInt("day", day);
        bundle.putInt("month", month);
        bundle.putInt("year", year);
        loadFragment(new ToDoList(), bundle);


    }




}
