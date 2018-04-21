package project.iti.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import project.iti.Data.Model.NoteModel;
import project.iti.R;

/**
 * Created by abanoub samy on 3/13/2018.
 */

public class UpComingTripNoteAdapter extends RecyclerView.Adapter<UpComingTripNoteAdapter.MyViewHolder> {

    ArrayList<NoteModel> notes;
    Context mContext;


    public UpComingTripNoteAdapter(ArrayList<NoteModel> notes, Context mContext) {
        this.notes = notes;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_view, parent, false);

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
