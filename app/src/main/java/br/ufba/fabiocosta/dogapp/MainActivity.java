package br.ufba.fabiocosta.dogapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends AppCompatActivity{

    private List<Dog> dogsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DogAdapter mAdapter;
    private DogDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Action Bar Custon
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        dao = DogDao.getInstance(this);

        // Testing Network
        if(!isNetworkAvailable()){
            Toast tost_network =  Toast.makeText(this, "Não há conexão a internet disponível", Toast.LENGTH_LONG);
            tost_network.show();
            dogsList = dao.getAll();

            // Configure RecycleView List
            configureRecycleView();
        }else {
            // Creating a task
            DownloaderTask task = new DownloaderTask();
            task.execute("https://dog.ceo/api/breeds/list");
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dog_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favorite:
                Intent intent_favorite = new Intent(this, FavoriteActivity.class);
                startActivity(intent_favorite);
                return false;
            case R.id.history:
                Intent intent_history = new Intent(this, HistoryActivity.class);
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
        mAdapter = new DogAdapter(dogsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
    }

    public boolean isNetworkAvailable(){
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    class DownloaderTask extends AsyncTask<String, Integer, JSONArray> {
        @Override
        protected JSONArray doInBackground(String... params){
            try{
                URL url = new URL(params[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                String jsonStr = getString(httpURLConnection.getInputStream());
                JSONObject objectJSON = new JSONObject(jsonStr);
                JSONArray jsonArray = objectJSON.getJSONArray("message");

                return jsonArray;
            }
            catch(MalformedURLException e) {e.printStackTrace();}
            catch (IOException e) {e.printStackTrace();}
            catch (JSONException e) {e.printStackTrace();}
            return null;

        }

        @Override
        protected void onPostExecute(JSONArray jsonArray){
            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    long idDog = insertDog(jsonArray.getString(i));

                    String str = "https://dog.ceo/api/breed/"+jsonArray.getString(i)+"/images";
                    DownloaderTaskURL task = new DownloaderTaskURL();
                    task.execute(str,String.valueOf(idDog));
                }
            }
            catch (JSONException e) {e.printStackTrace();}

            dogsList = dao.getAll();

            // Configure RecycleView List
            configureRecycleView();
        }


    }

    class DownloaderTaskURL extends AsyncTask<String, Integer, JSONArray>{
        private Integer idDog;

        @Override
        protected JSONArray doInBackground(String... params) {
            try{
                URL url2 = new URL(params[0]);
                idDog = Integer.valueOf(params[1]);
                HttpURLConnection httpURLConnection2 = (HttpURLConnection) url2.openConnection();
                httpURLConnection2.connect();
                String jsonStr2 = getString(httpURLConnection2.getInputStream());
                JSONObject objectJSON2 = new JSONObject(jsonStr2);
                JSONArray jsonArray = objectJSON2.getJSONArray("message");
                return jsonArray;

            }
            catch(MalformedURLException e) {e.printStackTrace();}
            catch (IOException e) {e.printStackTrace();}
            catch (JSONException e) {e.printStackTrace();}
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray){
            try{
                for (int i = 0; i < jsonArray.length() && i < 7; i++){
                    String u = jsonArray.getString(i);
                    insertPhotos(idDog,u);
                }
            }
            catch (JSONException e) {e.printStackTrace();}
        }
    }

    public long insertDog(String breed){
        Dog d = new Dog(breed);
        long id = dao.insertDog(d);
        return id;
    }

    public void insertPhotos(long idDog, String u){
        PhotoURL pu = new PhotoURL(u,idDog);
        dao.insertPhoto(pu);
    }

    private String getString(InputStream in) throws IOException{
        BufferedReader buffer = new BufferedReader(new InputStreamReader(in));
        StringBuilder str = new StringBuilder();
        String line = null;
        while ((line = buffer.readLine()) != null){
            str.append(line);
        }
        return str.toString();
    }
}
