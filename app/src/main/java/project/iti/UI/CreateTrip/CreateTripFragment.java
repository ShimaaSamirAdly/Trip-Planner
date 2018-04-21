package project.iti.UI.CreateTrip;


import android.app.AlarmManager;

import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;

import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import project.iti.BroadCast.AlarmReceiver;
import project.iti.Data.Model.NoteModel;
import project.iti.Data.Model.Trip;
import project.iti.Listener.OnTipListener;
import project.iti.R;
import project.iti.Singleton.ApiUtilities;
import project.iti.SqlDb.DataBaseAdapter;

import project.iti.UI.Home.HomeFragment;
import project.iti.UI.Main.MainActivity;
import project.iti.UI.Reminder.ReminderAlarmSettings;
import project.iti.Utility.AppUtil;
import retrofit2.http.HEAD;

import android.support.v4.app.Fragment;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateTripFragment extends Fragment implements OnTipListener {

    EditText tripName;

    Spinner tripType;
    Spinner tripRepeation;

    SupportPlaceAutocompleteFragment startPoint;
    SupportPlaceAutocompleteFragment endPoint;

    TextView dateTv;
    TextView timeTv;

    Button selectDate;
    Button selectTime;
    Button addNote;
    Button createTrip;

    LinearLayout notesLinearLayout;
    int numberOfNotes = 0;

    Bitmap bitmap;
    String selectedTripType;
    String selectedTripMode;
    String selectedStartPoint;
    String longitudeSelectedStartPoint;
    String latitudeSelectedStartPoint;
    String selectedEndPoint;

    String longitudeSelectedEndPoint;
    String latitudeSelectedEndPoint;

    String selectedDate;
    String selectedTime;

    Button cancelAlarm;




    DataBaseAdapter dba = new DataBaseAdapter(getContext());
    ReminderAlarmSettings r = new ReminderAlarmSettings();

    ArrayList<NoteModel> tripNotesList;


    byte[] byteArray;

    static int j = 0;

    public CreateTripFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_create_trip, container, false);

        MainActivity.fragmentName=getResources().getString(R.string.createTtip);
        ((MainActivity)getContext()).toolbar.setTitle( MainActivity.fragmentName);

        notesLinearLayout = (LinearLayout) v.findViewById(R.id.linear_layout_notes);


        tripName = v.findViewById(R.id.editText_trip_name);

        dateTv = (TextView) v.findViewById(R.id.text_view_date);
        timeTv = (TextView) v.findViewById(R.id.text_view_time);

        startPoint = (SupportPlaceAutocompleteFragment) getChildFragmentManager().findFragmentById(R.id.frag_start_point);
        startPoint.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onPlaceSelected(Place place) {


                selectedStartPoint = place.getName().toString();
                longitudeSelectedStartPoint = String.valueOf(place.getLatLng().longitude);
                latitudeSelectedStartPoint = String.valueOf(place.getLatLng().latitude);
            }
            @Override
            public void onError(Status status) {

            }
        });

        endPoint = (SupportPlaceAutocompleteFragment) getChildFragmentManager().findFragmentById(R.id.frag_end_point);
        endPoint.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                selectedEndPoint = place.getName().toString();
                longitudeSelectedEndPoint = String.valueOf(place.getLatLng().longitude);
                latitudeSelectedEndPoint = String.valueOf(place.getLatLng().latitude);

                Log.i("placeId ======= ", place.getId());

                getPhotos(place.getId());
            }

            @Override
            public void onError(Status status) {

            }
        });

        addNote = (Button) v.findViewById(R.id.button_add_note);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addExtraNote();
            }
        });
        createTrip = v.findViewById(R.id.button_create_trib);
        createTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertTripToDataBase();
            }
        });


        selectDate = (Button) v.findViewById(R.id.button_select_date);

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDate();
            }
        });

        selectTime = (Button) v.findViewById(R.id.button_select_time);

        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickTime();
            }
        });

        tripType = (Spinner) v.findViewById(R.id.spinner);

        tripType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selectedTripType = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        tripRepeation = (Spinner) v.findViewById(R.id.spinner_repeation_option);
        tripRepeation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {



                selectedTripMode = adapterView.getItemAtPosition(i).toString();


            selectedTripMode = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Log.i("sprite", "sprite");
        return v;
    }

    public void pickDate() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        selectedDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

                        dateTv.setText(selectedDate);

                    }
                }, mYear, mMonth, mDay);


        //  datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);


        datePickerDialog.show();
    }


    public void pickTime() {
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        selectedTime = hourOfDay + ":" + minute;

                        timeTv.setText(selectedTime);
                    }
                }, mHour, mMinute, false);

        timePickerDialog.show();
    }

    public void addExtraNote() {

        if (numberOfNotes < 5) {

            EditText e = new EditText(getContext());
            e.setTextSize(12);
            e.setTextColor(Color.WHITE);
            e.setHint("Type Note");
            e.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            e.setPadding(20, 20, 20, 20);

            notesLinearLayout.addView(e);
            numberOfNotes++;
        }
    }

    private void insertTripToDataBase() {
        if (checkFieldsIfEmpty()) {
            Toast.makeText(getContext(), "Enter All Fields", Toast.LENGTH_SHORT).show();


        }

        else if (checkTime()) {

            Toast.makeText(getContext(), "Invalid Time ", Toast.LENGTH_SHORT).show();


        }



        else {


            fillTripNotes();
            Trip t = new Trip();
            t.setName(tripName.getText().toString());
            t.setType(selectedTripType);
            t.setStartPoint(selectedStartPoint);
            t.setLongtiudeStartPoint(longitudeSelectedStartPoint);
            t.setLatitudeStartPoint(latitudeSelectedStartPoint);
            t.setEndPoint(selectedEndPoint);
            t.setLongtiudeEndPoint(longitudeSelectedEndPoint);
            t.setLatitudeEndPoint(latitudeSelectedEndPoint);
            t.setDate(selectedDate);
            t.setTime(selectedTime);
            t.setStatus("up coming");
            t.setNotesJasonString(AppUtil.convertNotesListToJsonString(tripNotesList));
            t.setImg(AppUtil.getPictureByteOfArray(bitmap));
            t.setUserEmail(ApiUtilities.getInstance().getRefrences(getContext()).getUser().getEmail());
            t.setRepeated(selectedTripMode);
            DataBaseAdapter dba = new DataBaseAdapter(getContext());
            dba.insertTrip(t);

            Log.i("trip", t.getName());


            dba.selectTripById(this, dba.selectLastTripId());


        }
    }

    private boolean checkTime() {



        String[] time = selectedTime.split(":");
        String[] date = selectedDate.split("-");
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




    public boolean checkFieldsIfEmpty() {
        if (TextUtils.isEmpty(tripName.getText()) || TextUtils.isEmpty(dateTv.getText())
                || TextUtils.isEmpty(timeTv.getText())
                || selectedStartPoint == null
                || selectedEndPoint == null) {
            return true;

        }
        return false;
    }

    public void fillTripNotes() {
        tripNotesList = new ArrayList<>();
        for (int j = 0; j < notesLinearLayout.getChildCount(); j++) {
            EditText e = (EditText) notesLinearLayout.getChildAt(j);
            if (!TextUtils.isEmpty(e.getText().toString())) {
                NoteModel nm = new NoteModel();
                nm.setNote(e.getText().toString());
                nm.setNoteStatus("unChecked");
                tripNotesList.add(nm);
            }

        }

    }




    private void getPhotos(String placeId) {

        final GeoDataClient mGeoDataClient = Places.getGeoDataClient(getContext(), null);
        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(placeId);
        if (photoMetadataResponse != null) {
            photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
                @Override
                public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                    PlacePhotoMetadataResponse photos = task.getResult();

//                if (photos!=null){
                    PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();

                    if (photoMetadataBuffer.getCount() != 0) {
                        PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(0);
                        Log.i("metaaaa", photoMetadata + "");
                        CharSequence attribution = photoMetadata.getAttributions();


                        Log.i("attributiooooooon", attribution + "");
                        Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getPhoto(photoMetadata);


                        photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
                            @Override
                            public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                                PlacePhotoResponse photo = task.getResult();


                                bitmap = photo.getBitmap();



                                Log.i("photto", "phot");


                            }


                        });
                    }
                }
            });
        }
    }

    @Override
    public void OnDeliverTrips(ArrayList<Trip> trips) {


        Log.i("tttrips size", trips.size() + "");
        r.setAlarm(getContext(), trips.get(0));

        switchToHomeFragment();



        Log.i("tttrips size",trips.size()+"");
        r.setAlarm(getContext(), trips.get(0));
        switchToHomeFragment();

    }

    private void switchToHomeFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        fragmentTransaction.replace(R.id.container, homeFragment, "HomeFragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}
