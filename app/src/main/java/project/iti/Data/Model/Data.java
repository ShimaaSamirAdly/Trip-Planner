package project.iti.Data.Model;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by asmaa on 02/27/2018.
 */

@Parcel
public class Data {
    private ArrayList<NoteModel> noteModel;

    public ArrayList<NoteModel> getNoteModel() {
        return noteModel;
    }

    public void setNoteModel(ArrayList<NoteModel> noteModel) {
        this.noteModel = noteModel;
    }
}
