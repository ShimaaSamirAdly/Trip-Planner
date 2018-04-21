package project.iti.BroadCast;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

import project.iti.Data.Model.Trip;
import project.iti.Listener.OnTipListener;
import project.iti.SqlDb.DataBaseAdapter;
import project.iti.UI.Main.MainActivity;
import project.iti.UI.Reminder.ReminderAlarmSettings;
import project.iti.UI.Reminder.StartingTrip;

public class InprogressReceiver extends BroadcastReceiver  implements OnTipListener {

    DataBaseAdapter dba;
    Context cont;
    StartingTrip startingTrip;

    @Override
    public void onReceive(Context context, Intent intent) {

        cont = context;
        dba = new DataBaseAdapter(context);
        startingTrip = new StartingTrip(context);

        int tripId = intent.getIntExtra("tripId", 0);

        dba.selectTripById(this, tripId);


    }

    @Override
    public void OnDeliverTrips(ArrayList<Trip> trips) {

        Trip trip = trips.get(0);

        if(trip.getType().equals("Round Trip") && trip.getStatus().equals("In progress Round")){
            Log.i("trip type", trip.getType());
//            trip.setStatus("In progress");
            trip = startingTrip.setRoundTrip(trip);

        }else {


            if (trip.getRepeated().equals("None")) {

                trip.setStatus("done");
            } else {

                trip.setStatus("In progress");
            }
        }

        //trip.setStatus("done");
        dba.updateTrip(trip);

        NotificationManager nm = (NotificationManager) cont.getSystemService(Context.NOTIFICATION_SERVICE);
        //i++;
        nm.cancel(trip.getId());

        Intent i = new Intent(cont, MainActivity.class);

        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        cont.startActivity(i);

    }
}
