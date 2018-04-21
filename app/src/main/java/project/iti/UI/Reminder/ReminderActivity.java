package project.iti.UI.Reminder;

import android.Manifest;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.support.v4.app.ActivityCompat;
import android.widget.ImageView;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.parceler.Parcels;
import java.io.IOException;
import java.util.ArrayList;


import project.iti.Data.Model.Map.MapModel;
import project.iti.Data.Model.Map.Route;
import project.iti.Data.Model.Trip;
import project.iti.Listener.OnTipListener;
import project.iti.R;
import project.iti.Service.NoteService;
import project.iti.SqlDb.DataBaseAdapter;

import project.iti.UI.Home.HomeFragment;
import project.iti.UI.Main.MainActivity;

import project.iti.Utility.AppUtil;

public class ReminderActivity extends AppCompatActivity implements OnTipListener{

    ImageView tripImg;
    MediaPlayer ringMgr;
    TextView tripName;
    TextView startPoint;
    TextView endPoint;
    TextView time;
    Button start;
    Trip trip ;
    DataBaseAdapter dba;
    ReminderAlarmSettings alarm;
    StartingTrip st;

    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    public static final String SERVICE_NOTE="note";
    private Trip newTrip = new Trip();

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        //st = new StartingTrip(this);
        st = new StartingTrip(this);
        alarm = new ReminderAlarmSettings();

        dba = new DataBaseAdapter(this);
        tripImg = findViewById(R.id.tripImg);
        tripName = findViewById(R.id.tripName);
        startPoint = findViewById(R.id.startPoint);
        endPoint = findViewById(R.id.endPoint);
        time = findViewById(R.id.time);
        start = findViewById(R.id.startTrip);

        int tripId = getIntent().getIntExtra("tripId", 0);
        dba.selectTripById(this,tripId);

        start.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
               refreshCurrentFragment();
                startTrip(trip);
            }
        });


        this.setFinishOnTouchOutside(false);
        this.setTitle("Trip");
       // Uri ring = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

            
            AudioManager audio = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
//
            audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        int maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_RING);
        audio.setStreamVolume(AudioManager.STREAM_RING, maxVolume, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);

            ringMgr = MediaPlayer.create(this, R.raw.naghma);
            //ringMgr.setDataSource(this, ring);
//        ringMgr.setVolume(500, 500);

//            ringMgr.setAudioStreamType(AudioManager.STREAM_RING);
            ringMgr.setLooping(true);

            ringMgr.start();

    }

    private void refreshCurrentFragment() {


    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    public void startTrip(Trip trip) {
//        this.trip=trip;

//        newTrip= trip;
        finishAndRemoveTask();
        Log.i("tttttt", ""+trip.getId());

        ringMgr.stop();
        Intent intent = st.startTrip(trip);
        notePermission();
        startActivityForResult(intent, trip.getId());
    }

    private void notePermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intentI = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intentI, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else {
            startService(new Intent(ReminderActivity.this, NoteService.class).putExtra(SERVICE_NOTE, trip.getId()));
            finish();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            //Check if the permission is granted or not.
            startService(new Intent(ReminderActivity.this, NoteService.class).putExtra(SERVICE_NOTE, trip.getId()));
            finish();
            if (resultCode == RESULT_OK) {
            } else { //Permission is not available
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void later(View v){

        Log.i("statusssss",trip.getStatus());
            Intent intent = st.startTrip(trip);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, trip.getId(), intent, 0);
            NotificationCompat.Builder noti = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(AppUtil.getBitmapFromByte(trip.getImg()))
                    .setContentTitle(trip.getName())
                    .setContentText(trip.getTime())
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setOngoing(true)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            Toast.makeText(this, trip.getName(), Toast.LENGTH_SHORT).show();
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(trip.getId(), noti.build());

        Log.i("statusssss",trip.getStatus());
            ringMgr.stop();
        startService(new Intent(ReminderActivity.this, NoteService.class).putExtra(SERVICE_NOTE, trip.getId()));
        finish();
            finishAndRemoveTask();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void cancel(View v){

        if(trip.getRepeated().equals("Monthly")){

            trip = st.setRepeating(trip);
        }

        if(trip.getRepeated().equals("None")){

            trip.setStatus("cancelled");
        }else{

            trip.setStatus("In progress");
        }

        dba.updateTrip(trip);
        ringMgr.stop();
        finishAndRemoveTask();
    }



    @Override
    public void OnDeliverTrips(ArrayList<Trip> trips) {
        trip = trips.get(0);

        if(trip.getImg()!=null) {
            tripImg.setImageBitmap(AppUtil.getBitmapFromByte(trip.getImg()));
        }
        //trip.setStatus("up coming");
        Log.i("tripStatus", trip.getStatus());
        tripName.setText(trip.getName());
        startPoint.setText(trip.getStartPoint());
        endPoint.setText(trip.getEndPoint());
        time.setText(trip.getTime());
    }

}

