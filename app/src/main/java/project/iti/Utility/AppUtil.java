package project.iti.Utility;

import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import project.iti.Data.Model.NoteModel;
import project.iti.R;
import project.iti.UI.Home.HomeFragment;
import project.iti.UI.Main.MainActivity;

/**
 * Created by asmaa on 03/05/2018.
 */

public class AppUtil {

    public static byte[] getPictureByteOfArray(Bitmap bitmap) {
        if(bitmap !=null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

            return byteArrayOutputStream.toByteArray();
        }

    else {
            return null;
        }
    }

    public static Bitmap getBitmapFromByte(byte[] image) {
        if(image!=null) {
            return BitmapFactory.decodeByteArray(image, 0, image.length);
        }
        else
        {
            return  null;
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }


    public static String convertNotesListToJsonString(ArrayList<NoteModel> notesList) {
        String jsonString = null;

        if (notesList != null) {

            JSONArray jArray = new JSONArray();
            for (NoteModel noteModel : notesList) {
                JSONObject j = new JSONObject();

                try {
                    j.put(noteModel.getNote(), noteModel.getNoteStatus());
                    jArray.put(j);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


            JSONObject jObject = new JSONObject();
            try {
                jObject.put("All Notes", jArray);
                jsonString = jObject.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


        return jsonString;
    }


    public static ArrayList<NoteModel> convertJsonToListOfNoteObject(String notesJson) {
        ArrayList<NoteModel> notes = null;

        if (notesJson != null) {
            notes = new ArrayList<>();
            try {
                JSONObject jObject = new JSONObject(notesJson);
                JSONArray jArray = jObject.getJSONArray("All Notes");

                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jO = (JSONObject) jArray.get(i);
                    Iterator<String>key = jO.keys();
                    String noteName = key.next();
                    NoteModel n = new NoteModel();
                    n.setNote(noteName);
                    n.setNoteStatus(jO.getString(noteName));
                    notes.add(n);

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    //    Log.i("totaa2",notes.get(0).getNote());
        return notes;
    }







}
