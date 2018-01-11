package br.ufba.fabiocosta.dogapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by fabiocosta on 21/11/17.
 */

public class DogPhotoActivity extends AppCompatActivity{
    private ImageView imgDog1;
    private ImageView imgDog2;
    private ImageView imgDog3;
    private ImageView imgDog4;
    private ImageView imgDog5;
    private ImageView imgDog6;

    private DogDao dao;
    private long dog_id;
    private List<PhotoURL> photosList = new ArrayList<>();

    String title = "Dog Breed";
    private List<Dog> dogsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_photo);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("title")) {
            title = extras.getString("title");
        }
        if (extras != null && extras.containsKey("id")) {
            dog_id = extras.getLong("id");
        }
        getSupportActionBar().setTitle(title);

        dao = DogDao.getInstance(this);
        photosList = dao.getAllPhotos(dog_id);

        if(photosList.isEmpty()){
            Toast tost_downloaded =  Toast.makeText(this, "O download dessa raça ainda não foi executado", Toast.LENGTH_LONG);
            tost_downloaded.show();

        }else{
            //WebView webview = (WebView)findViewById(R.id.photo_dog);
            //webview.loadUrl("https://dog.ceo/api/img/hound-afghan/n02088094_1003.jpg");
            imgDog1 = (ImageView)findViewById(R.id.imageViewDog1);
            imgDog2 = (ImageView)findViewById(R.id.imageViewDog2);
            imgDog3 = (ImageView)findViewById(R.id.imageViewDog3);
            imgDog4 = (ImageView)findViewById(R.id.imageViewDog4);
            imgDog5 = (ImageView)findViewById(R.id.imageViewDog5);
            imgDog6 = (ImageView)findViewById(R.id.imageViewDog6);

            setPhotosPicasso(imgDog1,photosList.get(1).getURl());
            setPhotosPicasso(imgDog2,photosList.get(2).getURl());
            setPhotosPicasso(imgDog3,photosList.get(3).getURl());
            setPhotosPicasso(imgDog4,photosList.get(4).getURl());
            setPhotosPicasso(imgDog5,photosList.get(5).getURl());
            setPhotosPicasso(imgDog6,photosList.get(6).getURl());

            // adding to history
            sharedPreferencesHistory(DogPhotoActivity.this,title,dog_id);
        }

    }

    public void setPhotosPicasso(ImageView img, String path){
        Picasso.with(getBaseContext()).load(path).placeholder(R.drawable.loading).error(R.drawable.dog).into(img);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dog_menu_photo, menu);
        return true;
    }

    private void saveSharedPreferences(Context context, List<Dog> dogList){
        SharedPreferences favoritePref = context.getSharedPreferences(getString(R.string.pref_key_favorites),context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = favoritePref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(dogList);
        prefsEditor.putString(getString(R.string.pref_key_favorites), json);
        prefsEditor.commit();
    }

    private static List<Dog> loadSharedPreferences(Context context) {
        List<Dog> dogList = new ArrayList<Dog>();
        SharedPreferences mPrefs = context.getSharedPreferences(context.getString(R.string.pref_key_favorites), context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString(context.getString(R.string.pref_key_favorites), "");
        if (json.isEmpty()) {
            dogList = new ArrayList<Dog>();
        } else {
            Type type = new TypeToken<List<Dog>>() {
            }.getType();
            dogList = gson.fromJson(json, type);
        }
        return dogList;
    }

    private static List<Dog> checkDogInFavorites(Context context, String title, long id, List<Dog> dogsList){
        List<Dog> toRemove = new ArrayList<Dog>();
        for (Dog dog: dogsList){
            if (dog.getBreed().equals(title)){
                toRemove.add(dog);
            }
        }
        if (toRemove.isEmpty()){
            Dog dog = new Dog(title);
            dog.setId(id);
            dogsList.add(0, dog);
            Toast toast =  Toast.makeText(context, title+" foi adicionado nos favoritos", Toast.LENGTH_LONG);
            toast.show();
        }else {
            dogsList.removeAll(toRemove);
            Toast toast = Toast.makeText(context,title+" foi removido dos favoritos",Toast.LENGTH_LONG);
            toast.show();
        }
        return dogsList;
    }

    private void sharedPreferencesHistory(Context context, String title, long id){
        List<Dog> dogList = new ArrayList<Dog>();

        // loading sharedPreferences
        SharedPreferences mPrefs = context.getSharedPreferences(context.getString(R.string.pref_key_histories), context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString(context.getString(R.string.pref_key_histories), "");
        if (json.isEmpty()) {
            dogList = new ArrayList<Dog>();
        } else {
            Type type = new TypeToken<List<Dog>>() {
            }.getType();
            dogList = gson.fromJson(json, type);
        }

        // adding to history
        if(dogList.size() > 14) {
            dogList.remove(14);
        }
        Dog dog = new Dog(title);
        dog.setId(id);
        dogList.add(0, dog);

        // Committing
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        json = gson.toJson(dogList);
        prefsEditor.putString(getString(R.string.pref_key_histories), json);
        prefsEditor.commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.breeds:
                Intent intent_breeds = new Intent(this, MainActivity.class);
                startActivity(intent_breeds);
                return false;
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
            case R.id.addFavorite:
                dogsList = loadSharedPreferences(DogPhotoActivity.this);
                dogsList = checkDogInFavorites(DogPhotoActivity.this,title,dog_id,dogsList);
                saveSharedPreferences(DogPhotoActivity.this, dogsList);
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }

    }



}
