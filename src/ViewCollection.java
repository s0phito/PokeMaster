package com.example.pokemaster;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.IOException;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class ViewCollection extends AppCompatActivity {
    private PokeDBHelper helper;
    private SQLiteDatabase myDB;
    private CollectionDBHelper collection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_collection);
        String tableName = getIntent().getStringExtra("login_name");
        if (tableName == null)
            collection = new CollectionDBHelper(this);
        else
            collection = new CollectionDBHelper(this, tableName);
        SQLiteDatabase cDB = collection.getWritableDatabase();
        try {
            cDB.execSQL("CREATE TABLE " + tableName + " (PokeDex INTEGER PRIMARY KEY, Name TEXT, Type1 TEXT, Type2 TEXT, Location TEXT, Evol TEXT)");
        }
        catch(SQLiteException e){
            // if table already exists, do not add it to database
        }
        Spinner spinner = (Spinner)findViewById(R.id.collectionID);
        initializeDB();
        initializeSpinner(spinner);
    }
    public void initializeDB() {

        helper = new PokeDBHelper(this);

        try {
            helper.update();
        } catch (IOException e) {
            throw new Error("Error encountered while updating database");
        }

        try {
            myDB = helper.getWritableDatabase();
        } catch (SQLException e) {
            throw e;
        }
    }
    public void initializeSpinner(Spinner  myPokemon){
        // Calls a function moveQuery that returns an array of the top 20 moves to use agaainst that type of pokemon
        ArrayList<String> pokeList = getMyPokemon();
        //System.out.println(this.move);


        ArrayAdapter<String> pkmnAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,pokeList);
        pkmnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myPokemon.setAdapter(pkmnAdapter);
    }

    public ArrayList<String> getMyPokemon(){
        ArrayList<String> pokeCollection = new ArrayList<>();

        String query = "SELECT Name FROM pkmn WHERE isCaught = 0";
        Cursor cursor = myDB.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                String name =  cursor.getString(cursor.getColumnIndex("Name"));
                pokeCollection.add(name);
            }
            while(cursor.moveToNext());
        }

        cursor.close();
        return pokeCollection;
    }

}
