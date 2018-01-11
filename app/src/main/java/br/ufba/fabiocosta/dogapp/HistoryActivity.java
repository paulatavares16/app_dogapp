package br.ufba.fabiocosta.dogapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class HistoryActivity extends AppCompatActivity{

    private List<Dog> dogsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DogAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Configure RecycleView List
        configureRecycleView();

    }

    @Override
    protected void onResume(){
        // Configure RecycleView List
        configureRecycleView();
        super.onResume();
    }

    protected void onPause(){
        super.onPause();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dog_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.breeds:
                Intent intent_breeds = new Intent(this, MainActivity.class);
                startActivity(intent_breeds);
                return false;
            case R.id.favorite:
                Intent intent_history = new Intent(this, FavoriteActivity.class);
                startActivity(intent_history);
                return false;
            case R.id.about:
                Intent intent_about = new Intent(this, AboutActivity.class);
                startActivity(intent_about);
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    // Configure RecycleView List
    private void configureRecycleView(){
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new DogAdapter(loadSharedPreferences(HistoryActivity.this));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
    }

    private static List<Dog> loadSharedPreferences(Context context) {
        List<Dog> history = new ArrayList<Dog>();
        SharedPreferences mPrefs = context.getSharedPreferences(context.getString(R.string.pref_key_histories), context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString(context.getString(R.string.pref_key_histories), "");
        if (json.isEmpty()) {
            history = new ArrayList<Dog>();
        } else {
            Type type = new TypeToken<List<Dog>>() {
            }.getType();
            history = gson.fromJson(json, type);
        }
        return history;
    }
}
