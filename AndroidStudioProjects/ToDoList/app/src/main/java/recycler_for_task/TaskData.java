package recycler_for_task;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TaskData extends RealmObject {
    @PrimaryKey
    private int id;
    private String task;
    private int countLose;
    private Integer day;
    private Integer month;
    private Integer year;
    private boolean check;
    private String timeStartHour;
    private String timeEndHour;
    private String timeStartMinute;
    private String timeEndMinute;
    private boolean lose;
    private boolean notif;
    private boolean planTask;
    private  String notifBeforeStart;
    private boolean sCheckTime;

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }


    public int getId() {
        return id;
    }


    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }


    public boolean isLose() {
        return lose;
    }

    public void setLose(boolean lose) {
        this.lose = lose;
    }

    public String  getTimeStartHour() {
        return timeStartHour;
    }

    public void setTimeStartHour(String timeStartHour) {
        this.timeStartHour = timeStartHour;
    }

    public String  getTimeEndHour() {
        return timeEndHour;
    }

    public void setTimeEndHour(String timeEndHour) {
        this.timeEndHour = timeEndHour;
    }

    public String  getTimeStartMinute() {
        return timeStartMinute;
    }

    public void setTimeStartMinute(String  timeStartMinute) {
        this.timeStartMinute = timeStartMinute;
    }

    public String  getTimeEndMinute() {
        return timeEndMinute;
    }

    public void setTimeEndMinute(String  timeEndMinute) {
        this.timeEndMinute = timeEndMinute;
    }


    public boolean isNotif() {
        return notif;
    }

    public void setNotif(boolean notif) {
        this.notif = notif;
    }

    public String getNotifBeforeStart() {
        return notifBeforeStart;
    }

    public void setNotifBeforeStart(String notifBeforeStart) {
        this.notifBeforeStart = notifBeforeStart;
    }




    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public boolean issCheckTime() {
        return sCheckTime;
    }

    public void setsCheckTime(boolean sCheckTime) {
        this.sCheckTime = sCheckTime;
    }

    public boolean isPlanTask() {
        return planTask;
    }

    public void setPlanTask(boolean planTask) {
        this.planTask = planTask;
    }

    public int getCountLose() {
        return countLose;
    }

    public void setCountLose(int countLose) {
        this.countLose = countLose;
    }
}
