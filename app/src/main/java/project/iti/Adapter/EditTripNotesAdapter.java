package project.iti.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import project.iti.Data.Model.NoteModel;
import project.iti.R;

/**
 * Created by abanoub samy on 3/13/2018.
 */

public class EditTripNotesAdapter extends RecyclerView.Adapter<EditTripNotesAdapter.MyViewHolder> {

    ArrayList<NoteModel> notes;
    Context mContext;
    RecyclerView mRecyclerView;


    public EditTripNotesAdapter(ArrayList<NoteModel> notes, Context mContext, RecyclerView mRecyclerView) {
        this.notes = notes;
        this.mContext = mContext;
        this.mRecyclerView = mRecyclerView;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_view, parent, false);

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                final int itemPosition = mRecyclerView.getChildLayoutPosition(view);


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        mContext);


                alertDialogBuilder.setTitle("Delete Note");


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

                                notes.remove(itemPosition);
                                notifyDataSetChanged();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();


                return false;
            }




        });
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.i("totaa3",notes.get(position).getNote());
        holder.noteName.setText(notes.get(position).getNote());


    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView noteName;

        public MyViewHolder(View itemView) {
            super(itemView);
            noteName = (TextView) itemView.findViewById(R.id.notTextView);

        }
    }

}
