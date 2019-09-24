package com.example.pokemaster;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class CollectionDBHelper extends SQLiteOpenHelper{
    public static String DATABASE_NAME = "user_collections.db";
    public static String TABLE_NAME = "";
    public static int DATABASE_VERSION = 1;
    public static final String COL_1 = "PokeDex";
    public static final String COL_2 = "Name";
    public static final String COL_3 = "Type1";
    public static final String COL_4 = "Type2";
    public static final String COL_5 = "Location";
    public static final String COL_6 = "Evol";

    public CollectionDBHelper(Context context, String tbName) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        TABLE_NAME = tbName;
    }

   public CollectionDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE "+TABLE_NAME+" (PokeDex INTEGER PRIMARY KEY, Name TEXT, Type1 TEXT, Type2 TEXT, Location TEXT, Evol TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase myDB, int oldVersion, int newVersion) {


    }
    public long insertPokemon(int num, String name, String type1, String type2, String location, String evol){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("PokeDex", num);
        cv.put("Name", name);
        cv.put("type1", type1);
        cv.put("type2", type2);
        cv.put("location", location);
        cv.put("Evol", evol);
        long newPoke = db.insert(TABLE_NAME, null, cv);


        db.close();

        return newPoke;
    }

    public long deletePokemon(String pName){

        pName = "'" + pName + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        long deleted = db.delete(TABLE_NAME,  "Name = " + pName, null);

        db.close();
        return deleted;
    }

    public ArrayList<String> viewCollection(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> data = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT PokeDex, Name FROM "+ TABLE_NAME, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                String pName =  cursor.getString(cursor.getColumnIndex("Name"));
                int pNum =  cursor.getInt(cursor.getColumnIndex("PokeDex"));
                data.add(pNum +": " + pName);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return data;


    }

    public void checkHasMove(String move) {

    }

}
