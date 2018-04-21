package project.iti.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import project.iti.Data.Model.NoteModel;
import project.iti.R;

/**
 * Created by abanoub samy on 3/16/2018.
 */


public class PastTripDetailNotes extends RecyclerView.Adapter<PastTripDetailNotes.MyViewHolder> {

    ArrayList<NoteModel> notes;
    Context mContext;



    public PastTripDetailNotes(ArrayList<NoteModel> notes, Context mContext) {
        this.notes = notes;
        this.mContext = mContext;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.past_trip_detail_notes, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.i("totaa3",notes.get(position).getNote());
        holder.noteName.setText(notes.get(position).getNote());

        if(notes.get(position).getNoteStatus().equals("checked"))
        {
            holder.status.setImageResource(R.drawable.done);

        }



    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView noteName;
        public ImageView status;

        public MyViewHolder(View itemView) {
            super(itemView);
            noteName = (TextView) itemView.findViewById(R.id.past_trip_note_name);
            status = (ImageView) itemView.findViewById(R.id.past_trip_note_img);

        }
    }

}