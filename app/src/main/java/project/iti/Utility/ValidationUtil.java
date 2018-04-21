package project.iti.Utility;

import android.text.TextUtils;

/**
 * Created by asmaa on 03/05/2018.
 */

public class ValidationUtil {

    public static boolean validateString(String string){
        if(string.equals("") || string.trim().length()==0 || string.equals(null) || string == null){
            return false;
        }

        return true;
    }

    public static boolean validateMore(String string){
        if( string.trim().length() <6){
            return false;
        }

        return true;
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

}
