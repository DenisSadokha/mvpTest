package recycler_for_task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.to_dolist.ChangeTask;
import com.example.to_dolist.CreateTask;
import com.example.to_dolist.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import database_calendar.CalnedarBase;
import inteface.UpdateCountCallback;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;

import static android.graphics.Color.*;
import static android.graphics.Color.RED;
import static com.example.to_dolist.R.drawable.abc_spinner_textfield_background_material;
import static com.example.to_dolist.R.drawable.close;
import static com.example.to_dolist.R.drawable.ic_autorenew_orange_24dp;
import static com.example.to_dolist.R.drawable.ic_check_black_24dp;
import static com.example.to_dolist.R.drawable.ic_playlist_add_check_black_24dp;
import static com.example.to_dolist.R.drawable.ic_query_builder_24px;
import static com.example.to_dolist.R.drawable.notification_tile_bg;

public class TaskAdapter extends RealmRecyclerViewAdapter<TaskData, TaskAdapter.TaskHolder> {
    Context context;
    CardView cardView;
    long getId;
    Realm realm;
    Activity activity;



    public TaskAdapter(@Nullable OrderedRealmCollection<TaskData> data, Context context, Activity activity) {
        super(data, true);
        this.context = context;
        this.activity = activity;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_task, parent, false);
        Realm.init(context);
        realm = Realm.getInstance(DataUpdate.getDefaultConfig());
        cardView = view.findViewById(R.id.cardview);
        return new TaskAdapter.TaskHolder(view);
    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull final TaskHolder holder, final int position) {
        final TaskData taskData = getItem(position);

        final TaskAdapter taskAdapter;
        assert taskData != null;
        final String task = taskData.getTask();
        final int id = taskData.getId();
        final Integer tsh = Integer.parseInt(taskData.getTimeStartHour());
        final Integer tsm = Integer.parseInt(taskData.getTimeStartMinute());
        final Integer teh = Integer.parseInt(taskData.getTimeEndHour());
        final Integer day = taskData.getDay();
        final Integer month = taskData.getMonth();
        final Integer year = taskData.getYear();
        final boolean checkPlan= taskData.isPlanTask();
        final boolean checkComplete = taskData.isCheck();
        final Integer tem = Integer.parseInt(taskData.getTimeEndMinute());
        final boolean checkNotif = taskData.isNotif();
        final Integer notifBeforeStart = Integer.parseInt(taskData.getNotifBeforeStart());
        taskAdapter = new TaskAdapter(realm.where(TaskData.class).findAll(), context, activity);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChangeTask.class);
                intent.putExtra("tsh", tsh);
                intent.putExtra("day", day);
                intent.putExtra("month", month);
                intent.putExtra("year", year);
                intent.putExtra("nbs", notifBeforeStart);
                intent.putExtra("checkNotif", checkNotif);
                intent.putExtra("tsm", tsm);
                intent.putExtra("check", checkComplete);
                intent.putExtra("checkPlan", checkPlan);
                intent.putExtra("teh", teh);
                intent.putExtra("tem", tem);
                intent.putExtra("task", task);
                intent.putExtra("id", id);
                activity.startActivity(intent);


            }
        });
        holder.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(context, holder.option);
                popupMenu.inflate(R.menu.context_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete:
                                if (taskData.isNotif()) {

                                    NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
                                    assert notificationManager != null;
                                    notificationManager.cancel(id);


                                }
                                TaskDataHelper.deleteItem(realm, holder.getItemId());

                                Toast.makeText(context, R.string.deleteToast, Toast.LENGTH_SHORT).show();
                                taskAdapter.notifyDataSetChanged();
                                break;
                            case R.id.performed:
                                TaskDataHelper.checked(realm, holder.getItemId());

                                break;
                            case R.id.cancel:
                                TaskDataHelper.losed(realm, holder.getItemId());
                                NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
                                assert notificationManager != null;
                                notificationManager.cancel(id);
                        }
                        return false;
                    }
                });


                popupMenu.show();

            }
        });


        holder.task.setText(taskData.getTask());
        showTime(taskData, holder);
        Log.d("TAG", "Position" + position);

        boolean check;
        boolean planTask = TaskDataHelper.checkPlan(realm, holder.getItemId());
        boolean lose = TaskDataHelper.checkLose(realm, holder.getItemId());
        check = TaskDataHelper.reqCheck(realm, holder.getItemId());
        if (check) {
            holder.status.setText(R.string.statusDone);
            holder.status.setTextColor(GREEN);
            holder.imageView.setImageResource(ic_check_black_24dp);
            holder.status.setGravity(Gravity.CENTER);
            if(!planTask){
                holder.timeStart.setVisibility(View.GONE);
                holder.timeEnd.setVisibility(View.GONE);

            }
                else {
                holder.timeStart.setVisibility(View.VISIBLE);
                holder.timeEnd.setVisibility(View.VISIBLE);
            }

        } else if (lose) {
            System.out.println("none");
            holder.status.setText(R.string.statusCancle);
            holder.status.setGravity(Gravity.CENTER);
            holder.imageView.setImageResource(close);
            holder.status.setTextColor(RED);
            if(!planTask){
                holder.timeStart.setVisibility(View.GONE);
                holder.timeEnd.setVisibility(View.GONE);

            }
            else {
                holder.timeStart.setVisibility(View.VISIBLE);
                holder.timeEnd.setVisibility(View.VISIBLE);
            }



        } else if (!planTask) {
            holder.status.setText(R.string.statusProcess);
            holder.status.setTextColor(R.color.textColor);
            holder.status.setGravity(Gravity.CENTER);
            holder.imageView.setImageResource(ic_autorenew_orange_24dp);
            holder.timeStart.setVisibility(View.GONE);
            holder.timeEnd.setVisibility(View.GONE);

        } else {
            holder.status.setText(R.string.statusPlan);
            holder.imageView.setImageResource(ic_query_builder_24px);
            holder.status.setGravity(Gravity.CENTER);
            holder.status.setTextColor(BLACK);



        }

    }

    public class TaskHolder extends RecyclerView.ViewHolder {
        TextView timeEnd, task, timeStart, option, status;
        ImageView imageView;


        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.tvStatus);
            imageView = itemView.findViewById(R.id.imageView);
            task = itemView.findViewById(R.id.tvTask);
            option = itemView.findViewById(R.id.option);
            timeStart = itemView.findViewById(R.id.tvTimeStart);
            timeEnd = itemView.findViewById(R.id.tvTimeEnd);


        }


    }

    @SuppressLint("SetTextI18n")
    public void showTime(TaskData taskData, TaskHolder holder) {
        int minStart = Integer.parseInt(taskData.getTimeStartMinute());
        int hourStart = Integer.parseInt(taskData.getTimeStartHour());
        int minEnd = Integer.parseInt(taskData.getTimeEndMinute());
        int hourEnd = Integer.parseInt(taskData.getTimeEndHour());
        if (minStart >= 0 && minStart <= 9) {
            if (hourStart >= 0 && hourStart <= 9) {

                holder.timeStart.setText("0" + taskData.getTimeStartHour() + ":" + "0" + minStart);
            } else {
                holder.timeStart.setText(taskData.getTimeStartHour() + ":" + "0" + minStart);
            }
        } else if (hourStart >= 0 && hourStart <= 9) {
            holder.timeStart.setText("0" + taskData.getTimeStartHour() + ":" + minStart);
        } else
            holder.timeStart.setText(taskData.getTimeStartHour() + ":" + minStart);


        if (minEnd >= 0 && minEnd <= 9) {
            if (hourEnd >= 0 && hourEnd <= 9) {

                holder.timeEnd.setText("0" + taskData.getTimeEndHour() + ":" + "0" + minEnd);
            } else {
                holder.timeEnd.setText(taskData.getTimeEndHour() + ":" + "0" + minEnd);
            }
        } else if (hourEnd >= 0 && hourEnd <= 9) {
            holder.timeEnd.setText("0" + taskData.getTimeEndHour() + ":" + minEnd);
        } else
            holder.timeEnd.setText(taskData.getTimeEndHour() + ":" + minEnd);


    }


    @Override
    public long getItemId(int index) {
        return Objects.requireNonNull(getItem(index)).getId();
    }
}
