package project.iti.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;

import project.iti.Data.Model.NoteModel;
import project.iti.Listener.NoteListener;
import project.iti.R;

/**
 * Created by asmaa on 02/26/2018.
 */

public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Context context;
    private NoteListener noteListener;
    private View itemView;
    public TextView tvNote;
    public SwitchButton switchButton;
    private int position;

//    switchButton = mFloatingWidgetView.findViewById(R.id.btnSwitch);

    private NoteModel homeModel;

    public NoteViewHolder(View itemView , NoteListener noteListener, Context context) {
        super(itemView);
        this.context=context;
        this.noteListener = noteListener;
        this.itemView= itemView;
        itemView.setOnClickListener(this);
        initializeViews();

    }

    private void initializeViews() {

        tvNote = itemView.findViewById(R.id.tvNote);
        switchButton = itemView.findViewById(R.id.btnSwitch);
//        switchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (switchButton.isChecked()) {
//                    Log.i("infoNote====  ", "" + switchButton.isChecked());
//
//                    switchButton.setChecked(false);
//                }
//                else {
//                    switchButton.setChecked(true);
//                    Log.i("infoNote====  ", "" + switchButton.isChecked());
//
//                }
//            }
//        });
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    homeModel.setNoteStatus("checked");
                    Log.i("change====  ",""+switchButton.isChecked());
                    noteListener.onItemChange(homeModel, position);
                } else {
                    Log.i("change====  ",""+switchButton.isChecked());
                    homeModel.setNoteStatus("unChecked");
                    noteListener.onItemChange(homeModel,position);
                }

            }
        });

    }

    public void setData(NoteModel data , int Position){
        this.homeModel=data;
        this.position=position;
        tvNote.setText(String.valueOf(homeModel.getNote()));
        if (homeModel.getNoteStatus().equals("checked")) {
            switchButton.setChecked(true);
            Log.i("infoNote====sssssss  ",""+switchButton.isChecked());
        }
        else { switchButton.setChecked(false);
            Log.i("infoNote====ssssss  ",""+switchButton.isChecked());}
    }

    @Override
    public void onClick(View view) {
        noteListener.onItemChange(homeModel,position);

    }
}
