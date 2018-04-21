package project.iti;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import project.iti.API.ApiEndpointInterface;
import project.iti.Data.Model.Data;
import project.iti.Data.Model.Map.MapModel;
import project.iti.Singleton.ApiUtilities;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//bonus
public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Call<MapModel> call ;
        call = ApiUtilities.getInstance().getApi().getMapData("Mostafa+Kamel+Hospital+For+Armed+Forces","Qism+sidi+gabir","AIzaSyBjGs-Zls3PdkCxYdTJMWX5gvoWD07hF4M");

        call.clone().enqueue(new Callback<MapModel>() {
            @Override
            public void onResponse(Call<MapModel> call, Response<MapModel> response) {

                if (response.body()!=null){
                   Log.i("TEST =====", response.body().getRoutes().get(0).getLegs().get(0).getDistance().getText());
                }
            }
            @Override
            public void onFailure(Call<MapModel> call, Throwable t) {

                Log.e("error: ",t.getMessage());
            }
        });
    }
}
