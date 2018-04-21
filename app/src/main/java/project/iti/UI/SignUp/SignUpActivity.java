package project.iti.UI.SignUp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import de.hdodenhof.circleimageview.CircleImageView;
import project.iti.Data.Model.User;
import project.iti.R;
import project.iti.SqlDb.DataBaseAdapter;
import project.iti.UI.Login.LoginActivity;
import project.iti.Utility.ValidationUtil;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private CircleImageView imgProfile;
    private MaterialEditText etEmail;
    private MaterialEditText etPassword,etRepassword  ;
    private MaterialEditText etName;
    private TextView tvLogin ,tvImgError ,tvError;
    private Button btnSignUp;
    private Bitmap profileBitmap;
    private DataBaseAdapter mSqlLite = new DataBaseAdapter(this);

    private final int CAMERA_RESULT = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);
        initView();
    }

    private void initView(){
        imgProfile =findViewById(R.id.profile_image);
        imgProfile.setOnClickListener(this);
        etEmail =findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPass);
        etRepassword = findViewById(R.id.etRePass);
        etName = findViewById(R.id.etName);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(this);
        tvLogin = findViewById(R.id.tvLogin);
        tvLogin.setOnClickListener(this);
        tvImgError = findViewById(R.id.tvImgError);
        tvError= findViewById(R.id.tvError);
    }

    private void dispatchTakenPictureIntent(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, CAMERA_RESULT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == CAMERA_RESULT){
                Bundle extras = data.getExtras();
                profileBitmap = (Bitmap) extras.get("data");
                imgProfile.setImageBitmap(profileBitmap);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_RESULT){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                dispatchTakenPictureIntent();
            }
            else{
                Toast.makeText(getApplicationContext(), "Permission Needed.", Toast.LENGTH_LONG).show();
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private boolean signUpValidate(){
        if (!ValidationUtil.validateString(etName.getText().toString())) {
            etName.setError(getResources().getText(R.string.emptyNameError));
            return false;
        }
        if (!ValidationUtil.validateMore(etName.getText().toString())) {
            etName.setError(getResources().getText(R.string.moreCharError));
            return false;
        }

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

        if (!ValidationUtil.validateString(etRepassword.getText().toString())) {
            etRepassword.setError(getResources().getText(R.string.emptyPassError));
            return false;
        }
        if (!ValidationUtil.validateMore(etRepassword.getText().toString())) {
            etRepassword.setError(getResources().getText(R.string.moreCharError));
            return false;
        }
        if (!etPassword.getText().toString().equals( etRepassword.getText().toString())){
            etRepassword.setError(getResources().getText(R.string.passMatchError));
            return false;
        }
        if ( profileBitmap==null){
            tvImgError.setVisibility(View.VISIBLE);
            return false;
        }else tvImgError.setVisibility(View.GONE);

        return true;
    }

    private void switchToLogin(){
        Intent switchLogin = new Intent(this, LoginActivity.class);
        startActivity(switchLogin);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.profile_image:
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    dispatchTakenPictureIntent();
                }
                else{
                    if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                        Toast.makeText(getApplicationContext(), "Permission Needed.", Toast.LENGTH_LONG).show();
                    }
                    requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_RESULT);
                }
                break;

            case R.id.tvLogin:
                switchToLogin();
                break;
            case R.id.btnSignUp:
                if (signUpValidate()){
                    if (mSqlLite.getUserByEmail(etEmail.getText().toString())==null) {
                        tvError.setVisibility(View.GONE);
                        User user = new User();
                        user.setImg2(profileBitmap);
                        user.setName(etName.getText().toString());
                        user.setEmail(etEmail.getText().toString());
                        user.setPassword(etPassword.getText().toString());
                        mSqlLite.addUser(user);
                        switchToLogin();
                    }else {

                        tvError.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }
    }
}
