package project.iti.UI.Reminder;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.channels.FileLock;
import java.util.Calendar;


import project.iti.BroadCast.InprogressReceiver;
import project.iti.Data.Model.Map.MapModel;
import project.iti.Data.Model.Trip;
import project.iti.R;

import project.iti.SqlDb.DataBaseAdapter;



import project.iti.Utility.AppUtil;



/**
 * Created by MMM on 3/16/2018.
 */

public class StartingTrip {

    Context context;
    DataBaseAdapter dba;
    String points;
    String url;
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    private Trip trip= new Trip();

    ReminderAlarmSettings alarm = new ReminderAlarmSettings();

    public StartingTrip(Context context){

        this.context = context;
        dba = new DataBaseAdapter(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.DONUT)
    public Intent startTrip(Trip trip){

//        db.selectTripById(this, trip.getId());
//        if(trip.getStatus().equals("up coming")) {
        getJson(trip);
        Log.i("trip type", trip.getType());
        Intent intent = getMap(trip);

        if(trip.getType().equals("Round Trip") && trip.getStatus().equals("up coming")){
            Log.i("trip type", trip.getType());
            trip.setStatus("In progress Round");
            //trip = setRoundTrip(trip);

        }else{

            trip.setStatus("In progress");
            Log.i("status", trip.getStatus());
//            trip.setStatus("done");
        }

        if(trip.getRepeated().equals("Monthly")){

            trip.setStatus("In progress");
            trip = setRepeating(trip);
        }

        dba.updateTrip(trip);

        inprogressNotification(trip);
        return intent;
    }

    @RequiresApi(api = Build.VERSION_CODES.DONUT)
    public Intent getMap(Trip trip){
        this.trip=trip;

        Uri mapIntentUri = Uri.parse("google.navigation:q="+trip.getEndPoint());

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapIntentUri);

        mapIntent.setPackage("com.google.android.apps.maps");
        mapIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        return mapIntent;
    }

    public void getJson(final Trip trip){

        Log.i("tripJSON", trip.getName());
        Log.i("tripJSON", trip.getLatitudeStartPoint());
        Log.i("tripJSON", trip.getLatitudeEndPoint());
        Log.i("tripJSON", trip.getLongtiudeEndPoint());
        Log.i("tripJSON", trip.getLongtiudeStartPoint());
        RequestQueue request = Volley.newRequestQueue(context);
        JsonObjectRequest json = new JsonObjectRequest(Request.Method.GET, "https://maps.googleapis.com/maps/api/directions/json?origin=" + trip.getLongtiudeStartPoint() + "," + trip.getLatitudeStartPoint() + "&destination=" + trip.getLongtiudeEndPoint() + "," + trip.getLatitudeEndPoint() + "", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray obj = response.getJSONArray("routes");

                    //condn hena ?????
                    JSONObject route = (JSONObject) obj.get(0);
                    JSONObject poly = (JSONObject) route.get("overview_polyline");
                    points = poly.getString("points");
                    Log.i("points", points);
                    url = "https://maps.googleapis.com/maps/api/staticmap?size=1000x1000&maptype=roadmap&path=enc:" + points;
                    Log.i("url", url);
                    //Log.i("imghistory", imgs.toString());
//                    getMapImg(url, trip);
                    MapModel mapModel = new MapModel();
                    Gson gsonObj = new Gson();
                    mapModel = gsonObj.fromJson(response.toString(), MapModel.class);
                    Log.i("MODELTEST", mapModel.getRoutes().get(0).getLegs().get(0).getDistance().getText());
                    String distance = mapModel.getRoutes().get(0).getLegs().get(0).getDistance().getText();
                    String[] disParts = distance.split(" ");
                    String disNumber = disParts[0];
                    Log.i("distance ", ""+disNumber );

                    float distance_ =Float.parseFloat(disNumber);
                    Log.i("dis=================== ", ""+distance_);

                    String duration = mapModel.getRoutes().get(0).getLegs().get(0).getDuration().getText();
                    String[] durParts = duration.split(" ");
                    String durNumber = durParts[0];
                    float duration_=Float.parseFloat(durNumber);
                    Log.i("duration ", ""+durNumber);
                    Log.i("dur=================== ", ""+duration_);

                    trip.setDuration(duration);
                    Float avgSpeed=distance_/duration_;
                    trip.setAverageSpeed(String.valueOf(avgSpeed)+" km/min");
                    Log.i("logloglgo",String.valueOf(avgSpeed)+" km/min");

                    getMapImg(url, trip);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {


                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("volley", "Error");
                    }
                }
        );

        request.add(json);
    }


    public void getMapImg(String url, final Trip trip){

        RequestQueue request = Volley.newRequestQueue(context);
        ImageRequest img = new ImageRequest(url, new Response.Listener<Bitmap>() {

            @Override
            public void onResponse(Bitmap response) {

                trip.setImgHistory(AppUtil.getPictureByteOfArray(response));

                dba.updateTrip(trip);

            }
        },0,0,null,
                new Response.ErrorListener() {


                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("volley", "Error");
                    }
                }
        );


        request.add(img);


    }

    public Trip setRoundTrip(Trip trip){

        String temp = trip.getLongtiudeStartPoint();
        trip.setLongtiudeStartPoint(trip.getLongtiudeEndPoint());
        trip.setLongtiudeEndPoint(temp);
        temp = trip.getLatitudeStartPoint();
        trip.setLatitudeStartPoint(trip.getLatitudeEndPoint());
        trip.setLatitudeStartPoint(temp);
        temp = trip.getStartPoint();
        trip.setStartPoint(trip.getEndPoint());
        trip.setEndPoint(temp);
        alarm.setRoundingAlarm(context, trip);
        return trip;
    }

    public Trip setRepeating(Trip trip){

        String[] date = trip.getDate().split("-");
        String[] time = trip.getTime().split(":");
        int year = Integer.parseInt(date[2]);
        int month = Integer.parseInt(date[1]);
        int day = Integer.parseInt(date[0]);
        int hours = Integer.parseInt(time[0]);
        int min = Integer.parseInt(time[1]);
        trip.setTime(hours+":"+min);
        Log.i("trip type", hours+":"+min);
        alarm.setAlarm(context, trip);

        Log.i("repeated", trip.getRepeated());
        //alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, l, AlarmManager.INTERVAL_DAY * 7, PendingIntent.getBroadcast(context, tripId, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        if(month == Calendar.DECEMBER){
            month = Calendar.JANUARY;
            //cal.set(Calendar.MONTH, month);
        }else{

            month++;
        }

        trip.setDate(day+"-"+month+"-"+year);
        Log.i("trip type", hours+":"+min);
        alarm.setAlarm(context, trip);

        return trip;
    }

    public void inprogressNotification(Trip trip){

        Intent i = new Intent(context, InprogressReceiver.class);
        i.putExtra("tripId", trip.getId());

        PendingIntent action = PendingIntent.getBroadcast(context, trip.getId(), i, 0);

        NotificationCompat.Builder noti = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(AppUtil.getBitmapFromByte(trip.getImg()))
                .setContentTitle(trip.getName())
                .setContentText("Running Now!")
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setOngoing(true)
                .addAction(R.drawable.ic_close, "End Trip", action)
                .setAutoCancel(true);

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(trip.getId(), noti.build());
    }
}
