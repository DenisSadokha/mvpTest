package database_calendar;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import recycler_for_task.TaskData;

public class CalnedarBase extends RealmObject {
    @PrimaryKey
    private long id;

    private String day;
    private String month;
    private String year;
    private TaskData taskData;
    public RealmList<TaskData> taskDataRealmList;


    public long getId() {
        return id;
    }

    public RealmList<TaskData> getTaskDataRealmList() {
        return taskDataRealmList;
    }

    public void setTaskDataRealmList(RealmList<TaskData> taskDataRealmList) {
        this.taskDataRealmList = taskDataRealmList;
    }
}
