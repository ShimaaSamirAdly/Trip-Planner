package project.iti.UI.PastTrip;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;

import project.iti.Adapter.PastTipScreenAdapter;
import project.iti.Data.Model.Trip;
import project.iti.Listener.OnTipListener;
import project.iti.R;
import project.iti.SqlDb.DataBaseAdapter;
import project.iti.UI.Main.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class PastTripFragment extends Fragment implements OnTipListener{


    private RecyclerView recyclerView;
    private PastTipScreenAdapter adapter;
    private ArrayList<Trip> pastTripList;

    public PastTripFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_past_trip, container, false);

        MainActivity.fragmentName=getResources().getString(R.string.pastTrip);
        ((MainActivity)getContext()).toolbar.setTitle( MainActivity.fragmentName);


        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_past);

        pastTripList = new ArrayList<>();
        adapter = new PastTipScreenAdapter(getContext(), pastTripList,recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);



        return v;
    }



    @Override
    public void onResume() {
        super.onResume();
        pastTripList.clear();
        getPastTrips();
    }





    private void getPastTrips() {
        DataBaseAdapter dba = new DataBaseAdapter(getContext());
         dba.selectTrips(this,"history");
    }

    @Override
    public void OnDeliverTrips(ArrayList<Trip> trips) {
        if (trips!= null) {

            Collections.reverse(trips);
            for (Trip tr : trips) {
                pastTripList.add(tr);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
