package project.iti.Data.Model;

import org.parceler.Parcel;

import java.util.Map;

/**
 * Created by asmaa on 02/27/2018.
 */

@Parcel
public class NoteModel {


    private String note;
    private String noteStatus;


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNoteStatus() {
        return noteStatus;
    }

    public void setNoteStatus(String noteStatus) {
        this.noteStatus = noteStatus;
    }
}

