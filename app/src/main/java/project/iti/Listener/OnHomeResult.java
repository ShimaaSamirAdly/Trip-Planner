package project.iti.Listener;

import java.util.ArrayList;

import project.iti.Data.Model.NoteModel;

/**
 * Created by asmaa on 02/27/2018.
 */

public interface OnHomeResult {
    void onSuccess(ArrayList<NoteModel> noteModels);
    void onFailure();
}
