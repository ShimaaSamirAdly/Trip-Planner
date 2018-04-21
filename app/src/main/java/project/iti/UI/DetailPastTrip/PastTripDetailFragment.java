package project.iti.UI.DetailPastTrip;


import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import project.iti.Adapter.PastTripDetailNotes;
import project.iti.Data.Model.Trip;
import project.iti.R;
import project.iti.SqlDb.DataBaseAdapter;
import project.iti.UI.EditTrip.EditTripFragment;
import project.iti.UI.Home.HomeFragment;
import project.iti.UI.Main.MainActivity;
import project.iti.Utility.AppUtil;
import retrofit2.http.HEAD;


/**
 * A simple {@link Fragment} subclass.
 */
public class PastTripDetailFragment extends Fragment {


    ImageView pastTripImage;
    ImageView pastTripMap;

    TextView tripName;
    TextView tripStatus;
    TextView startPoint;
    TextView endPoint;
    TextView date;
    TextView time;
    TextView type;

    TextView duration;
    TextView avgSpeed;

    TextView mode;


    Button showNotes;
    Button delete;
    Button showMap;
    View view;
    Trip trip;
    ListView notesListView;

    String points;
    String url;
    boolean builderCreated = false;
    boolean builderCreated2 = false;

    AlertDialog.Builder builder;

    AlertDialog ad;
    AlertDialog.Builder builder2;

    AlertDialog ad2;

    public PastTripDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_past_trip_detail, container, false);

        MainActivity.fragmentName=getResources().getString(R.string.detailsTrip);
        ((MainActivity)getContext()).toolbar.setTitle( MainActivity.fragmentName);


        trip = (Trip) getArguments().getSerializable("trip");

        pastTripImage = (ImageView) v.findViewById(R.id.imageView4_detail_past);


        tripName = (TextView) v.findViewById(R.id.text_view_detail_tName_detail_past);
        tripStatus = (TextView) v.findViewById(R.id.textView_detail_status_detail_past);
        startPoint = (TextView) v.findViewById(R.id.text_view_detail_start_point_detail_past);
        endPoint = (TextView) v.findViewById(R.id.text_view_detail_end_point_detail_past);
        date = (TextView) v.findViewById(R.id.text_view_detail_date_detail_past);
        time = (TextView) v.findViewById(R.id.text_view_detail_time_detail_past);
        type = (TextView) v.findViewById(R.id.text_view_detail_type_detail_past);
        mode = (TextView) v.findViewById(R.id.text_view_past_detail_trip_mode);
        duration = (TextView) v.findViewById(R.id.text_view_past_trip_detail_duration);
        avgSpeed = (TextView) v.findViewById(R.id.text_view_past_trip_detail_speed);

        showNotes = (Button) v.findViewById(R.id.button_show_notes_detail_past);

        showNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTripNotes();
            }
        });


        delete = (Button) v.findViewById(R.id.button_detail_delete_detail_past);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteTrip();
            }
        });


        showMap = (Button) v.findViewById(R.id.button_detail_past_show_map);

        showMap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showMapImage();

            }
        });
        return v;
    }

    private void showMapImage() {


        try {
            prepareDialogueBuilder();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        builder2.setView(pastTripMap);


        if (ad2 == null) {
            ad2 = builder2.create();
        }
        ad2.show();
    }

    private void prepareDialogueBuilder() throws ExecutionException, InterruptedException {

        if (!builderCreated2) {

            pastTripMap = new ImageView(getContext());

            pastTripMap.setImageBitmap(AppUtil.getBitmapFromByte(trip.getImgHistory()));

            builder2 = new AlertDialog.Builder(getContext());

            builder2.setTitle("Trip Map");
            builder2.setCancelable(true);
            builder2.setPositiveButton("Ok", null);

            builderCreated2 = true;
        }
    }

    private void deleteTrip() {
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(
                getContext());
                alertDialogBuilder.setTitle("5ly balk");
        alertDialogBuilder
                .setMessage("Are you sure ?")
                .setCancelable(false)
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DataBaseAdapter dba = new DataBaseAdapter(getContext());
                        dba.delete(trip.getId());
                        Toast.makeText(getContext(), "Trip Deleted Successfully", Toast.LENGTH_SHORT).show();
                        switchToHome();
                    }
                });
        // create alert dialog
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (trip.getImg() != null) {

            pastTripImage.setImageBitmap(AppUtil.getBitmapFromByte(trip.getImg()));
        }

        if (trip.getStatus().equals("cancelled")) {
            showMap.setVisibility(View.INVISIBLE);
        }

        tripName.setText(trip.getName());
        tripStatus.setText(trip.getStatus());
        startPoint.setText(trip.getStartPoint());
        endPoint.setText(trip.getEndPoint());

        SimpleDateFormat sd = new SimpleDateFormat("E dd/MM/yyyy");
        date.setText( sd.format(trip.getDateObject()));
        SimpleDateFormat sd2 = new SimpleDateFormat("hh:mm:a");
        time.setText( sd2.format(trip.getDateObject()));
        type.setText(trip.getType());


     //   Log.i("d",trip.getDuration());
     //   Log.i("av",trip.getAverageSpeed());
        if(trip.getStatus().equals("cancelled")||trip.getDuration()==null||trip.getAverageSpeed()==null)
        {
            duration.setText("-");
            avgSpeed.setText("-");
        }
        else
        {
            duration.setText(trip.getDuration());
            avgSpeed.setText(trip.getAverageSpeed());
        }



        mode.setText(trip.getRepeated());

    }



    private void showTripNotes() {
        createDialogueBuilder();

        builder.setView(view);
        if (ad == null) {
            ad = builder.create();
        }
        ad.show();
    }

    private void createDialogueBuilder() {
        if (!builderCreated) {

            view = getLayoutInflater().inflate(R.layout.recycler_view_test, null);

            RecyclerView rv = (RecyclerView) view.findViewById(R.id.recycler_view_notes);

            PastTripDetailNotes adapter = new PastTripDetailNotes(AppUtil.convertJsonToListOfNoteObject(trip.getNotesJasonString()), getContext());
            rv.setAdapter(adapter);
            rv.setLayoutManager(new LinearLayoutManager(getContext()));

            builder = new AlertDialog.Builder(getContext());

            builder.setTitle("Your Notes");
            builder.setIcon(R.drawable.note);
            builder.setCancelable(true);
            builder.setPositiveButton("Ok", null);
            builderCreated = true;
        }
    }
    public void switchToHome() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        fragmentTransaction.replace(R.id.container, homeFragment, "HomeFragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}


