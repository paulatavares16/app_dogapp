package br.ufba.fabiocosta.dogapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;

/**
 * Created by fabiocosta on 25/11/17.
 */

public class AboutActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


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
            case R.id.history:
                Intent intent_history = new Intent(this, HistoryActivity.class);
                startActivity(intent_history);
                return false;
            case R.id.favorite:
                Intent intent_favorite = new Intent(this, FavoriteActivity.class);
                startActivity(intent_favorite);
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
