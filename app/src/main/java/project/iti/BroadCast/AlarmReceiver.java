package project.iti.BroadCast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import project.iti.UI.Reminder.ReminderActivity;

public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(context, ReminderActivity.class);

        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("tripId", intent.getIntExtra("tripId", 0));
        context.startActivity(i);
        
    }
}
