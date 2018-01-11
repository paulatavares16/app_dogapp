package br.ufba.fabiocosta.dogapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by paulatavares on 18/12/17.
 */

public class DogDao {
    private SQLiteDatabase db ;
    private DataBaseHelper dbHelper;
    private static DogDao dao;

    public static DogDao getInstance(Context context){
        if(dao == null){
            dao = new DogDao(context);
            dao.open();
        }
        return dao;
    }

    private DogDao(Context context){
        dbHelper = new DataBaseHelper(context);
    }

    private void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    private void close(){
        Log.i("CLOSEEE","FECHANDO AQUI");
        dbHelper.close();
        db = null;
    }

    public long insertDog(Dog dog_) {
        ContentValues values = new ContentValues();
        values.put("breed", dog_.getBreed());
        long id = db.insertWithOnConflict("dogs", null, values, SQLiteDatabase.CONFLICT_REPLACE);
        Log.d("Info", "Inserting new dog"+id);
        return id;
    }

    public void insertPhoto(PhotoURL photoURL){
        ContentValues values = new ContentValues();
        values.put("url", photoURL.getURl());
        values.put("breedP", photoURL.getDog_id());
        db.insertWithOnConflict("photos",null, values, SQLiteDatabase.CONFLICT_REPLACE);
        Log.d("Info", "Inserting new photo URL"+photoURL);
    }

    public List<Dog> getAll(){
        List<Dog> lista = new ArrayList<>();

        Cursor cursor = db.query(false, "dogs", null, null, null, null, null, "breed", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){

            long id = cursor.getLong(0);
            String breed = cursor.getString(1);
            Dog dog = new Dog(breed);
            dog.setId(id);
            lista.add(dog);
            cursor.moveToNext();
        }
        return lista;
    }

    public List<PhotoURL> getAllPhotos(long id){

        List<PhotoURL> lista = new ArrayList<>();
        String iD = String.valueOf(id);

        String query = "select url, breedP from photos where breedP = "+iD+";";

        Log.i("QUERY",""+query);

        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){

            String url = cursor.getString(0);
            long dog_id = cursor.getLong(1);
            PhotoURL pu = new PhotoURL(url, dog_id);
            lista.add(pu);
            cursor.moveToNext();
        }
        Log.i("TESTE QUERY","RESULT: "+lista);
        return lista;

    }
}
