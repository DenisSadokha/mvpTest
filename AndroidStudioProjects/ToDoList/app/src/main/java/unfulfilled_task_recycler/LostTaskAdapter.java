package unfulfilled_task_recycler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.to_dolist.ChangeTask;
import com.example.to_dolist.R;

import org.w3c.dom.Text;

import java.util.Objects;

import inteface.OnRecClick;
import inteface.UpdateCountCallback;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import recycler_for_task.DataUpdate;
import recycler_for_task.TaskAdapter;
import recycler_for_task.TaskData;
import recycler_for_task.TaskDataHelper;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static com.example.to_dolist.R.drawable.close;
import static com.example.to_dolist.R.drawable.ic_autorenew_orange_24dp;
import static com.example.to_dolist.R.drawable.ic_check_black_24dp;
import static com.example.to_dolist.R.drawable.ic_query_builder_24px;

public class LostTaskAdapter extends RealmRecyclerViewAdapter<TaskData, LostTaskAdapter.Holder> {
   private Context context;
    private Activity activity;
    LostTaskAdapter lostTaskAdapter;
    OnRecClick onRecClick;
    Realm realm;
    private UpdateCountCallback updateCountCallback;


    public LostTaskAdapter(@Nullable OrderedRealmCollection<TaskData> data, Context context, Activity activity, UpdateCountCallback updateCountCallback) {
        super(data, true);
        this.context = context;
        this.activity = activity;
        this.updateCountCallback=updateCountCallback;
        Realm.init(context);
        realm= Realm.getInstance(DataUpdate.getDefaultConfig());
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_task, parent, false);


        return new LostTaskAdapter.Holder(view);
    }
 public void setOnRecListener(OnRecClick onRecClick){
        this.onRecClick=onRecClick;

 }
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int position) {
        final TaskData taskData = getItem(position);
        assert taskData != null;
        final String task = taskData.getTask();
        final int id = taskData.getId();
        final Integer tsh = Integer.parseInt(taskData.getTimeStartHour());
        final Integer tsm = Integer.parseInt(taskData.getTimeStartMinute());
        final Integer teh = Integer.parseInt(taskData.getTimeEndHour());
        final Integer day = taskData.getDay();
        final Integer month = taskData.getMonth();
        final Integer year = taskData.getYear();
        final boolean checkPlan = taskData.isPlanTask();
        final boolean checkComplete = taskData.isCheck();
        final Integer tem = Integer.parseInt(taskData.getTimeEndMinute());
        final boolean checkNotif = taskData.isNotif();
        UpdateCountCallback i = null;

        final Integer notifBeforeStart = Integer.parseInt(taskData.getNotifBeforeStart());
        final UpdateCountCallback finalI = i;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChangeTask.class);
                onRecClick.onRecClickListener();
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
                                updateCountCallback.taskCountUpdate();
                                Toast.makeText(context, R.string.deleteToast, Toast.LENGTH_SHORT).show();

                                break;
                            case R.id.performed:
                                TaskDataHelper.checked(realm, holder.getItemId());
                                updateCountCallback.taskCountUpdate();

                                break;
                            case R.id.cancel:
                                TaskDataHelper.losed(realm, holder.getItemId());
                                updateCountCallback.taskCountUpdate();
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
        holder.tvTitle.setText(task);
        showTime(taskData, holder);


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

    public class Holder extends RecyclerView.ViewHolder {
        TextView tvTitle, timeEnd, timeStart, option, status;
        ImageView imageView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            status= itemView.findViewById(R.id.tvStatus);
            imageView= itemView.findViewById(R.id.imageView);
            tvTitle = itemView.findViewById(R.id.tvTask);
            option = itemView.findViewById(R.id.option);
            timeStart = itemView.findViewById(R.id.tvTimeStart);
            timeEnd = itemView.findViewById(R.id.tvTimeEnd);
        }
    }

    @SuppressLint("SetTextI18n")
    public void showTime(TaskData taskData, LostTaskAdapter.Holder holder) {
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
