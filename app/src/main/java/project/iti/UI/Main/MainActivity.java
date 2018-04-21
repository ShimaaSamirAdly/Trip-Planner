package project.iti.UI.Main;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import de.hdodenhof.circleimageview.CircleImageView;
import project.iti.Data.Model.Trip;
import project.iti.Data.Model.User;
import project.iti.Listener.OnUserImageSqlLiteListener;
import project.iti.R;
import project.iti.Service.NoteService;
import project.iti.Singleton.ApiUtilities;
import project.iti.SqlDb.DataBaseAdapter;
import project.iti.UI.CreateTrip.CreateTripFragment;
import project.iti.UI.DetailPastTrip.PastTripDetailFragment;
import project.iti.UI.DetailTrip.UpComingTripDetailFragment;
import project.iti.UI.EditTrip.EditTripFragment;
import project.iti.UI.Home.HomeFragment;
import project.iti.UI.Login.LoginActivity;
import project.iti.UI.PastTrip.PastTripFragment;
import project.iti.UI.Profile.ProfileFragment;
import project.iti.UI.Reminder.ReminderActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, OnUserImageSqlLiteListener {
    private CircleImageView imgProfile;
    private TextView tvName;
    private LinearLayout liProfile;
    private NavigationView navigationView;
    private  ActionBarDrawerToggle toggle;
    private  DrawerLayout drawer;
    public  Toolbar toolbar;
    private View headerLayout;
    private DataBaseAdapter mSqlLite = new DataBaseAdapter(this);
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    public static String fragmentName ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(fragmentName);
        setSupportActionBar(toolbar);
        switchToHome();
         drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

//        toolbar.setSubtitle("Asmaa");

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerLayout =  navigationView.getHeaderView(0);
        initView();

        PackageInfo info;
        try {

            info = getPackageManager().getPackageInfo(
                    "project.iti", PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key==  ", something);
                System.out.println("Hash key===  " + something);
            }

        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    private void initView(){
        imgProfile =  headerLayout.findViewById(R.id.imgProfile);
        tvName =  headerLayout.findViewById(R.id.tvName);
        tvName.setText(ApiUtilities.getInstance().getRefrences(this).getUser().getName());
        mSqlLite.fetchUser(this,ApiUtilities.getInstance().getRefrences(this).getUser().getEmail());

        liProfile = (LinearLayout)  headerLayout.findViewById(R.id.liProfile);
        liProfile.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        Log.i("here","here");
        HomeFragment homeFragment ;
        homeFragment = (HomeFragment)getSupportFragmentManager().findFragmentByTag("HomeFragment");

        int count = getFragmentManager().getBackStackEntryCount();

        Log.i("count frag",""+count);

        if(count==0&&homeFragment==null)
        {
            finish();
        }
    }

    private void switchToProfile(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new ProfileFragment())
                .commit();
    }
    private void switchToLogin(){
        Intent switchLogin = new Intent(this, LoginActivity.class);
        startActivity(switchLogin);
    }

    private void switchToHome() {

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        HomeFragment homeFragment = new HomeFragment();

        fragmentTransaction.replace(R.id.container, homeFragment, "HomeFragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void switchToAdd() {


        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        CreateTripFragment createTripFragment = new CreateTripFragment();

        fragmentTransaction.replace(R.id.container, createTripFragment, "createTripFragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        Log.i("gh","ghjkk");
    }

    private void switchToHistory()
    {

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        PastTripFragment pastTripFragment = new PastTripFragment();

        fragmentTransaction.replace(R.id.container, pastTripFragment, "pastTripFragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void switchToHistoryTripDetail(Trip p)

    {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        PastTripDetailFragment pastTripFrag = new PastTripDetailFragment();
        Bundle bundleobj = new Bundle();

        bundleobj.putSerializable("trip", p);
        pastTripFrag.setArguments(bundleobj);
        fragmentTransaction.replace(R.id.container, pastTripFrag, "pastTripFrag");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void switchToEditTrip(Trip p) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        EditTripFragment editTripFrag = new EditTripFragment();
        Bundle bundleobj = new Bundle();

        bundleobj.putInt("id", p.getId());
        editTripFrag.setArguments(bundleobj);
        fragmentTransaction.replace(R.id.container, editTripFrag, "editTripFrag");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }


    public void switchToUpComingTripDetail(Trip p) {

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        UpComingTripDetailFragment upComingTripDetailFragment = new UpComingTripDetailFragment();
        Bundle bundleobj = new Bundle();

        //bundleobj.putSerializable("trip", p);

        bundleobj.putInt("id",p.getId());
        upComingTripDetailFragment.setArguments(bundleobj);
        fragmentTransaction.replace(R.id.container, upComingTripDetailFragment, "upComingTripDetailFragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }





    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            switchToHome();
        } else if (id == R.id.nav_add) {

            switchToAdd();

        } else if (id == R.id.nav_history) {

            switchToHistory();

        } else if (id == R.id.nav_logOut) {
            ApiUtilities.getInstance().getRefrences(this).removeUser();
            switchToLogin();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.liProfile:
                drawer.closeDrawers();
                switchToProfile();
                break;
        }
    }

    @Override
    public void onDeliverUser(User user) {
        imgProfile.setImageBitmap(user.getImg2());
    }
}
