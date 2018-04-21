package project.iti.UI.Home;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import project.iti.Adapter.MainScreenAdapter;
import project.iti.Data.Model.Trip;
import project.iti.Listener.OnTipListener;
import project.iti.R;
import project.iti.SqlDb.DataBaseAdapter;
import project.iti.UI.Main.MainActivity;


public class HomeFragment extends Fragment implements OnTipListener{

    private RecyclerView recyclerView;
    private MainScreenAdapter adapter;
    private ArrayList<Trip> tripList;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);

        MainActivity.fragmentName=getResources().getString(R.string.home);
        ((MainActivity)getContext()).toolbar.setTitle( MainActivity.fragmentName);

        tripList = new ArrayList<>();
        adapter = new MainScreenAdapter(getContext(), tripList,recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);





        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        tripList.clear();
        getTrips();
    }

    private void getTrips() {

        DataBaseAdapter dba = new DataBaseAdapter(getContext());
        dba.selectTrips(this,"home");



    }


    @Override
    public void OnDeliverTrips(ArrayList<Trip> trips) {
        if (trips != null) {
            for (Trip tr : trips) {
                tripList.add(tr);
            }

        }
        adapter.notifyDataSetChanged();
    }


}
