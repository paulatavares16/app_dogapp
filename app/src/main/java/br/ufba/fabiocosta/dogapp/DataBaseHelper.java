package br.ufba.fabiocosta.dogapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by paula on 22/11/17.
 */

public class DataBaseHelper extends SQLiteOpenHelper{
    public static final String TABLE_BREED = "dogs";
    public static final String COLUMN_ID_BREED = "_id";
    public static final String COLUMN_BREED = "breed";

    public static final String TABLE_PHOTO = "photos";
    public static final String COLUMN_ID_PHOTO = "_id";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_MANY = "breedP";

    private static final String DATABASE_NAME_1 = "dogs.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DB_CREATE_DOGS = "create table "+TABLE_BREED+"("+COLUMN_ID_BREED+" integer primary key autoincrement, "
            +COLUMN_BREED+" text not null);";

    private static final String DB_CREATE_PHOTOS = "create table "+TABLE_PHOTO+"("+COLUMN_ID_PHOTO+" integer primary key autoincrement, "
            +COLUMN_URL+" text not null, "+COLUMN_MANY+" integer not null, foreign key ("+COLUMN_MANY+") references "+TABLE_BREED+"("+COLUMN_ID_BREED+"));";

    private static final String CREATE_PHOTO_INDEX = "create unique index if not exists photoIndex on "+TABLE_PHOTO+" ("+COLUMN_URL+")";

    private static final String CREATE_BREED_INDEX = "create unique index if not exists breedIndex on "+TABLE_BREED+" ("+COLUMN_BREED+")";



    public DataBaseHelper(Context context){
        super(context, DATABASE_NAME_1, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(DB_CREATE_DOGS);
        db.execSQL(DB_CREATE_PHOTOS);
        db.execSQL(CREATE_BREED_INDEX);
        db.execSQL(CREATE_PHOTO_INDEX);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int  oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_BREED);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_PHOTO);
        onCreate(db);
    }
}
