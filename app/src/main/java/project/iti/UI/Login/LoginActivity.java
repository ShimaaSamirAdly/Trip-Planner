package project.iti.UI.Login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;

import project.iti.Data.Model.User;
import project.iti.R;
import project.iti.Singleton.ApiUtilities;
import project.iti.SqlDb.DataBaseAdapter;
import project.iti.UI.Main.MainActivity;
import project.iti.UI.SignUp.SignUpActivity;
import project.iti.Utility.AppUtil;
import project.iti.Utility.ValidationUtil;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String EMAIL = "email";
    private CallbackManager callbackManager;
    private LoginButton  loginButton;
    private Button btnLogin;
    private MaterialEditText etEmail, etPassword;
    private TextView tvSignUp, tvError;
    private String email;
    private DataBaseAdapter mSqlLite = new DataBaseAdapter(this);
    private User user = new User();
    private  User checkUser = new User();
    private final String TAG=this.getClass().getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        initView();
        if (AppUtil.isNetworkAvailable(this))
        facebook();
        else Toast.makeText(this, "connection error", Toast.LENGTH_SHORT).show();
    }

    private void initView(){

        loginButton = (LoginButton) findViewById(R.id.login_button);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPass);
        tvError = findViewById(R.id.tvError);
        tvSignUp = findViewById(R.id.tvSignUp);
        tvSignUp.setOnClickListener(this);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
    }

    private void facebook(){
        FacebookSdk.sdkInitialize(getApplicationContext());
        LoginManager.getInstance().logOut();
        callbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                facebookGetUserDetails(loginResult);
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    protected void facebookGetUserDetails(final LoginResult loginResult) {
        GraphRequest data_request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onCompleted(
                            JSONObject json_object,
                            GraphResponse response) {
                        try {

                            checkUser= mSqlLite.getUserByFBToken(loginResult.getAccessToken().getUserId());
                            if (checkUser==null) {
                                String id=  loginResult.getAccessToken().getUserId();
                                String name = (String) json_object.get("name");
                                email = (String) json_object.get("email");
                                if ( json_object.get("email") ==null){
                                    email="asmaa@gmail.com";
                                }else {
                                    email = (String) json_object.get("email");
                                }
                                Log.i("tokennn", loginResult.getAccessToken().getToken());
                                Log.i("tokennnId", loginResult.getAccessToken().getUserId());
                                user.setEmail(email);
                                user.setFbID(id);
                                user.setFbToken(loginResult.getAccessToken().getToken());
                                user.setName(name);
                                user.setPhone("");
                                SaveIntoSqlLite task = new SaveIntoSqlLite();
                                task.execute(user);
                                Log.i(TAG,"CHECK");
                            }else {
                                Log.i(TAG,"not CHECK");
                                ApiUtilities.getInstance().getRefrences(LoginActivity.this).setUser(checkUser);
                                switchToHome();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });
        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email,picture.width(120).height(120)");
        data_request.setParameters(permission_param);
        data_request.executeAsync();

    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public class SaveIntoSqlLite extends AsyncTask<User, Void, Void> {

        User user = new User();
        private final String TAG = SaveIntoSqlLite.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(User... params) {

            User user = params[0];
            this.user=user;

            try {
                Log.i(TAG,"TASK");

                InputStream stream = new URL("https://graph.facebook.com/"
                        + user.getFbID()
                        + "/picture?type=large").openStream();
                Bitmap bitmap = BitmapFactory.decodeStream(stream);
                user.setImg2(bitmap);
                mSqlLite.addUser(user);

            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            Log.i("loadiiiiing","loading");

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.i(TAG,"TASK DONE");
            ApiUtilities.getInstance().getRefrences(LoginActivity.this).setUser(user);
            switchToHome();
        }
    }

    private void switchToHome(){
        Intent switchHome = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(switchHome);
    }

    protected void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    private boolean loginValidate(){

        if (!ValidationUtil.validateString(etEmail.getText().toString())) {
            etEmail.setError(getResources().getText(R.string.emptyEmailError));
            return false;
        }
        if (!ValidationUtil.isValidEmail(etEmail.getText().toString())) {
            etEmail.setError(getResources().getText(R.string.emailError));
            return false;
        }

        if (!ValidationUtil.validateString(etPassword.getText().toString())) {
            etPassword.setError(getResources().getText(R.string.emptyPassError));
            return false;
        }
        if (!ValidationUtil.validateMore(etPassword.getText().toString())) {
            etPassword.setError(getResources().getText(R.string.moreCharError));
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnLogin:
                if (loginValidate()){
                 user=   mSqlLite.getUser(etEmail.getText().toString(), etPassword.getText().toString());
                 if (user!=null){
                     tvError.setVisibility(View.GONE);
                     ApiUtilities.getInstance().getRefrences(LoginActivity.this).setUser(user);
                     Intent switchHome = new Intent(LoginActivity.this, MainActivity.class);
                     startActivity(switchHome);
                 }else {
                     tvError.setVisibility(View.VISIBLE);
                 }
                }
                break;

            case R.id.tvSignUp:
                Intent switchSignUp = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(switchSignUp);
                break;
        }
    }
}
