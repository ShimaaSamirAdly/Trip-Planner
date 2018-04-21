package project.iti.Adapter;

import android.support.v7.widget.RecyclerView;

import project.iti.ViewHolder.NoteViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import project.iti.Data.Model.NoteModel;
import project.iti.Listener.NoteListener;
import project.iti.R;

/**
 * Created by asmaa on 02/26/2018.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder>{


    private ArrayList<NoteModel>  data;
    private NoteListener noteListener;
    ////TEST



    public NoteAdapter(ArrayList<NoteModel>  data , NoteListener noteListener){
        this.data=data;
        this.noteListener = noteListener;
    }
    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_note, parent, false);
        return new NoteViewHolder(view, noteListener,parent.getContext());
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        Log.i("log:=====","log");
         holder.setData(data.get(position),position);
//         data.get(position).getNote();
    }
    /////Asmaa Hassen Ibrahem Edit //////<<<>>>>>>>>

    @Override
    public int getItemCount() {
//        Log.i("sizee: ",""+data.size());
        return data.size();

    }
}
