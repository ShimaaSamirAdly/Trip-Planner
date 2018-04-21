package project.iti.UI.Home;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import project.iti.Adapter.NoteAdapter;
import project.iti.Data.Model.NoteModel;
import project.iti.Injection.Injection;
import project.iti.Listener.NoteListener;
import project.iti.R;

public class NoteActivity extends AppCompatActivity implements HomePresenter.HomeView,NoteListener {
    private RecyclerView recyclerView;

    private HomePresenter presenter;
    private NoteAdapter noteAdapter;
    private ArrayList<NoteModel> noteModels = new ArrayList<>();

//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView= findViewById(R.id.my_recycler_view);

        presenter = new HomePresenter(Injection.provideHomeRepository());
        presenter.setView(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onItemChange
            (NoteModel noteModel, int position) {


    }

    @Override
    public void setData(ArrayList<NoteModel>data) {
        Log.i("setData ====", "setD");
        noteModels.addAll(data);
        noteAdapter = new NoteAdapter(noteModels,this);
        noteAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(noteAdapter);



    }
}
