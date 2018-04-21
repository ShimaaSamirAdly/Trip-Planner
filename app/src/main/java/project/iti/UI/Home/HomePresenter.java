package project.iti.UI.Home;

/**
 * Created by asmaa on 02/26/2018.
 */

import android.util.Log;

import java.util.ArrayList;

import project.iti.Data.Model.NoteModel;
import project.iti.Data.Repository.HomeRepository;
import project.iti.Listener.OnHomeResult;

/**
 * presenter class to get data from repository class and handle show this data in view
 */

public class HomePresenter {

    private ArrayList<NoteModel> data = new ArrayList<>();

    private HomeRepository homeRepository;
    HomeView view;

    public HomePresenter( HomeRepository homeRepository){
        this.homeRepository =homeRepository;
    }

    public void setView(HomeView view) {
        this.view =view;

        Log.i("here ","here");
        if (data.size()== 0) {
            getHomeData();
            Log.i("here ","here2");

        } else {
            view.setData(data);
            Log.i("here ","here3");
        }
    }

    public void getHomeData(){
        homeRepository.getHomeData(new OnHomeResult() {
            @Override
            public void onSuccess(ArrayList<NoteModel> noteModels) {

                Log.i("presenter:==== ",""+ noteModels.size());
                data.addAll(noteModels);
                view.setData(data);
            }

            @Override
            public void onFailure() {


            }
        });

    }

    public interface HomeView{
        void setData(ArrayList<NoteModel> noteModels);


    }
}
