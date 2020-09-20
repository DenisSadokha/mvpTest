package com.example.to_dolist;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import dialogs.ChooseDate;
import dialogs.ChooseNorif;
import dialogs.TimePickerEnd;
import dialogs.TimePickerEnd.TimeEnd;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import dialogs.TimePickerFrag;
import inteface.UpdateCountCallback;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.internal.operators.completable.CompletableDisposeOn;
import io.realm.Realm;
import notfication.AlertReceiver;
import recycler_for_task.DataUpdate;
import recycler_for_task.TaskDataHelper;

@SuppressLint("Registered")
public class CreateTask extends AppCompatActivity implements View.OnClickListener, TimePickerFrag.EditTimeStart, TimeEnd,
        CompoundButton.OnCheckedChangeListener, ChooseNorif.Notif, ChooseDate.DateChoose {
    ViewGroup layoutTime;
    EditText etSetTime;
    Toolbar toolbar;
    EditText etTask;
    Switch sCheckNot, sCheckTime;
    Realm realm;
    TextView tvTimeStart, tvTimeEnd, tvStatusNotif;
    Button bTimeStart, bTimeEnd, bSetNotif;
    private Integer timeStartHour = 13;
    private Integer timeStartMinute = 0;
    private Integer mTimeEndHour = 14;
    private Integer timeEndMinute = 0;
    private boolean isCheckTime=true;
    Integer day, month, year;
    Calendar calendarStart, calendarEnd, nCalendarBeforeStart, dateCalendar;
    boolean checkNotif;
    ViewGroup viewGroup;
    TextView dateTask;
    private Integer notifBeforeStart = 5;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.task_add, menu);
        return true;
    }

    private void addTask(String task, Integer timeStartHour, Integer timeStartMinute,
                         Integer timeEndHour, Integer timeEndMinute, Integer day, Integer month, Integer year) {
        int id = TaskDataHelper.incrementId(realm);

        if (checkNotif) {

            startAlarm(nCalendarBeforeStart, id, notifBeforeStart, day, month, year);

        } else {
            cancelAlarm(id);
        }
        TaskDataHelper.add(realm, task, timeStartHour.toString(), timeStartMinute.toString(), timeEndHour.toString(),
                timeEndMinute.toString(), checkNotif, notifBeforeStart.toString(), day, month, year, isCheckTime, id);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String task;

        switch (item.getItemId()) {
            case R.id.save:

                task = etTask.getText().toString().trim();
                if (task.length() != 0) {
                    addTask(task, timeStartHour, timeStartMinute, mTimeEndHour, timeEndMinute, day, month, year);
                    finish();
                } else Toast.makeText(CreateTask.this, "Введите текст", Toast.LENGTH_SHORT).show();

                break;

            default:
                return super.onOptionsItemSelected(item);

        }
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendarStart = Calendar.getInstance();
        calendarEnd = Calendar.getInstance();
        dateCalendar=Calendar.getInstance();
        nCalendarBeforeStart = Calendar.getInstance();

        Bundle b = getIntent().getExtras();
        if (b != null) {
            day = b.getInt("day");
            month = b.getInt("month");
            year = b.getInt("year");
        }


        setContentView(R.layout.create_task);
        // new function maybe update or delete in future
        layoutTime =  findViewById(R.id.layout_with_time);
        etSetTime = findViewById(R.id.load_time_task);
        // end
        toolbar = findViewById(R.id.appbar);
        toolBarSet();
        viewGroup = findViewById(R.id.mainLayout);
        tvTimeStart = findViewById(R.id.tvTimeStatusStart);
        tvTimeEnd = findViewById(R.id.tvTimeStatusEnd);
        bTimeStart = findViewById(R.id.bTimeStart);
        bTimeEnd = findViewById(R.id.bTimeEnd);
        sCheckNot = findViewById(R.id.sCheckNot);
        dateTask=findViewById(R.id.dateTask);
        bSetNotif = findViewById(R.id.bSetNotif);
        tvStatusNotif = findViewById(R.id.tvStatusNotif);
        etTask = findViewById(R.id.etTask);
        etTask.requestFocus();
        sCheckTime = findViewById(R.id.sCheckTime);
        bSetNotif.setOnClickListener(this);
        sCheckNot.setOnCheckedChangeListener(this);
        bTimeEnd.setOnClickListener(this);
        bTimeStart.setOnClickListener(this);

        if (savedInstanceState != null) {
            isCheckTime=savedInstanceState.getBoolean("checkPlan");
            timeStartHour = savedInstanceState.getInt("startHour");
            timeStartMinute = savedInstanceState.getInt("startMinute");
            mTimeEndHour = savedInstanceState.getInt("endHour");
            timeEndMinute = savedInstanceState.getInt("endMinute");
            calendarStart.set(Calendar.HOUR_OF_DAY, timeStartHour);
            calendarStart.set(Calendar.MINUTE, timeStartMinute);
            calendarEnd.set(Calendar.HOUR_OF_DAY, mTimeEndHour);
            calendarEnd.set(Calendar.MINUTE, timeEndMinute);
            day=savedInstanceState.getInt("day");
            month=savedInstanceState.getInt("month");
            year=savedInstanceState.getInt("year");
            checkMinTime(calendarStart, tvTimeStart);
            checkMinTime(calendarEnd, tvTimeEnd);
            planTask(isCheckTime);


        }



        dateCalendar.set(Calendar.YEAR, year);
        dateCalendar.set(Calendar.DAY_OF_MONTH, day);
        dateCalendar.set(Calendar.MONTH, month);
        calendarStart.set(Calendar.HOUR_OF_DAY, timeStartHour);
        calendarStart.set(Calendar.MINUTE, timeStartMinute);
        calendarEnd.set(Calendar.HOUR_OF_DAY, mTimeEndHour);
        calendarEnd.set(Calendar.MINUTE, timeEndMinute);
        dateTask.setText(DateFormat.getDateInstance().format(dateCalendar.getTime()));
        dateTask.setPaintFlags(dateTask.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        dateTask.setOnClickListener(this);
        checkEnable(bSetNotif, tvStatusNotif, checkNotif);
        //   checkEnable(bSetNotif, tvStatusNotif, bTimeStart, bTimeEnd, sCheckNot, tvStatusNotif, isCheckTime);
        checkMinTime(calendarStart, tvTimeStart);
        checkMinTime(calendarEnd, tvTimeEnd);
        Realm.init(this);
        realm = Realm.getInstance(DataUpdate.getDefaultConfig());
        Log.d("LogCheckPlasnTask",""+isCheckTime);
        sCheckTime.setChecked(isCheckTime);
        sCheckTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                planTask(isChecked);
            }
        });



    }

    private void toolBarSet() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            toolbar.setTitle(getResources().getString(R.string.newTask));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });


        }
        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.newTask));

    }

    public void dialogTimeStart() {
        TimePickerFrag d = new TimePickerFrag(this, timeStartHour, timeStartMinute);
        d.show(getSupportFragmentManager(), "picker");


    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("checkPlan",isCheckTime);
        outState.putInt("startHour", timeStartHour);
        outState.putInt("startMinute", timeStartMinute);
        outState.putInt("endHour", mTimeEndHour);
        outState.putInt("endMinute", timeEndMinute);
        outState.putInt("day", day);
        outState.putInt("month", month);
        outState.putInt("year", year);
    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    public void dialogTimeEnd() {
        TimePickerEnd timePickerEnd = new TimePickerEnd(mTimeEndHour, timeEndMinute);
        timePickerEnd.setCancelable(false);
        timePickerEnd.show(getSupportFragmentManager(), "picker");

    }

    public void dialogNorifBefore() {
        ChooseNorif chooseNorif = new ChooseNorif();
        chooseNorif.setCancelable(true);
        chooseNorif.show(getSupportFragmentManager(), "picker");


    }

    public void startAlarm(Calendar calendar, int id, Integer notifBeforeStart, Integer day, Integer month, Integer year) {
        if (timeStartMinute - notifBeforeStart >= 0) {
            calendar.set(Calendar.HOUR_OF_DAY, timeStartHour);
            calendar.set(Calendar.MINUTE, timeStartMinute - notifBeforeStart);
            calendar.set(Calendar.SECOND, 0);

        } else {
            int r = 60 + (timeStartMinute - notifBeforeStart);
            calendar.set(Calendar.HOUR_OF_DAY, timeStartHour - 1);
            calendar.set(Calendar.MINUTE, r);
            calendar.set(Calendar.SECOND, 0);
        }
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("id", id);
        intent.putExtra("task", etTask.getText().toString().trim());
        System.out.println(id);
        PendingIntent p = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_ONE_SHOT);
        assert alarmManager != null;
        String w = DateFormat.getTimeInstance().format(calendar.getTime());
        Log.d("mylog", w);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), p);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bTimeStart:
                dialogTimeStart();
                break;
            case R.id.bTimeEnd:
                dialogTimeEnd();
                break;
            case R.id.bSetNotif:
                dialogNorifBefore();
                break;
            case R.id.dateTask:
                dateStart(day,month,year);
                break;
        }
    }

    public void dateStart(int day, int month, int year) {
        ChooseDate chooseDate = new ChooseDate(day, month, year);
        chooseDate.show(getSupportFragmentManager(), "pickerD");
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onFinishTimeDialog(int timeStartHour, int timeStartMinute) {
        this.timeStartHour = timeStartHour;
        this.timeStartMinute = timeStartMinute;
        calendarStart.set(Calendar.HOUR_OF_DAY, this.timeStartHour);
        calendarStart.set(Calendar.MINUTE, this.timeStartMinute);

        if (checkCorrectTime(timeStartHour, timeStartMinute, mTimeEndHour, timeEndMinute)) {
            checkMinTime(calendarStart, tvTimeStart);

        } else {

            mTimeEndHour = timeStartHour + 1;
            this.timeEndMinute = timeStartMinute;
            calendarEnd.set(Calendar.HOUR_OF_DAY, mTimeEndHour);
            calendarEnd.set(Calendar.MINUTE, timeEndMinute);
            checkMinTime(calendarStart, tvTimeStart);
            checkMinTime(calendarEnd, tvTimeEnd);
        }


    }


    @SuppressLint("SetTextI18n")
    @Override
    public void timeEnd(int timeEndHour, int timeEndMinute) {
        mTimeEndHour = timeEndHour;
        this.timeEndMinute = timeEndMinute;
        calendarEnd.set(Calendar.HOUR_OF_DAY, mTimeEndHour);
        calendarEnd.set(Calendar.MINUTE, this.timeEndMinute);
        if (checkCorrectTime(timeStartHour, timeStartMinute, mTimeEndHour, this.timeEndMinute)) {
            checkMinTime(calendarEnd, tvTimeEnd);

        } else {

            mTimeEndHour = timeStartHour + 1;
            this.timeEndMinute = timeStartMinute;
            calendarEnd.set(Calendar.HOUR_OF_DAY, mTimeEndHour);
            calendarEnd.set(Calendar.MINUTE, this.timeEndMinute);
            checkMinTime(calendarEnd, tvTimeEnd);
        }

    }


    @SuppressLint("SetTextI18n")
    public void checkMinTime(Calendar calendar, TextView textView) {
        String time = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
        textView.setText(time);
    }


    public boolean checkCorrectTime(int hourStart, int minuteStart, int hourEnd, int minuteEnd) {
        int raz = hourEnd - hourStart;
        if (raz > 0) {
            return true;
        } else if (raz == 0) {
            return minuteEnd - minuteStart > 0;

        }
        return false;
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        checkNotif = isChecked;
        checkEnable(bSetNotif, tvStatusNotif, checkNotif);
    }

    public void checkEnable(Button button, TextView textView, boolean check) {
        if (check) {
            button.setEnabled(true);
            textView.setTextColor(Color.BLACK);
            button.setBackgroundColor(Color.TRANSPARENT);


        } else {
            button.setEnabled(false);
            textView.setTextColor(getResources().getColor(R.color.textUnEnable));
            button.setBackgroundColor(getResources().getColor(R.color.enableNotifColor));


        }


    }

//    public void checkEnable(Button button, TextView textView, Button tStart, Button bTimeEnd, Switch s, TextView tvUved, boolean check) {
//        if (check) {
//            button.setEnabled(true);
//            bTimeEnd.setEnabled(true);
//            tStart.setEnabled(true);
//            textView.setTextColor(Color.BLACK);
//            button.setBackgroundColor(Color.TRANSPARENT);
//            tStart.setBackgroundColor(Color.TRANSPARENT);
//            bTimeEnd.setBackgroundColor(Color.TRANSPARENT);
//            tvUved.setTextColor(Color.BLACK);
//            s.setEnabled(true);
//
//
//        } else {
//            button.setEnabled(false);
//            bTimeEnd.setEnabled(false);
//            tStart.setEnabled(false);
//            textView.setTextColor(getResources().getColor(R.color.textUnEnable));
//            button.setBackgroundColor(getResources().getColor(R.color.enableNotifColor));
//            tStart.setBackgroundColor(getResources().getColor(R.color.enableNotifColor));
//            bTimeEnd.setBackgroundColor(getResources().getColor(R.color.enableNotifColor));
//            s.setEnabled(false);
//            tvUved.setTextColor(getResources().getColor(R.color.textUnEnable));
//
//
//        }
//
//
//    }

    private void planTask(boolean isCheckTime) {

        if (!isCheckTime) {
            this.isCheckTime = false;
            // new function maybe update or delete in future
            layoutTime.setVisibility(View.VISIBLE);
            // end
            viewGroup.setVisibility(View.GONE);
            checkNotif = false;
            sCheckNot.setChecked(false);
            checkEnable(bSetNotif, tvStatusNotif, false);

        } else {
            viewGroup.setVisibility(View.VISIBLE);
            this.isCheckTime=true;

        }
    }
    private void cancelAlarm(int id) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent p = PendingIntent.getBroadcast(this, id, intent, 0);
        assert alarmManager != null;
        alarmManager.cancel(p);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setNotif(int before) {
        notifBeforeStart = before;
        if (before == 60) {

            tvStatusNotif.setText(R.string.norifBefore60);
        } else
            switch (before){
                case 5:

                    tvStatusNotif.setText(R.string.norifBefore5);
                    break;
                case 15:
                    tvStatusNotif.setText(R.string.norifBefore15);
                    break;
                case 30:
                    tvStatusNotif.setText(R.string.norifBefore30);
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
                    break;
            }


    }

    @Override
    public void chooseDate(int day, int month, int year) {
        this.day=day;
        this.month=month;
        this.year=year;
        dateCalendar.set(Calendar.DAY_OF_MONTH, day);
        dateCalendar.set(Calendar.YEAR,year);
        dateCalendar.set(Calendar.MONTH, month);
        dateTask.setText(DateFormat.getDateInstance().format(dateCalendar.getTime()));
    }
}
