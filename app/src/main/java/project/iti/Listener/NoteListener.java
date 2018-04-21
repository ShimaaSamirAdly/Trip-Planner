package project.iti.Listener;

import project.iti.Data.Model.NoteModel;

/**
 * Created by asmaa on 02/27/2018.
 */

public interface NoteListener {
    void onItemChange(NoteModel homeModel, int position);
}
