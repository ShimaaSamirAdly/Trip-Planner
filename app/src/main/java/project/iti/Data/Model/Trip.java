package project.iti.Data.Model;


import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by abanoub samy on 3/3/2018.
 */

@Parcel
public class Trip implements Serializable,Comparable{


    private int id;
    private String name;
    private String startPoint;
    private String longtiudeStartPoint;
    private String latitudeStartPoint;
    private String endPoint;
    private String longtiudeEndPoint;
    private String latitudeEndPoint;
    private String date;
    private Date dateObject;
    private String time;
    private String type;
    private String status;
    private byte[] img;
    private byte[] imgHistory;
    private String notesJasonString;
    private ArrayList<String>notes;
    private String userEmail;
    private String repeated;
    private String duration;
    private String averageSpeed;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getLongtiudeStartPoint() {
        return longtiudeStartPoint;
    }

    public void setLongtiudeStartPoint(String longtiudeStartPoint) {
        this.longtiudeStartPoint = longtiudeStartPoint;
    }

    public String getLatitudeStartPoint() {
        return latitudeStartPoint;
    }

    public void setLatitudeStartPoint(String latitudeStartPoint) {
        this.latitudeStartPoint = latitudeStartPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getLongtiudeEndPoint() {
        return longtiudeEndPoint;
    }

    public void setLongtiudeEndPoint(String longtiudeEndPoint) {
        this.longtiudeEndPoint = longtiudeEndPoint;
    }

    public String getLatitudeEndPoint() {
        return latitudeEndPoint;
    }

    public void setLatitudeEndPoint(String latitudeEndPoint) {
        this.latitudeEndPoint = latitudeEndPoint;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }



    public void setTime(String time) {
        this.time = time;
    }



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public byte[] getImgHistory() {
        return imgHistory;
    }

    public void setImgHistory(byte[] imgHistory) {
        this.imgHistory = imgHistory;
    }

    public String getNotesJasonString() {
        return notesJasonString;
    }

    public void setNotesJasonString(String notesJasonString) {
        this.notesJasonString = notesJasonString;
    }

    public ArrayList<String> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<String> notes) {
        this.notes = notes;
    }


    public Date getDateObject() {
        return dateObject;
    }

    public void setDateObject(Date dateObject) {
        this.dateObject = dateObject;
    }


    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }


    public String getRepeated() {
        return repeated;
    }

    public void setRepeated(String repeated) {
        this.repeated = repeated;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(String averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return this.getDateObject().compareTo(((Trip)o).getDateObject());
    }
}
