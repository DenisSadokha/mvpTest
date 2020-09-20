package com.example.to_dolist;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
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
import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;

import dialogs.ChooseDate;
import dialogs.ChooseNorif;
import dialogs.TimePickerEnd;
import dialogs.TimePickerFrag;
import inteface.UpdateCountCallback;
import io.realm.Realm;
import notfication.AlertReceiver;
import recycler_for_task.DataUpdate;
import recycler_for_task.TaskDataHelper;


public class ChangeTask extends AppCompatActivity implements View.OnClickListener, TimePickerFrag.EditTimeStart, TimePickerEnd.TimeEnd,
        CompoundButton.OnCheckedChangeListener, ChooseNorif.Notif, ChooseDate.DateChoose {
    UpdateCountCallback updateCountCallback;
    Realm realm;
    Toolbar toolbar;
    String task;
    boolean checkComplete;
    Calendar calendarStart,
            calendarEnd,
            nCalendarBeforeStart, calendarDate;
    Integer timeStartHour, timeStartMinute, timeEndHour, timeEndMinute;
    int id;
    EditText etTask;
    TextView tvTimeStart, tvTimeEnd, tvStatusNotif;
    boolean checkNotif, checkPlan;
    Switch sCheckNot;
    Button bTimeStart, bTimeEnd, bSetNotif;
    TextView tvDateTask;
    private Integer notifBeforeStart;
    private Integer day, year, month;
    ViewGroup layout;


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("startHour", timeStartHour);
        outState.putInt("startMinute", timeStartMinute);
        outState.putInt("endHour", timeEndHour);
        outState.putInt("endMinute", timeEndMinute);
        outState.putInt("day", day);
        outState.putInt("month", month);
        outState.putInt("year", year);
        outState.putInt("id", id);

    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        timeStartHour = savedInstanceState.getInt("startHour");
        timeStartMinute = savedInstanceState.getInt("startMinute");
        timeEndHour = savedInstanceState.getInt("endHour");
        timeEndMinute = savedInstanceState.getInt("endMinute");
            calendarStart.set(Calendar.HOUR_OF_DAY, timeStartHour);
        calendarStart.set(Calendar.MINUTE, timeStartMinute);
        calendarEnd.set(Calendar.HOUR_OF_DAY, timeEndHour);
        calendarEnd.set(Calendar.MINUTE, timeEndMinute);
        checkMinTime(calendarStart, tvTimeStart);
        checkMinTime(calendarEnd, tvTimeEnd);

    }

    private void updateTask(String task, Integer timeStartHour, Integer timeStartMinute, Integer timeEndHour, Integer timeEndMinute, Integer day
            , Integer month, Integer year) {
        if (checkNotif) {
            startAlarm(nCalendarBeforeStart, id, notifBeforeStart);


        } else {
            cancelAlarm(id);

        }
        Log.d("This Update Id", "" + id);
        TaskDataHelper.update(realm, task, timeStartHour.toString(), timeStartMinute.toString(),
                timeEndHour.toString(), timeEndMinute.toString(), checkNotif, notifBeforeStart.toString(), day, month, year, id, correctDate()[0],
                correctDate()[1], correctDate()[2]);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(this);

        realm = Realm.getInstance(DataUpdate.getDefaultConfig());
        Bundle bundle = getIntent().getExtras();
        calendarDate = Calendar.getInstance();
        calendarStart = Calendar.getInstance();
        calendarEnd = Calendar.getInstance();
        nCalendarBeforeStart = Calendar.getInstance();
        if (bundle != null) {
            updateCountCallback = (UpdateCountCallback) bundle.get("callback");
            task = bundle.getString("task");
            checkNotif = bundle.getBoolean("checkNotif");
            notifBeforeStart = bundle.getInt("nbs");
            timeStartHour = bundle.getInt("tsh");
            timeStartMinute = bundle.getInt("tsm");
            timeEndHour = bundle.getInt("teh");
            timeEndMinute = bundle.getInt("tem");
            day = bundle.getInt("day");
            month = bundle.getInt("month");
            year = bundle.getInt("year");
            id = bundle.getInt("id");
            if (savedInstanceState != null) {
                id = savedInstanceState.getInt("id");
                day = savedInstanceState.getInt("day");
                month = savedInstanceState.getInt("month");
                year = savedInstanceState.getInt("year");
            }
            checkPlan = bundle.getBoolean("checkPlan");
            Log.d("CHECKPLAN", "" + checkPlan);
            checkComplete = bundle.getBoolean("check");
            calendarStart.set(Calendar.HOUR_OF_DAY, timeStartHour);
            calendarStart.set(Calendar.DAY_OF_MONTH, day);
            calendarStart.set(Calendar.MONTH, month);
            calendarStart.set(Calendar.YEAR, year);
            calendarStart.set(Calendar.MINUTE, timeStartMinute);
            calendarDate.set(Calendar.MONTH, month);
            calendarDate.set(Calendar.YEAR, year);
            calendarDate.set(Calendar.DAY_OF_MONTH, day);
            calendarEnd.set(Calendar.HOUR_OF_DAY, timeEndHour);
            calendarEnd.set(Calendar.MINUTE, timeEndMinute);

        }

        setContentView(R.layout.change_task);
        toolbar = findViewById(R.id.appbar);
        toolBarSet();
        tvTimeStart = findViewById(R.id.tvTimeStatusStart);
        tvTimeEnd = findViewById(R.id.tvTimeStatusEnd);
        etTask = findViewById(R.id.etTask);
        tvDateTask = findViewById(R.id.dateTask);
        sCheckNot = findViewById(R.id.sCheckNot);
        layout = findViewById(R.id.layout);
        bSetNotif = findViewById(R.id.bSetNotif);
        bSetNotif.setOnClickListener(this);
        sCheckNot.setOnCheckedChangeListener(this);
        bTimeStart = findViewById(R.id.bTimeStart);
        bTimeEnd = findViewById(R.id.bTimeEnd);
        bTimeEnd.setOnClickListener(this);
        bTimeStart.setOnClickListener(this);
        tvStatusNotif = findViewById(R.id.tvStatusNotif);
        etTask.setText(task);
        checkMinTime(calendarStart, tvTimeStart);
        checkMinTime(calendarEnd, tvTimeEnd);
        checkEnable(bSetNotif, tvStatusNotif, checkNotif);
        setNotif(notifBeforeStart);
        checkPlanTask(checkPlan, layout);

        tvDateTask.setText(DateFormat.getDateInstance().format(calendarDate.getTime()));
        tvDateTask.setMovementMethod(LinkMovementMethod.getInstance());
        tvDateTask.setPaintFlags(tvDateTask.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvDateTask.setOnClickListener(this);

    }

    private void checkPlanTask(boolean check, ViewGroup viewGroup) {
        if (check) {
            viewGroup.setVisibility(View.VISIBLE);
            checkPlan = true;

        } else {
            viewGroup.setVisibility(View.GONE);
            checkPlan = false;
            checkEnable(bSetNotif, tvStatusNotif, false);
        }

    }

    private void toolBarSet() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });


        }
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.Task_change);

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

    @SuppressLint("SetTextI18n")
    public void checkMinTime(Calendar calendar, TextView textView) {
        String time = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
        textView.setText(time);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bTimeStart:
                dialogTimeStart();
                break;
            case R.id.bTimeEnd:
//                CalnedarBase calnedarBase1 = realm.where(CalnedarBase.class).equalTo("id",id).findFirst();
//                Log.d("MyTag", calnedarBase1.getTaskDataRealmList().get(0).getTask());
//                Log.d("MyTag", calnedarBase1.getTaskDataRealmList().get(0).getTimeStartMinute());
                dialogTimeEnd();
                break;
            case R.id.bSetNotif:
                dialogNorifBefore();
                break;
            case R.id.dateTask:
                dateStart(day, month, year);
                break;
        }
    }

    public void dateStart(int day, int month, int year) {
        ChooseDate chooseDate = new ChooseDate(day, month, year);
        chooseDate.show(getSupportFragmentManager(), "pickerD");
    }

    public void dialogTimeStart() {
        TimePickerFrag d = new TimePickerFrag(this, timeStartHour, timeStartMinute);
        d.show(getSupportFragmentManager(), "picker");


    }

    public void dialogTimeEnd() {
        TimePickerEnd timePickerEnd = new TimePickerEnd(timeEndHour, timeEndMinute);
        timePickerEnd.setCancelable(false);
        timePickerEnd.show(getSupportFragmentManager(), "picker");

    }

    @Override
    public void onFinishTimeDialog(int timeStartHour, int timeStartMinute) {

        this.timeStartHour = timeStartHour;
        this.timeStartMinute = timeStartMinute;
        calendarStart.set(Calendar.HOUR_OF_DAY, this.timeStartHour);
        calendarStart.set(Calendar.MINUTE, this.timeStartMinute);

        if (checkCorrectTime(this.timeStartHour, this.timeStartMinute, timeEndHour, timeEndMinute)) {
            checkMinTime(calendarStart, tvTimeStart);

        } else {

            this.timeEndHour = timeStartHour + 1;
            this.timeEndMinute = timeStartMinute;

            calendarEnd.set(Calendar.HOUR_OF_DAY, timeEndHour);
            calendarEnd.set(Calendar.MINUTE, timeEndMinute);
            checkMinTime(calendarStart, tvTimeStart);
            checkMinTime(calendarEnd, tvTimeEnd);
        }
    }

    public void dialogNorifBefore() {
        ChooseNorif chooseNorif = new ChooseNorif();
        chooseNorif.setCancelable(true);
        chooseNorif.show(getSupportFragmentManager(), "picker");

    }

    @Override
    public void timeEnd(int timeEndHour, int timeEndMinute) {
        this.timeEndHour = timeEndHour;
        this.timeEndMinute = timeEndMinute;
        calendarEnd.set(Calendar.HOUR_OF_DAY, this.timeEndHour);
        calendarEnd.set(Calendar.MINUTE, this.timeEndMinute);
        if (checkCorrectTime(timeStartHour, timeStartMinute, this.timeEndHour, this.timeEndMinute)) {
            checkMinTime(calendarEnd, tvTimeEnd);

        } else {

            this.timeEndHour = timeStartHour + 1;
            this.timeEndMinute = timeStartMinute;
            calendarEnd.set(Calendar.HOUR_OF_DAY, this.timeEndHour);
            calendarEnd.set(Calendar.MINUTE, this.timeEndMinute);
            checkMinTime(calendarEnd, tvTimeEnd);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_change, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveTask:
                task = etTask.getText().toString().trim();
                updateTask(task, timeStartHour, timeStartMinute, timeEndHour, timeEndMinute, day, month, year);
                onBackPressed();
                Toast.makeText(this, R.string.saveToast, Toast.LENGTH_LONG).show();
                break;
            case R.id.deleteTask:
                if (updateCountCallback != null) {
                    updateCountCallback.taskCountUpdate();
                }
                TaskDataHelper.deleteItem(realm, id);
                Toast.makeText(this, R.string.deleteToast, Toast.LENGTH_SHORT).show();
                cancelAlarm(id);
                finish();
                break;


        }
        return true;
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
            sCheckNot.setChecked(true);


        } else {
            button.setEnabled(false);
            textView.setTextColor(getResources().getColor(R.color.textUnEnable));
            button.setBackgroundColor(getResources().getColor(R.color.enableNotifColor));
            sCheckNot.setChecked(false);


        }


    }

    private void cancelAlarm(int id) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent p = PendingIntent.getBroadcast(this, id, intent, 0);
        assert alarmManager != null;
        alarmManager.cancel(p);
    }

    public void startAlarm(Calendar calendar, int id, Integer notifBeforeStart) {
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
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("id", id);
        intent.putExtra("task", etTask.getText().toString().trim());
        System.out.println(id);
        PendingIntent p = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_ONE_SHOT);
        assert alarmManager != null;
        String w = DateFormat.getTimeInstance().format(calendar.getTime());
        System.out.println(w);
        Log.d("mylog", w);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), p);
    }

    void checkCompleteTask(boolean checkComplete) {
        if (checkComplete) {


        } else {

        }

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
        this.day = day;
        this.month = month;
        this.year = year;
        calendarDate.set(Calendar.DAY_OF_MONTH, day);
        calendarDate.set(Calendar.YEAR, year);
        calendarDate.set(Calendar.MONTH, month);
        tvDateTask.setText(DateFormat.getDateInstance().format(calendarDate.getTime()));
    }


    private int[] correctDate() {
        int[] date = new int[3];
        date[0] = day;
        date[1] = month;
        date[2] = year;
        if (day - 1 > 0) {
            date[0] = day - 1;
            date[1] = month;
            date[2] = year;
        } else {
            if (month - 1 >= 0 && year != 2024 && year != 2028 && year != 2032 && year != 2036) {
                switch (month - 1) {
                    case 0:
                    case 11:
                    case 7:
                    case 6:
                    case 9:
                    case 4:
                    case 2:
                        date[0] = 31;
                        break;
                    case 1:
                        date[0] = 28;
                        break;
                    case 3:
                    case 10:
                    case 8:
                    case 5:
                        date[0] = 30;
                        break;

                }
                date[1] = month - 1;
            } else if (year == 2024 || year == 2028 || year == 2032 || year == 2036 && month - 1 >= 0) {
                switch (month) {
                    case 0:
                    case 11:
                    case 7:
                    case 6:
                    case 9:
                    case 4:
                    case 2:
                        date[0] = 31;
                        break;
                    case 1:
                        date[0] = 29;
                        break;
                    case 3:
                    case 10:
                    case 8:
                    case 5:
                        date[0] = 30;
                        break;

                }

            } else if (month < 0) {
                date[2] = year - 1;
                date[1] = 0;
                date[0] = 1;


            }
        }

        Log.d("DATETAG", "" + date[0] + " " + date[1] + " " + date[2]);
        return date;

    }
}
