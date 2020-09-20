package com.example.to_dolist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;

import dialogs.ChooseDate;
import inteface.OnRecClick;
import inteface.UpdateCountCallback;
import io.realm.Realm;
import recycler_for_task.DataUpdate;
import recycler_for_task.TaskAdapter;
import recycler_for_task.TaskData;
import recycler_for_task.TaskDataHelper;

public class ToDoList extends Fragment implements UpdateCountCallback {
    RecyclerView recyclerView;
    Realm realm;
    Context context;
    TextView tvDateNow, tvCountLostTask;
    Bundle bundle = new Bundle();
    Integer day, month, year;
    int countList;
    FloatingActionButton floatingActionButton;
    TaskAdapter taskAdapter;
    private Calendar calendarDate;
    ImageButton imageILTask;
    PopupWindowTask popupWindowTask;

//        public static ToDoList getInstance(Bundle args){
//        ToDoList toDoList =  new ToDoList();
//        Bundle arguments= new Bundle();
//        arguments.putBundle("date",args);
//        toDoList.setArguments(arguments);
//        return toDoList;
//
//    }
//

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        context = getActivity();
//        Time today = new Time(Time.getCurrentTimezone());
//        today.setToNow();

//        day = today.monthDay;
//        Log.d(MainActivity.LOG_TAG, "change " + day);
//        month = today.month;
//        year = today.year;
        Realm.init(context);
        realm = Realm.getInstance(DataUpdate.getDefaultConfig());


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).setTitle(getString(R.string.do_list));
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.to_do_fragment, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        day = this.getArguments().getInt("day");
        month = this.getArguments().getInt("month");
        year = this.getArguments().getInt("year");
        calendarDate = Calendar.getInstance();
        tvDateNow = view.findViewById(R.id.tvTodayDate);
        tvCountLostTask = view.findViewById(R.id.countLostTask);
        calendarDate.set(Calendar.YEAR, year);
        calendarDate.set(Calendar.MONTH, month);
        calendarDate.set(Calendar.DAY_OF_MONTH, day);
        imageILTask = view.findViewById(R.id.bILTask);
        popupWindowTask = new PopupWindowTask(getActivity(), correctDate()[0], correctDate()[1], correctDate()[2], realm, getActivity(),this);

        imageILTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindowTask.onCreatePopup().showAsDropDown(v, -170, 0);

            }
        });
        getCurrentDate();
        showCountLostTask(popupWindowTask.getCountTask());

//        showCountLostTask(getCountLostTask());
        Log.d("TagBundle", "day " + day);
        floatingActionButton = view.findViewById(R.id.floatingActionButton);

        taskAdapter = new TaskAdapter(realm.where(TaskData.class).equalTo("day", day).equalTo("month", month)
                .equalTo("year", year).findAll(), context, getActivity());
        recyclerView = view.findViewById(R.id.recycler);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);
        recyclerView.setAdapter(taskAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                countList = taskAdapter.getItemCount();
                if (countList <= 5) {
                    floatingActionButton.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy < 0) {
                    floatingActionButton.show();
                } else if (dy > 0) {
                    floatingActionButton.hide();

                }
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), CreateTask.class);
                intent.putExtra("day", day);
                intent.putExtra("month", month);
                intent.putExtra("year", year);
                startActivity(intent);

            }
        });
        return view;
    }

    private void getCurrentDate() {
        calendarDate.set(Calendar.DAY_OF_MONTH, day);
        calendarDate.set(Calendar.MONTH, month);
        calendarDate.set(Calendar.YEAR, year);
        tvDateNow.setText(DateFormat.getDateInstance().format(calendarDate.getTime()));
    }

    private int getCountLostTask() {
        int[] date;
        date = correctDate();
        return TaskDataHelper.getCountLostTask(realm, date[0], date[1], date[2]);

    }

    private void showCountLostTask(int count) {
        if (count <= 0) {
            tvCountLostTask.setVisibility(View.GONE);
        } else {
            tvCountLostTask.setVisibility(View.VISIBLE);
            tvCountLostTask.setText(String.valueOf(count));
        }
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

    @Override
    public void onResume() {
        super.onResume();
        showCountLostTask(popupWindowTask.getCountTask());
        if(popupWindowTask!=null) {
            if (popupWindowTask.getCountTask() == 0) {
                popupWindowTask.emptyList();

            }
        }
    }


    @Override
    public void taskCountUpdate() {
        showCountLostTask(popupWindowTask.getCountTask());
        if(popupWindowTask.getCountTask()==0){
            popupWindowTask.emptyList();

        }

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.todo_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteAll:
                TaskDataHelper.deleteAll(realm, day, month, year);
                Toast.makeText(getActivity(), R.string.all_delete, Toast.LENGTH_SHORT).show();
                break;
            case R.id.choose_date:
                dateStart(day, month, year);
                break;
        }
        return true;
    }

    public void dateStart(int day, int month, int year) {
        ChooseDate chooseDate = new ChooseDate(day, month, year);
        assert getFragmentManager() != null;
        chooseDate.show(getFragmentManager(), "pickerD");
    }
}
