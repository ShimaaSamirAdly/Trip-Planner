package project.iti.UI.DetailTrip;


import android.app.AlertDialog;

import android.content.Intent;

import android.os.Build;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import project.iti.Adapter.UpComingTripNoteAdapter;
import project.iti.Data.Model.NoteModel;
import project.iti.Data.Model.Trip;
import project.iti.Listener.OnTipListener;
import project.iti.R;
import project.iti.SqlDb.DataBaseAdapter;
import project.iti.Service.NoteService;
import project.iti.UI.EditTrip.EditTripFragment;

import project.iti.UI.Main.MainActivity;
import project.iti.UI.Reminder.ReminderActivity;
import project.iti.UI.Reminder.ReminderAlarmSettings;
import project.iti.UI.Reminder.StartingTrip;
import project.iti.Utility.AppUtil;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpComingTripDetailFragment extends Fragment implements OnTipListener {


    ImageView upcomingTripImg;
    TextView tripName;
    TextView tripStatus;
    TextView startPoint;
    TextView endPoint;
    TextView date;
    TextView time;
    TextView type;
    TextView tripMode;
    StartingTrip startingTrip;
    ReminderAlarmSettings alarm;


    Button showNotes;
    Button edit;
    Button start;

    View view;


    Trip trip;
    int tripId;



    RecyclerView notesRecyclerView;

    UpComingTripNoteAdapter noteAdapter;
    DataBaseAdapter db;


    boolean builderCreated = false;

    AlertDialog.Builder builder;
    AlertDialog ad;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    public UpComingTripDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        startingTrip = new StartingTrip(getContext());
        db = new DataBaseAdapter(getContext());
        alarm = new ReminderAlarmSettings();
        View v = inflater.inflate(R.layout.fragment_up_coming_trip_detail, container, false);

        MainActivity.fragmentName = getResources().getString(R.string.upcomingTrip);
        ((MainActivity) getContext()).toolbar.setTitle(MainActivity.fragmentName);
        tripId = getArguments().getInt("id");

//        trip = (Trip) getArguments().getSerializable("trip");

        upcomingTripImg = (ImageView) v.findViewById(R.id.imageView4);
        tripName = (TextView) v.findViewById(R.id.text_view_detail_tName);
        tripStatus = (TextView) v.findViewById(R.id.textView_detail_status);
        startPoint = (TextView) v.findViewById(R.id.text_view_detail_start_point);
        endPoint = (TextView) v.findViewById(R.id.text_view_detail_end_point);
        date = (TextView) v.findViewById(R.id.text_view_detail_date);
        time = (TextView) v.findViewById(R.id.text_view_detail_time);
        type = (TextView) v.findViewById(R.id.text_view_detail_type);
        tripMode = (TextView) v.findViewById(R.id.text_view_past_detail_trip_mode);


        showNotes = (Button) v.findViewById(R.id.button_show_notes);


        showNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTripNotes();
            }
        });

        edit = (Button) v.findViewById(R.id.button_detail_edit);

        edit.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {



                  switchToEditTripFragment();

            }
        });


        start = v.findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {


     Intent intent = startingTrip.startTrip(trip);
                alarm.cancelAlarm(getContext(), trip.getId());
                getActivity().startService(new Intent(getActivity(), NoteService.class).putExtra(ReminderActivity.SERVICE_NOTE, trip.getId()));
                getActivity().finish();
                startActivity(intent);

            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getTripByIdToCheckStatus(tripId);

        Log.i("Resume","resume Created");
    }


    private void getTripByIdToCheckStatus(int tripId) {

        DataBaseAdapter dba = new DataBaseAdapter(getContext());
        dba.selectTripById(this, tripId);

    }


    public void switchToEditTripFragment() {


        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        EditTripFragment editTripFrag = new EditTripFragment();
        Bundle bundleobj = new Bundle();


        bundleobj.putInt("id", trip.getId());
        editTripFrag.setArguments(bundleobj);
        fragmentTransaction.replace(R.id.container, editTripFrag, "editTripFrag");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

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

            notesRecyclerView = view.findViewById(R.id.recycler_view_notes);
            notesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


            ArrayList<NoteModel>nm = AppUtil.convertJsonToListOfNoteObject(trip.getNotesJasonString());


           if(nm==null)
           {
               nm = new ArrayList<>();
           }

            noteAdapter = new UpComingTripNoteAdapter(nm, getContext());


            notesRecyclerView.setAdapter(noteAdapter);


            builder = new AlertDialog.Builder(getContext());


            builder.setTitle("Your Notes");
            builder.setIcon(R.drawable.note);
            builder.setCancelable(true);
            builder.setPositiveButton("Ok", null);

            builderCreated = true;
        }
    }


    @Override
    public void OnDeliverTrips(ArrayList<Trip> trips) {



        trip = trips.get(0);
        fillFragmentWithData();


    }


    public void fillFragmentWithData()
    {


        if (trip.getStatus().equals("In progress") || trip.getStatus().equals("In progress Round")||trip.getStatus().equals("cancelled")) {

//            edit.setEnabled(false);
//            start.setEnabled(false);

            edit.setVisibility(View.INVISIBLE);
            start.setVisibility(View.INVISIBLE);


        }


        if (trip.getImg() != null) {
            upcomingTripImg.setImageBitmap(AppUtil.getBitmapFromByte(trip.getImg()));

        }
        tripName.setText(trip.getName());
        tripStatus.setText(trip.getStatus());
        tripMode.setText(trip.getRepeated());
        startPoint.setText(trip.getStartPoint());
        endPoint.setText(trip.getEndPoint());

        SimpleDateFormat sd = new SimpleDateFormat("E dd/MM/yyyy");
        date.setText(sd.format(trip.getDateObject()));
        SimpleDateFormat sd2 = new SimpleDateFormat("hh:mm:a");
        time.setText(sd2.format(trip.getDateObject()));
        type.setText(trip.getType());

    }




}
