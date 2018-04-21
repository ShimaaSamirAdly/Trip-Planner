package project.iti.SqlDb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.facebook.internal.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import project.iti.Data.Model.Trip;
import project.iti.Data.Model.User;
import project.iti.Listener.OnTipListener;
import project.iti.Listener.OnUserImageSqlLiteListener;
import project.iti.Singleton.ApiUtilities;
import project.iti.Utility.AppUtil;


/**
 * Created by abanoub samy on 3/3/2018.
 */


public class DataBaseAdapter {

    private final String TAG = this.getClass().getName();

    private Context context;
    static DataBaseHelper dbHelper;

    public DataBaseAdapter(Context _context) {
        dbHelper = new DataBaseHelper(_context);
        context = _context;
    }


    public boolean insertTrip(Trip trip) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DataBaseHelper.T_NAME, trip.getName());
        cv.put(DataBaseHelper.START_POINT, trip.getStartPoint());
        cv.put(DataBaseHelper.LONGTIUDE_START_POINT, trip.getLongtiudeStartPoint());
        cv.put(DataBaseHelper.LATITUDE_START_POINT, trip.getLatitudeStartPoint());
        cv.put(DataBaseHelper.END_POINT, trip.getEndPoint());
        cv.put(DataBaseHelper.LONGTIUDE_END_POINT, trip.getLongtiudeEndPoint());
        cv.put(DataBaseHelper.LATITUDE_END_POINT, trip.getLatitudeEndPoint());
        cv.put(DataBaseHelper.DATE, trip.getDate());
        cv.put(DataBaseHelper.TIME, trip.getTime());
        cv.put(DataBaseHelper.TYPE, trip.getType());
        cv.put(DataBaseHelper.NOTES, trip.getNotesJasonString());
        cv.put(DataBaseHelper.STATUS, trip.getStatus());
        cv.put(DataBaseHelper.IMAGE, (trip.getImg()));
        cv.put(DataBaseHelper.USER_EMAIL,trip.getUserEmail());

        cv.put(DataBaseHelper.IMAGE_HISTORY,(trip.getImgHistory()));
        cv.put(DataBaseHelper.REPEATED,trip.getRepeated());


        db.insert(DataBaseHelper.TABLE_NAME, null, cv);

        db.close();
        return true;

    }

    public ArrayList<Trip> selectTrips(OnTipListener listener,String status) {
        String selection = null;

        ArrayList<Trip> retrievedTrip = null;
        Cursor c;
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        Log.i("statussssss",status);

        if(status.equals("home"))
             {
             selection = DataBaseHelper.STATUS + " IN (?,?,?) AND " + DataBaseHelper.USER_EMAIL + " = ? ";
            String[]  selectionArgs= {"up coming","In progress","In progress Round",  ApiUtilities.getInstance().getRefrences(context).getUser().getEmail()};
            c = db.query(DataBaseHelper.TABLE_NAME, null,selection, selectionArgs, null, null, null);

        }

        else
        {

             selection = DataBaseHelper.STATUS + " IN (?,?) AND " + DataBaseHelper.USER_EMAIL + " = ? ";

            String[] selectionArgs = { "done","cancelled" , ApiUtilities.getInstance().getRefrences(context).getUser().getEmail()};
            c = db.query(DataBaseHelper.TABLE_NAME, null,selection, selectionArgs, null, null, null);

        }

        TripFetcher tripFetcher = new TripFetcher(listener,c);
        tripFetcher.start();

        return retrievedTrip;
    }

    public ArrayList<Trip> orderTripsByDate(ArrayList<Trip> retrievedTrips) {

        for (int i = 0; i < retrievedTrips.size(); i++) {

            SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm");
            try {
               Date d =sdf.parse(retrievedTrips.get(i).getDate()+" "+retrievedTrips.get(i).getTime());

                retrievedTrips.get(i).setDateObject(d);
                Log.i("Date is :", retrievedTrips.get(i).getDateObject().toString());
            } catch (ParseException e) {
                Log.i("catch :", "msh by3ml format asln");
                e.printStackTrace();
            }

        }

          Collections.sort(retrievedTrips);
        return retrievedTrips;
    }


    public boolean updateTrip(Trip trip) {
        Log.i("Done===DB=== ", "Done");

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Log.i("db", trip.getStatus());
        cv.put(DataBaseHelper.T_NAME, trip.getName());
        cv.put(DataBaseHelper.START_POINT, trip.getStartPoint());
        cv.put(DataBaseHelper.LONGTIUDE_START_POINT, trip.getLongtiudeStartPoint());
        cv.put(DataBaseHelper.LATITUDE_START_POINT, trip.getLatitudeStartPoint());
        cv.put(DataBaseHelper.END_POINT, trip.getEndPoint());
        cv.put(DataBaseHelper.LONGTIUDE_END_POINT, trip.getLongtiudeEndPoint());
        cv.put(DataBaseHelper.LATITUDE_END_POINT, trip.getLatitudeEndPoint());
        cv.put(DataBaseHelper.DATE, trip.getDate());
        cv.put(DataBaseHelper.TIME, trip.getTime());
        cv.put(DataBaseHelper.TYPE, trip.getType());
        cv.put(DataBaseHelper.NOTES, trip.getNotesJasonString());
        cv.put(DataBaseHelper.STATUS, trip.getStatus());
        cv.put(DataBaseHelper.IMAGE, (trip.getImg()));
        cv.put(DataBaseHelper.USER_EMAIL,trip.getUserEmail());
        cv.put(DataBaseHelper.IMAGE_HISTORY,(trip.getImgHistory()));
        cv.put(DataBaseHelper.REPEATED,trip.getRepeated());
        cv.put(DataBaseHelper.DURATION,trip.getDuration());
        cv.put(DataBaseHelper.AVERAGE_SPEED,trip.getAverageSpeed());

        db.update(DataBaseHelper.TABLE_NAME, cv, DataBaseHelper.ID + "='" + trip.getId() + "'", null);

        db.close();

        return true;
    }


    public boolean delete(int tripId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DataBaseHelper.TABLE_NAME, DataBaseHelper.ID + "='" + tripId + "'", null);
        db.close();
        return true;

    }


    public int selectLastTripId() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id FROM " + DataBaseHelper.TABLE_NAME + " WHERE " + DataBaseHelper.ID + " = (SELECT MAX(id) FROM Trip)", null);

        c.moveToNext();
        int lastId = c.getInt(0);



        c.close();
        db.close();


        return lastId ;


    }

    public Trip selectTripById(OnTipListener listener,int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + DataBaseHelper.TABLE_NAME + " WHERE id = "+id, null);

        TripFetcher t = new TripFetcher(listener,c);
        t.start();
        return null;

    }

    //asmaa database function

    public void addUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.KEY_NAME, user.getName());
        values.put(DataBaseHelper.KEY_EMAIL, user.getEmail());

        values.put(DataBaseHelper.KEY_FB_ID, user.getFbID());
        values.put(DataBaseHelper.KEY_FB_TOKEN, user.getFbToken());
        values.put(DataBaseHelper.KEY_PASSWORD, user.getPassword());
        values.put(DataBaseHelper.KEY_PH_NO, user.getPhone());
        values.put(DataBaseHelper.KEY_IMAGE, AppUtil.getPictureByteOfArray(user.getImg2()));

        // Inserting Row
        db.insert(DataBaseHelper.TABLE_USER, null, values);
        db.close(); // Closing database connection
        Log.i(TAG,"add User in  sqlLite Done");
    }

    // Getting single user
    public User getUser(String email , String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DataBaseHelper.TABLE_USER, new String[] {DataBaseHelper.KEY_ID ,DataBaseHelper.KEY_NAME, DataBaseHelper.KEY_EMAIL,DataBaseHelper. KEY_PASSWORD,DataBaseHelper.KEY_FB_TOKEN,DataBaseHelper.KEY_FB_ID },
                DataBaseHelper.KEY_EMAIL + "=? AND " +DataBaseHelper.KEY_PASSWORD+"=? ",
                new String[] {email , password }, null, null, null, null);
        User user=null;
        if(cursor!=null && cursor.getCount()>0) {
            cursor.moveToFirst();

            user = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5));

            Log.i(TAG, "get user is Done");
//            Log.i(TAG, "get user is Done");

            return user;
        }
        return user;
    }

    public User getUserByEmail(String email){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DataBaseHelper.TABLE_USER, new String[] {DataBaseHelper.KEY_ID ,DataBaseHelper.KEY_NAME, DataBaseHelper.KEY_EMAIL, DataBaseHelper.KEY_PASSWORD,DataBaseHelper.KEY_FB_TOKEN,DataBaseHelper.KEY_FB_ID  },
                DataBaseHelper.KEY_EMAIL+ "=?" ,
                new String[] {email }, null, null, null, null);
        User user=null;
        if (cursor != null && cursor.getCount()>0) {
            cursor.moveToFirst();

            user = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5));
            return user;

        } else {
            Log.i(TAG, "get user by fb is Done");

            // return user
            return null;
        }

    }
    public User getUserByFBToken(String fbId ) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DataBaseHelper.TABLE_USER, new String[] {DataBaseHelper.KEY_ID ,DataBaseHelper.KEY_NAME, DataBaseHelper.KEY_EMAIL, DataBaseHelper.KEY_PASSWORD,DataBaseHelper.KEY_FB_TOKEN,DataBaseHelper.KEY_FB_ID  },
                DataBaseHelper.KEY_FB_ID+ "=?" ,
                new String[] {fbId  }, null, null, null, null);
        User user=null;
        if (cursor != null && cursor.getCount()>0) {
            cursor.moveToFirst();

            user = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5));
            return user;

        } else {
            Log.i(TAG, "get user by fb is Done");

            // return user
            return null;
        }
    }

    // Updating single user
    public int updateUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.i("passdb", user.getPassword());
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.KEY_NAME, user.getName());
        values.put(DataBaseHelper.KEY_EMAIL, user.getEmail());
        values.put(DataBaseHelper.KEY_PASSWORD, user.getPassword());

        // updating row
        return db.update(DataBaseHelper.TABLE_USER, values, DataBaseHelper.KEY_ID + " = ?",
                new String[] { String.valueOf(user.getId()) });
    }

    // Deleting single user
    public void deleteUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DataBaseHelper.TABLE_USER, DataBaseHelper.KEY_ID + " = ?",
                new String[] { String.valueOf(user.getId()) });
        db.close();
    }

    // Getting users Count
    public int getUsersCount() {
        String countQuery = "SELECT  * FROM " + DataBaseHelper.TABLE_USER;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }



    public void fetchUser(OnUserImageSqlLiteListener listener , String id) {
        DataBaseAdapter.UserFetcher fetcher = new DataBaseAdapter.UserFetcher(listener, dbHelper.getWritableDatabase(),id);
        fetcher.start();
    }

    public class UserFetcher extends Thread {

        private final OnUserImageSqlLiteListener mListener;
        private final SQLiteDatabase mDb;
        String id;
        public UserFetcher(OnUserImageSqlLiteListener listener, SQLiteDatabase db,String id) {
            mListener = listener;
            mDb = db;
            this.id=id;
        }

        @Override
        public void run() {

            Cursor cursor = mDb.query(DataBaseHelper.TABLE_USER, new String[] { DataBaseHelper.KEY_IMAGE },  DataBaseHelper.KEY_EMAIL + "=?",
                    new String[] {id }, null, null, null, null);
            if (cursor != null )
                cursor.moveToFirst();

            User user = new User(AppUtil.getBitmapFromByte(cursor.getBlob(0)));

            // return user
            mListener.onDeliverUser(user);
            Log.i(TAG, "fetch user img is Done");

        }
    }




    static class DataBaseHelper extends SQLiteOpenHelper {


        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "TripPlannerDb";
        private static final String TABLE_NAME = "Trip";
        private static final String ID = "id";
        private static final String T_NAME = "name";
        private static final String START_POINT = "start_point";
        private static final String LONGTIUDE_START_POINT = "longtiude_start_point";
        private static final String LATITUDE_START_POINT = "latitude_start_point";
        private static final String END_POINT = "end_point";
        private static final String LONGTIUDE_END_POINT = "longtiude_end_point";
        private static final String LATITUDE_END_POINT = "latitude_end_point";
        private static final String DATE = "date";
        private static final String TIME = "time";
        private static final String TYPE = "type";
        private static final String STATUS = "status";
        private static final String NOTES = "notes";
        private static final String IMAGE = "image";
        private static  final String IMAGE_HISTORY ="image_history";
        private static final String USER_EMAIL ="user_email";
        private static final String REPEATED = "repeated";
        private static final String DURATION = "duration";
        private static final String AVERAGE_SPEED = "average_speed";

        //---------------------------------------------------------------------------------

//asmaa user db constants


        // Contacts table name
        private static final String TABLE_USER = "user";

        // Contacts Table Columns names
        private static final String KEY_ID = "id";
        private static final String KEY_NAME = "name";
        private static final String KEY_PH_NO = "phone_number";
        private static final String KEY_EMAIL = "email";
        private static final String KEY_FB_ID = "fb_id";
        private static final String KEY_FB_TOKEN = "fb_token";
        private static final String KEY_PASSWORD = "password";
        private static final String KEY_IMAGE = "img";




        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + T_NAME + " TEXT, " +
                START_POINT + " TEXT, " + LONGTIUDE_START_POINT + " TEXT, " + LATITUDE_START_POINT + " TEXT, " +
                END_POINT + " TEXT, " + LONGTIUDE_END_POINT + " TEXT, " + LATITUDE_END_POINT + " TEXT, " +
                DATE + " TEXT, " + TIME + " TEXT, " +
                NOTES + " TEXT, " + TYPE + " TEXT, " + STATUS + " TEXT, " +
                IMAGE + " BLOB, "+
                USER_EMAIL+" TEXT,"+IMAGE_HISTORY+" BLOB,"+REPEATED+" TEXT, "+DURATION+" TEXT, "+AVERAGE_SPEED+" TEXT );";


        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY ," + KEY_NAME + " TEXT," + KEY_EMAIL + " TEXT,"
                +KEY_FB_ID + " TEXT," + KEY_FB_TOKEN + " TEXT," + KEY_PASSWORD+ " TEXT,"
                + KEY_IMAGE + " blob,"
                + KEY_PH_NO + " TEXT" + ")";

        public DataBaseHelper(Context context) {
//            super(context, Environment.getExternalStorageDirectory()+ File.separator+"sqlDb"+File.separator+DATABASE_NAME, null, DATABASE_VERSION);

            super(context, DATABASE_NAME, null, DATABASE_VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL(CREATE_TABLE);
            db.execSQL(CREATE_CONTACTS_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub

        }
    }

}
