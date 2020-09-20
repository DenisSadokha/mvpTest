package notfication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.to_dolist.ChangeTask;
import com.example.to_dolist.CreateTask;
import com.example.to_dolist.MainActivity;
import com.example.to_dolist.R;

import io.realm.Realm;
import recycler_for_task.DataUpdate;
import recycler_for_task.TaskData;
import recycler_for_task.TaskDataHelper;

public class NotificationHelper extends ContextWrapper {
    public static final String NOTIF_TASK_ID = "taskId";
    public static final String NOTOF_TASK_NAME = "Task Notification";
    public Realm realm;
    NotificationManager manager;

    public NotificationHelper(Context base) {
        super(base);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Realm.init(base);
            realm= Realm.getInstance(DataUpdate.getDefaultConfig());
            createChannel();
        }
}

    public void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannelTask = new NotificationChannel(NOTIF_TASK_ID, NOTOF_TASK_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannelTask.setLightColor(Color.BLUE);
            notificationChannelTask.enableVibration(true);
            notificationChannelTask.enableLights(true);
            notificationChannelTask.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            getNotificationManager().createNotificationChannel(notificationChannelTask);


        }
    }

    public NotificationManager getNotificationManager() {
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        return manager;
    }

    public NotificationCompat.Builder createNotification(String title, String message, int id) {
        // get data from database
        TaskData taskData = TaskDataHelper.getDataContent(realm, id);
        Log.d("IDTAG",""+id);
        final String task = taskData.getTask();
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
//        String task = taskData.getTask();
//        final Integer tsh = Integer.parseInt(taskData.getTimeStartHour());
//        final Integer tsm = Integer.parseInt(taskData.getTimeStartMinute());
//        final Integer teh = Integer.parseInt(taskData.getTimeEndHour());
//        final Integer tem = Integer.parseInt(taskData.getTimeEndMinute());
//        final boolean checkNotif = taskData.isNotif();
//        final Integer notifBeforeStart= Integer.parseInt(taskData.getNotifBeforeStart());

        // set up data and start intent
        Intent intent = new Intent(getApplicationContext(), ChangeTask.class);
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
//        Intent intent = new Intent(getApplicationContext(), ChangeTask.class);
//        intent.putExtra("tsh", tsh);
//        intent.putExtra("nbs",notifBeforeStart);
//        intent.putExtra("checkNotif", checkNotif);
//        intent.putExtra("tsm", tsm);
//        intent.putExtra("teh", teh);
//        intent.putExtra("tem", tem);
//        intent.putExtra("task", task);
//        intent.putExtra("id", id);
      // create notification
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return new NotificationCompat.Builder(getApplicationContext(), NOTIF_TASK_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.fast);

    }

}
