package recycler_for_task;

import android.util.Log;

import io.realm.Realm;
import io.realm.RealmResults;

public class TaskDataHelper {
    private static int key;
    private static boolean check;
    private static boolean lose;
    private static boolean planTask;
    private static boolean isNotif;
    private static int lost;
    static TaskData taskData;


    public static void setNotif(Realm realm, final long id) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TaskData taskData = realm.where(TaskData.class).equalTo("id", id).findFirst();
                assert taskData != null;
                if (taskData.isNotif()) {
                    taskData.setNotif(false);
                } else taskData.setNotif(true);


            }
        });


    }

    public static void incCount(Realm realm, final Integer day, final Integer month, final Integer year) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TaskData taskData = realm.where(TaskData.class)
                        .equalTo("day", day)
                        .equalTo("month", month)
                        .equalTo("year", year)
                        .findFirst();
            }
        });

    }

    public static int getCountLostTask(Realm realm, final Integer day, final Integer month, final Integer year) {
        lost = 0;
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TaskData taskData = realm.where(TaskData.class)
                        .equalTo("day", day)
                        .equalTo("month", month)
                        .equalTo("year", year)
                        .findFirst();
                if (taskData != null) {
                    lost = taskData.getCountLose();
                }


            }
        });

        return lost;
    }

    public static TaskData getDataContent(Realm realm, final long id) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                taskData = realm.where(TaskData.class).equalTo("id", id).findFirst();

            }
        });
        return taskData;
    }

    public static void deleteAll(Realm realm, final int day, final int month, final int year) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<TaskData> taskData = realm.where(TaskData.class).equalTo("day", day).equalTo("month", month)
                        .equalTo("year", year).findAll();
                taskData.deleteAllFromRealm();
            }
        });
    }

    public static boolean checkNotification(Realm realm, final long id) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TaskData taskData = realm.where(TaskData.class).equalTo("id", id).findFirst();
                assert taskData != null;
                isNotif = taskData.isNotif();


            }
        });
        return isNotif;


    }


    public static boolean checkLose(Realm realm, final long id) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TaskData taskData = realm.where(TaskData.class).equalTo("id", id).findFirst();
                assert taskData != null;
                lose = taskData.isLose();


            }
        });
        return lose;


    }

    public static void losed(Realm realm, final long id) {
        final int[] day = new int[1];
        final int[] month = new int[1];
        final int[] year = new int[1];
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TaskData taskData = realm.where(TaskData.class).equalTo("id", id).findFirst();
                assert taskData != null;
                taskData.setLose(true);
                day[0] = taskData.getDay();
                month[0] = taskData.getMonth();
                year[0] = taskData.getYear();


            }
        });
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                int lose;
                TaskData taskData = realm.where(TaskData.class)
                        .equalTo("day", day[0])
                        .equalTo("month", month[0])
                        .equalTo("year", year[0])
                        .findFirst();
                if (taskData != null) {
                    lose = taskData.getCountLose();
                    if (lose != 0) {
                        taskData.setCountLose(lose - 1);

                    }
                }
            }
        });

    }

    public static boolean checkPlan(Realm realm, final long id) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TaskData taskData = realm.where(TaskData.class).equalTo("id", id).findFirst();
                assert taskData != null;
                planTask = taskData.isPlanTask();

            }
        });
        return planTask;

    }

    public static void deleteItem(Realm realm, final long id) {
        final int[] day = new int[1];
        final int[] month = new int[1];
        final int[] year = new int[1];

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TaskData taskData = realm.where(TaskData.class).equalTo("id", id).findFirst();
                if (taskData != null) {
                    day[0] = taskData.getDay();
                    month[0] = taskData.getMonth();
                    year[0] = taskData.getYear();
                    taskData.deleteFromRealm();

                }
            }
        });
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                int lose;
                TaskData taskData = realm.where(TaskData.class)
                        .equalTo("day", day[0])
                        .equalTo("month", month[0])
                        .equalTo("year", year[0])
                        .findFirst();
                if (taskData != null) {
                    lose = taskData.getCountLose();
                    if (lose != 0) {
                        taskData.setCountLose(lose - 1);

                    }
                }
            }
        });
    }

    public static void add(Realm realm, final String task, final String timeStartHour, final String timeStartMinute, final String timeEndHour,
                           final String timeEndMinute, final boolean notif, final String notifBeforeStart, final Integer day, final Integer month,
                           final Integer year, final boolean planTask, final long id) {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TaskData taskData = realm.createObject(TaskData.class, id);
                taskData.setTimeStartHour(timeStartHour);
                taskData.setNotifBeforeStart(notifBeforeStart);
                taskData.setNotif(notif);
                taskData.setPlanTask(planTask);
                taskData.setTimeStartMinute(timeStartMinute);
                taskData.setTimeEndHour(timeEndHour);
                taskData.setTimeEndMinute(timeEndMinute);
                taskData.setTask(task);
                taskData.setDay(day);
                taskData.setMonth(month);
                taskData.setYear(year);


            }
        });
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                int lose;
                TaskData taskData = realm.where(TaskData.class)
                        .equalTo("day", day)
                        .equalTo("month", month)
                        .equalTo("year", year)
                        .findFirst();
                if (taskData != null) {
                    lose = taskData.getCountLose();
                    if (lose == 0) {
                        taskData.setCountLose(1);

                    } else {
                        lose++;
                        taskData.setCountLose(lose);
                    }
                } else {


                }
            }
        });


    }

    public static void update(Realm realm, final String task, final String timeStartHour, final String timeStartMinute, final String timeEndHour,
                              final String timeEndMinute, final boolean checkNotif, final String notifBeforeStart,
                              final Integer day, final Integer month, final Integer year, final int id, final Integer dayB, final Integer monthB, final Integer yearB) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TaskData taskData = realm.where(TaskData.class).equalTo("id", id).findFirst();
                assert taskData != null;
                taskData.setNotif(checkNotif);
                taskData.setNotifBeforeStart(notifBeforeStart);
                taskData.setTimeStartHour(timeStartHour);
                taskData.setTimeStartMinute(timeStartMinute);
                taskData.setTimeEndHour(timeEndHour);
                taskData.setDay(day);
                taskData.setMonth(month);
                taskData.setYear(year);
                taskData.setTimeEndMinute(timeEndMinute);
                taskData.setTask(task);
                TaskData taskData1 = realm.where(TaskData.class)
                        .equalTo("day", day)
                        .equalTo("month", month)
                        .equalTo("year", year)
                        .findFirst();
                assert taskData1 != null;
                Log.d("BadgeTag","day"+taskData1.getDay());
                Log.d("BadgeTag","lose "+taskData1.getCountLose());
                taskData1.setCountLose(taskData1.getCountLose() + 1);

            }
        });
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TaskData taskData = realm.where(TaskData.class)
                        .equalTo("day", dayB)
                        .equalTo("month", monthB)
                        .equalTo("year", yearB)
                        .findFirst();
                if (taskData != null) {
                    taskData.setCountLose(taskData.getCountLose() - 1);
                    Log.d("BadgeTag", "lastDay " + taskData.getDay());
                    Log.d("BadgeTag", "lastLose " + taskData.getCountLose());
                }
            }
        });


    }

    public static boolean reqCheck(Realm realm, final long id) {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TaskData taskData = realm.where(TaskData.class).equalTo("id", id).findFirst();
                assert taskData != null;
                check = taskData.isCheck();
            }
        });
        return check;
    }

    public static void checked(Realm realm, final long id) {
        final int[] day = new int[1];
        final int[] month = new int[1];
        final int[] year = new int[1];
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TaskData taskData = realm.where(TaskData.class).equalTo("id", id).findFirst();
                assert taskData != null;
                taskData.setCheck(true);

                day[0] = taskData.getDay();
                month[0] = taskData.getMonth();
                year[0] = taskData.getYear();


            }
        });
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                int lose;
                TaskData taskData = realm.where(TaskData.class)
                        .equalTo("day", day[0])
                        .equalTo("month", month[0])
                        .equalTo("year", year[0])
                        .findFirst();
                if (taskData != null) {
                    lose = taskData.getCountLose();
                    if (lose != 0) {
                        taskData.setCountLose(lose - 1);

                    }
                }
            }
        });

    }

    public static int incrementId(Realm realm) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Number number = realm.where(TaskData.class).max("id");
                if (number == null) {
                    key = 1;
                } else
                    key = number.intValue() + 1;
            }
        });
        return key;
    }

}
