package com.example.pokemaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class TypeCalculator extends AppCompatActivity {

    private PokeDBHelper helper;
    private SQLiteDatabase allPoke;
    private CollectionDBHelper collection;
    private ArrayList<String> pokeList;

    private int num = -2;
    private String type1 = "", type2 = "", location = "", evol = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_typecalculator);

        initializeDB();
        setPokeList();

        SearchView sv = (SearchView) findViewById(R.id.searchView);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println(newText);
                checkText(newText);
                return false;
            }
        });


    }

    public void initializeDB() {

        helper = new PokeDBHelper(this);

        try {
            helper.update();
        } catch (IOException e) {
            throw new Error("Error encountered while updating database");
        }

        try {
            allPoke = helper.getWritableDatabase();
        } catch (SQLException e) {
            throw e;
        }
    }

    public void setPokeList() {
        pokeList = new ArrayList<String>();
        Cursor cursor = allPoke.rawQuery("SELECT Name FROM pkmn ORDER BY Name", null);

        if (cursor != null) {
            cursor.moveToFirst();
            do {
                String pName =  cursor.getString(cursor.getColumnIndex("Name"));
                pokeList.add(pName);
            }while(cursor.moveToNext());
        }
        cursor.close();
    }

    public void checkText(String input){
        if (pokeList.contains(input)){
            // load data
            System.out.println("Pokemon found!");
        }
    }

}

