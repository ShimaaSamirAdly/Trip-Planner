package project.iti.UI.Profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import de.hdodenhof.circleimageview.CircleImageView;
import project.iti.Data.Model.User;
import project.iti.Listener.OnUserImageSqlLiteListener;
import project.iti.R;
import project.iti.Singleton.ApiUtilities;
import project.iti.SqlDb.DataBaseAdapter;
import project.iti.UI.Login.LoginActivity;
import project.iti.UI.Main.MainActivity;


public class ProfileFragment extends Fragment implements OnUserImageSqlLiteListener {


    User user;
    DataBaseAdapter db;
    private CircleImageView imgProfile;
    private MaterialEditText email;
    private MaterialEditText password;
    private MaterialEditText name;
    private Button saveBtn;


        public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        MainActivity.fragmentName="Edit Profile";
        ((MainActivity)getContext()).toolbar.setTitle( MainActivity.fragmentName);

        db = new DataBaseAdapter(getContext());

        imgProfile = v.findViewById(R.id.profile_image);
        email = v.findViewById(R.id.email);
        name = v.findViewById(R.id.name);
        password = v.findViewById(R.id.password);
        saveBtn = v.findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveData();
            }
        });

        return v;
    }

    private void initView(View v ){

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        user = db.getUserByEmail(ApiUtilities.getInstance().getRefrences(getContext()).getUser().getEmail());
        db.fetchUser(this,ApiUtilities.getInstance().getRefrences(getContext()).getUser().getEmail());
        name.setText(ApiUtilities.getInstance().getRefrences(getContext()).getUser().getName());
        email.setText(ApiUtilities.getInstance().getRefrences(getContext()).getUser().getEmail());
        password.setText(ApiUtilities.getInstance().getRefrences(getContext()).getUser().getPassword());


    }


    @Override
    public void onDeliverUser(User user) {
        imgProfile.setImageBitmap(user.getImg2());
    }


    public void saveData(){

        String nameData = name.getText().toString();
        String passwordData = password.getText().toString();
        if(!nameData.equals("") && !passwordData.equals("")) {
            user.setName(nameData);
            user.setPassword(passwordData);

            Log.i("pass", passwordData);
            Log.i("pass", user.getPassword());

            ApiUtilities.getInstance().getRefrences(getContext()).removeUser();

            db.updateUser(user);

            switchToLogin();

        }else{

            Toast.makeText(getContext(), "You Should Fill All Fields", Toast.LENGTH_LONG).show();
        }
    }

    private void switchToLogin(){
        Intent switchLogin = new Intent(getContext(), LoginActivity.class);
        startActivity(switchLogin);
    }
}
