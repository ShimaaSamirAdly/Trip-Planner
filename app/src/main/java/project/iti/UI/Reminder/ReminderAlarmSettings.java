package project.iti.UI.Reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import project.iti.BroadCast.AlarmReceiver;
import project.iti.Data.Model.Trip;
import project.iti.SqlDb.DataBaseAdapter;

/**
 * Created by MMM on 3/13/2018.
 */

public class ReminderAlarmSettings {

    public void setAlarm(Context context, Trip trip){


        String[] time = trip.getTime().split(":");
        String[] date = trip.getDate().split("-");
        int hours = Integer.parseInt(time[0]);
        int min = Integer.parseInt(time[1]);
        int year = Integer.parseInt(date[2]);
        int month = Integer.parseInt(date[1]);
        int day = Integer.parseInt(date[0]);

        Calendar now = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month-1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hours);
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, 0);

            long l = cal.getTimeInMillis();
            int tripId = trip.getId();
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);

            intent.putExtra("tripId", tripId);

            AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            if(trip.getRepeated().equals("Daily")){

                Log.i("repeated", trip.getRepeated());
                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, l, AlarmManager.INTERVAL_DAY, PendingIntent.getBroadcast(context, tripId, intent, PendingIntent.FLAG_UPDATE_CURRENT));

            }else if(trip.getRepeated().equals("Weekly")){

                Log.i("repeated", trip.getRepeated());
                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, l, AlarmManager.INTERVAL_DAY*7, PendingIntent.getBroadcast(context, tripId, intent, PendingIntent.FLAG_UPDATE_CURRENT));

            }else {

                alarmMgr.set(AlarmManager.RTC_WAKEUP, l, PendingIntent.getBroadcast(context, tripId, intent, PendingIntent.FLAG_UPDATE_CURRENT));
            }

    }

    public void cancelAlarm(Context context, int id){

        Intent intent = new Intent(context, AlarmReceiver.class);

        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent p = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.cancel(p);
    }


    public void setRoundingAlarm(Context context, Trip trip){

        Calendar calendar = Calendar.getInstance();
        Long time = calendar.getTimeInMillis();

        int tripId = trip.getId();
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);

        intent.putExtra("tripId", tripId);

        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmMgr.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(context, tripId, intent, PendingIntent.FLAG_UPDATE_CURRENT));
    }
}
