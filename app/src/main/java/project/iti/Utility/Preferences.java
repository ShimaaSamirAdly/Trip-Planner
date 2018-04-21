package project.iti.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.gson.Gson;

import project.iti.Data.Model.User;

/**
 * Created by asmaa on 02/26/2018.
 */

/**
 * shared preferences class to cache app's data
 */

public class Preferences {

    private Context context;
    private SharedPreferences mPreferences;

//    private static final String LOGGED = "checked";
    private static final String KEY_USER = "user";
    private static final String IS_LAUNCHER = "is_launcher";
    private static final String USER_EMAIL = "user_email";

    public Preferences(Context context) {
        this.context=context;
        initialization();
    }

    private void initialization(){
        mPreferences = context.getSharedPreferences("preferences", 0);
    }

    public void setLauncher(boolean state) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(IS_LAUNCHER, state).commit();
    }

    public boolean isLauncher() {
        return mPreferences.getBoolean(IS_LAUNCHER, false);
    }

    public void setUser(User user) {

        SharedPreferences.Editor editor = mPreferences.edit();
        String json = new Gson().toJson(user);
        editor.putString(KEY_USER,null);
        editor.putString(KEY_USER,json);
        editor.commit();
    }
    public User getUser() {
        return new Gson().fromJson(mPreferences.getString(KEY_USER,""),User.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public void removeUser() {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.remove(KEY_USER);
        editor.apply();
    }

    public String getUserName() {
        return mPreferences.getString(USER_EMAIL, "");
    }

}
