package project.iti.Adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import project.iti.BroadCast.AlarmReceiver;
import project.iti.BroadCast.InprogressReceiver;
import project.iti.Data.Model.Trip;
import project.iti.R;
import project.iti.Service.NoteService;
import project.iti.SqlDb.DataBaseAdapter;
import project.iti.UI.Main.MainActivity;
import project.iti.UI.Reminder.ReminderActivity;
import project.iti.UI.Reminder.ReminderAlarmSettings;
import project.iti.UI.Reminder.StartingTrip;
import project.iti.Utility.AppUtil;

import static android.app.Activity.RESULT_OK;
import static project.iti.UI.Reminder.ReminderActivity.SERVICE_NOTE;

/**
 * Created by abanoub samy on 3/5/2018.
 */

public class MainScreenAdapter extends RecyclerView.Adapter<MainScreenAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Trip> allTrips;
    private RecyclerView mRecyclerView;
    ReminderAlarmSettings r;
    StartingTrip startingTrip;
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;

    public MainScreenAdapter(Context mContext, ArrayList<Trip> allTrips, RecyclerView mRecyclerView) {
        this.mContext = mContext;
        this.allTrips = allTrips;
        this.mRecyclerView = mRecyclerView;
        r = new ReminderAlarmSettings();
        startingTrip = new StartingTrip(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_home, parent, false);


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int itemPosition = mRecyclerView.getChildLayoutPosition(view);
                ((MainActivity) mContext).switchToUpComingTripDetail(allTrips.get(itemPosition));


            }
        });

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Trip t = allTrips.get(position);


        if (t.getImg() != null) {
            holder.tripImage.setImageBitmap(AppUtil.getBitmapFromByte(t.getImg()));
        }

        if (t.getImg() != null) {
            holder.tripImage.setImageBitmap(AppUtil.getBitmapFromByte(t.getImg()));
        }

        holder.tripName.setText(t.getName());
        holder.status.setText(t.getStatus());
        holder.startPoint.setText(t.getStartPoint());
        SimpleDateFormat sd = new SimpleDateFormat("E dd/MM/yyyy");
        holder.date.setText(sd.format(t.getDateObject()));
        holder.menuImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.menuImage, position);
            }
        });

        holder.startTripBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Intent intent = startingTrip.startTrip(t);
                r.cancelAlarm(mContext, t.getId());
                mContext.startService(new Intent(mContext, NoteService.class).putExtra(SERVICE_NOTE, t.getId()));
                ((MainActivity)mContext).finish();
                mContext.startActivity(intent);
            }
        });

         holder.endTripBtn.setVisibility(View.GONE);


         holder.endTripBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent i = new Intent(mContext, InprogressReceiver.class);
                 i.putExtra("tripId", t.getId());
                 mContext.sendBroadcast(i);
             }
         });

        if (t.getStatus().equals("In progress") || t.getStatus().equals("In progress Round")) {

            holder.startTripBtn.setVisibility(View.GONE);
            holder.menuImage.setVisibility(View.GONE);
            holder.endTripBtn.setVisibility(View.VISIBLE);

        }
    }




    @Override
    public int getItemCount() {
        return allTrips.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tripName, startPoint, date, status;
        public ImageView menuImage;
        public CircleImageView tripImage;
        public Button startTripBtn;
        public Button endTripBtn;


        public MyViewHolder(View view) {
            super(view);

            tripImage = view.findViewById(R.id.imageView_upcoming_trip);
            menuImage = view.findViewById(R.id.overflow);
            tripName = view.findViewById(R.id.text_view_trip_name);
            status = view.findViewById(R.id.textView_detail_status);
            startPoint = view.findViewById(R.id.text_view_start_point);
            date = view.findViewById(R.id.text_view_date);
            startTripBtn = view.findViewById(R.id.button_start_trip);
            endTripBtn = view.findViewById(R.id.button_end_trip);


        }
    }


    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view, int position) {

        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_trip, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position));
        popup.show();

    }


    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        int position;

        public MyMenuItemClickListener(int position) {

            this.position = position;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_edit_trip:
                    ((MainActivity) mContext).switchToEditTrip(allTrips.get(position));
                    return true;
                case R.id.action_delete_trip:
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            mContext);
                    alertDialogBuilder.setTitle("Delete Trip");


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
                                    DataBaseAdapter dba = new DataBaseAdapter(mContext);
                                    dba.delete(allTrips.get(position).getId());
                                    r.cancelAlarm(mContext, allTrips.get(position).getId());
                                    allTrips.remove(position);
                                    notifyDataSetChanged();

                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();


                    return true;
                default:
            }
            return false;
        }
    }


}
