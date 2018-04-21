package project.iti.UI.EditTrip;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.zip.Inflater;

import project.iti.Adapter.EditTripNotesAdapter;
import project.iti.Data.Model.NoteModel;
import project.iti.Data.Model.Trip;


import project.iti.Listener.OnTipListener;
import project.iti.R;
import project.iti.SqlDb.DataBaseAdapter;
import project.iti.UI.Home.HomeFragment;
import project.iti.UI.Main.MainActivity;
import project.iti.UI.Reminder.ReminderAlarmSettings;
import project.iti.Utility.AppUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditTripFragment extends Fragment implements OnTipListener {

    Trip trip;
    ArrayList<NoteModel> notes;

    int tripId;

    String flag;


    EditText tripName;


    Spinner tripType;
    Spinner tripRepeatition;

    SupportPlaceAutocompleteFragment startPoint;
    SupportPlaceAutocompleteFragment endPoint;

    TextView dateTv;
    TextView timeTv;


    EditText noteToAdd;
    Button selectDate;
    Button selectTime;
    Button editNotes;
    Button addNote;
    Button saveChanges;

    ReminderAlarmSettings alarm;


    RecyclerView rv;
    EditTripNotesAdapter adapter;

    AlertDialog.Builder builder;

    AlertDialog ad;


    View notesView;


    boolean builderCreated = false;

    public EditTripFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        alarm = new ReminderAlarmSettings();

        View v = inflater.inflate(R.layout.fragment_edit_trip, container, false);
        MainActivity.fragmentName = getResources().getString(R.string.editTrip);
        ((MainActivity) getContext()).toolbar.setTitle(MainActivity.fragmentName);


        tripName = (EditText) v.findViewById(R.id.editText_trip_name_ef);

        dateTv = (TextView) v.findViewById(R.id.text_view_date_ef);


        timeTv = (TextView) v.findViewById(R.id.text_view_time_ef);


        tripType = (Spinner) v.findViewById(R.id.spinner_ef);

        tripType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                trip.setType(adapterView.getItemAtPosition(i).toString());


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });

        tripRepeatition = (Spinner) v.findViewById(R.id.spinner_edit_repeation_option);


        tripRepeatition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                trip.setRepeated(adapterView.getItemAtPosition(i).toString());


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        startPoint = (SupportPlaceAutocompleteFragment) getChildFragmentManager().findFragmentById(R.id.frag_start_point_ef);


        startPoint.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onPlaceSelected(Place place) {

                trip.setStartPoint(place.getName().toString());
                trip.setLongtiudeStartPoint(String.valueOf(place.getLatLng().longitude));
                trip.setLatitudeStartPoint(String.valueOf(place.getLatLng().latitude));

            }

            @Override
            public void onError(Status status) {


            }
        });


        endPoint = (SupportPlaceAutocompleteFragment) getChildFragmentManager().findFragmentById(R.id.frag_end_point_ef);


        endPoint.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                trip.setEndPoint(place.getName().toString());
                trip.setLongtiudeEndPoint(String.valueOf(place.getLatLng().longitude));
                trip.setLatitudeEndPoint(String.valueOf(place.getLatLng().latitude));
            }

            @Override
            public void onError(Status status) {

            }
        });


        editNotes = (Button) v.findViewById(R.id.button_edit_note_ef);

        editNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTripNotes();
            }
        });
        selectDate = (Button) v.findViewById(R.id.button_select_date_ef);

        selectDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View view) {
                pickDate();
            }
        });


        selectTime = (Button) v.findViewById(R.id.button_select_time_ef);


        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickTime();
            }
        });


        saveChanges = (Button) v.findViewById(R.id.button_save_changes_ef);


        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                flag = "save";
                getTripByIdToCheckStatus(tripId);
                //   updateTripInDataBase();
//
//                alarm.cancelAlarm(getContext(), trip.getId());
//                alarm.setAlarm(getContext(), trip);

            }
        });

        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        tripId = getArguments().getInt("id");

        getTripByIdToCheckStatus(tripId);

//        trip = (Trip) getArguments().getSerializable("trip");
//        tripName.setText(trip.getName());
//
//        if (trip.getType().equals("One Direction")) {
//            tripType.setSelection(0);
//        } else {
//            tripType.setSelection(1);
//        }
//
//
//        if(trip.getRepeated().equals("None"))
//        {
//            tripRepeatition.setSelection(0);
//
//        }
//
//        else if(trip.getRepeated().equals("Daily"))
//        {
//            tripRepeatition.setSelection(1);
//        }
//
//        else if(trip.getRepeated().equals("Weekly"))
//        {
//            tripRepeatition.setSelection(2);
//        }
//
//        else
//        {
//            tripRepeatition.setSelection(3);
//        }
//
//        startPoint.setText(trip.getStartPoint());
//        endPoint.setText(trip.getEndPoint());
//        dateTv.setText(trip.getDate());
//        timeTv.setText(trip.getTime());

    }


    private void getTripByIdToCheckStatus(int tripId) {

        DataBaseAdapter dba = new DataBaseAdapter(getContext());
        dba.selectTripById(this, tripId);

    }




    private boolean checkTime() {



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



        if(cal.before(now)) {



            return true;
        }


        return  false;




    }


    public void editTripNotes() {

        prepareNotesView();


        builder.setView(notesView);
        if (ad == null) {
            ad = builder.create();
        }
        ad.show();


    }

    private void prepareNotesView() {

        if (!builderCreated) {

            notesView = getLayoutInflater().inflate(R.layout.edit_trip_notes, null);


            rv = (RecyclerView) notesView.findViewById(R.id.recycler_edit_notes);


            notes = AppUtil.convertJsonToListOfNoteObject(trip.getNotesJasonString());
            if(notes==null)
            {
                notes = new ArrayList<>();
            }

            if(notes==null)
            {
                notes = new ArrayList<>();
            }

            adapter = new EditTripNotesAdapter(notes, getContext(), rv);
            rv.setAdapter(adapter);
            rv.setLayoutManager(new LinearLayoutManager(getContext()));


            noteToAdd = (EditText) notesView.findViewById(R.id.edit_text_added_notes);
            addNote = (Button) notesView.findViewById(R.id.button_add_note);

            addNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addNoteToTheList();
                }
            });


            builder = new AlertDialog.Builder(getContext());


            builder.setTitle("Your Notes");
            builder.setIcon(R.drawable.note);

            builder.setCancelable(true);
            builder.setPositiveButton("Done", null);

            builderCreated = true;

        }

    }


    public void addNoteToTheList() {


        if (!TextUtils.isEmpty(noteToAdd.getText().toString())) {

            Log.i("note : ", noteToAdd.getText().toString());
            NoteModel n = new NoteModel();
            n.setNote(noteToAdd.getText().toString());
            n.setNoteStatus("unChecked");
            notes.add(n);
            noteToAdd.getText().clear();
            adapter.notifyDataSetChanged();

        }

    }


    private void updateTripInDataBase() {


        if (checkFieldsIfEmpty()) {


            Toast.makeText(getContext(), "Enter All Fields", Toast.LENGTH_SHORT).show();

        }


        else if (checkTime()) {

            Toast.makeText(getContext(), "Invalid Time ", Toast.LENGTH_SHORT).show();


        }


        else {

            trip.setName(tripName.getText().toString());
            trip.setNotesJasonString(AppUtil.convertNotesListToJsonString(notes));
            DataBaseAdapter dba = new DataBaseAdapter(getContext());
            dba.updateTrip(trip);



            alarm.cancelAlarm(getContext(), trip.getId());
            alarm.setAlarm(getContext(), trip);

            switchToHomeFragment();


        }


    }

    private void switchToHomeFragment() {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        HomeFragment homeFragment = new HomeFragment();

        fragmentTransaction.replace(R.id.container, homeFragment, "HomeFragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    public boolean checkFieldsIfEmpty() {

        if (TextUtils.isEmpty(tripName.getText()) || TextUtils.isEmpty(dateTv.getText())
                || TextUtils.isEmpty(timeTv.getText())) {
            return true;

        }

        return false;


    }


    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public void pickDate() {


        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


                        trip.setDate(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        dateTv.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);


                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        datePickerDialog.show();

    }


    public void pickTime() {


        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        trip.setTime(hourOfDay + ":" + minute);

                        timeTv.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);


        timePickerDialog.show();


    }

    @Override
    public void OnDeliverTrips(ArrayList<Trip> trips) {


        if (flag != null && flag.equals("save")) {

            Trip t = trips.get(0);
            if (t.getStatus().equals("up coming")) {

                Log.i("aaaaaa", "aaaaaaa");
                updateTripInDataBase();


            }else if(t.getStatus().equals("cancelled"))
            {
                Toast.makeText(getContext(), "Trip is Cancelled", Toast.LENGTH_SHORT).show();


            }



            else {
                Toast.makeText(getContext(), "Trip is Running", Toast.LENGTH_SHORT).show();
            }


        } else {

            trip = trips.get(0);
            tripName.setText(trip.getName());

            if (trip.getType().equals("One Direction")) {
                tripType.setSelection(0);
            } else {
                tripType.setSelection(1);
            }


            if (trip.getRepeated().equals("None")) {
                tripRepeatition.setSelection(0);

            } else if (trip.getRepeated().equals("Daily")) {
                tripRepeatition.setSelection(1);
            } else if (trip.getRepeated().equals("Weekly")) {
                tripRepeatition.setSelection(2);
            } else {
                tripRepeatition.setSelection(3);
            }

            startPoint.setText(trip.getStartPoint());
            endPoint.setText(trip.getEndPoint());
            dateTv.setText(trip.getDate());
            timeTv.setText(trip.getTime());

        }


    }
}
