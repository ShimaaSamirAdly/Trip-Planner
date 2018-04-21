package project.iti.SqlDb;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import project.iti.Data.Model.Trip;
import project.iti.Listener.OnTipListener;
import project.iti.Utility.AppUtil;

/**
 * Created by abanoub samy on 3/16/2018.
 */

public class TripFetcher extends Thread {
    private final OnTipListener mListener;
    private final Cursor c;

    public TripFetcher(OnTipListener listener, Cursor c) {
        mListener = listener;
        this.c = c;

    }

    @Override
    public void run() {
        //Select all data
        // checking database is not empty

        ArrayList<Trip> retrievedTrip = null;
        if (c.getCount() > 0) {


            retrievedTrip = new ArrayList<>();

            while (c.moveToNext()) {
                Trip t = new Trip();
                t.setId(c.getInt(0));
                t.setName(c.getString(1));
                t.setStartPoint(c.getString(2));
                t.setLongtiudeStartPoint(c.getString(3));
                t.setLatitudeStartPoint(c.getString(4));
                t.setEndPoint(c.getString(5));
                t.setLongtiudeEndPoint(c.getString(6));
                t.setLatitudeEndPoint(c.getString(7));
                t.setDate(c.getString(8));
                t.setTime(c.getString(9));
                t.setNotesJasonString(c.getString(10));
                t.setType(c.getString(11));
                t.setStatus(c.getString(12));
                t.setImg((c.getBlob(13)));
                t.setUserEmail(c.getString(14));
                t.setImgHistory((c.getBlob(15)));
                t.setRepeated(c.getString(16));
                t.setDuration(c.getString(17));
                t.setAverageSpeed(c.getString(18));
                retrievedTrip.add(t);
            }
            retrievedTrip = orderTripsByDate(retrievedTrip);
        }

        publishList(retrievedTrip);
        c.close();
    }


    public ArrayList<Trip> orderTripsByDate(ArrayList<Trip> retrievedTrips) {

        for (int i = 0; i < retrievedTrips.size(); i++) {

            SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm");
            try {
                Date d =sdf.parse(retrievedTrips.get(i).getDate()+" "+retrievedTrips.get(i).getTime());

                retrievedTrips.get(i).setDateObject(d);
                Log.i("Date is :", retrievedTrips.get(i).getDateObject().toString());
            } catch (ParseException e) {
                Log.i("catch :", "msh by3ml format asln");
                e.printStackTrace();
            }

        }

        Collections.sort(retrievedTrips);
        return retrievedTrips;
    }

    public void publishList(final ArrayList <Trip>  retrievedTripList ) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                // passing data through onDeliverData()
                mListener.OnDeliverTrips(retrievedTripList);
            }
        });
    }
}