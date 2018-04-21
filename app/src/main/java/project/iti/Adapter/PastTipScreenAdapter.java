package project.iti.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import project.iti.Data.Model.Trip;
import project.iti.R;

import project.iti.SqlDb.DataBaseAdapter;
import project.iti.UI.Main.MainActivity;
import project.iti.Utility.AppUtil;

/**
 * Created by abanoub samy on 3/8/2018.
 */

public class PastTipScreenAdapter  extends  RecyclerView.Adapter<PastTipScreenAdapter.MyViewHolder> {


    private Context mContext;
    private ArrayList<Trip> allPastTrips;
    private RecyclerView mRecyclerView;


    public PastTipScreenAdapter(Context mContext, ArrayList<Trip> allPastTrips, RecyclerView mRecyclerView) {
        this.mContext = mContext;
        this.allPastTrips = allPastTrips;
        this.mRecyclerView = mRecyclerView;
    }

    @Override
    public PastTipScreenAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_past, parent, false);


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int itemPosition = mRecyclerView.getChildLayoutPosition(view);
                ((MainActivity) mContext).switchToHistoryTripDetail(allPastTrips.get(itemPosition));


            }
        });

        return new PastTipScreenAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PastTipScreenAdapter.MyViewHolder holder, final int position) {

        Trip t = allPastTrips.get(position);

        if(t.getImg()!=null)
        {


            holder.pastTripImage.setImageBitmap(AppUtil.getBitmapFromByte(t.getImg()));


        }

        holder.tripName.setText(t.getName());
        holder.startPoint.setText(t.getStartPoint());
        SimpleDateFormat sd = new SimpleDateFormat("E dd/MM/yyyy");
        holder.date.setText(sd.format(t.getDateObject()));
        holder.deleteTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePastTrip(position);
            }
        });



    }





    public  void  deletePastTrip(final int position)
    {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                mContext);


        alertDialogBuilder.setTitle("Delete Trip");


        alertDialogBuilder
                .setMessage("Are you sure ?")
                .setCancelable(false)
                .setPositiveButton("Cancel",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();

                    }
                })
                .setNegativeButton("Delete",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        DataBaseAdapter dba = new DataBaseAdapter(mContext);
                        dba.delete(allPastTrips.get(position).getId());
                        allPastTrips.remove(position);
                        notifyDataSetChanged();

                        Toast.makeText(mContext,"Trip Deleted Successfully",Toast.LENGTH_SHORT).show();



                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();





    }

    @Override
    public int getItemCount() {
        return allPastTrips.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tripName, startPoint, date;
        public Button deleteTrip;
        public ImageView pastTripImage;


        public MyViewHolder(View view) {
            super(view);


            pastTripImage = view.findViewById(R.id.imageView2_past);
            tripName = view.findViewById(R.id.text_view_trip_name_past);
            startPoint = view.findViewById(R.id.text_view_start_point_past);
            date = view.findViewById(R.id.text_view_date_past);
            deleteTrip = view.findViewById(R.id.button_delete_trip_past);


        }
    }



}
