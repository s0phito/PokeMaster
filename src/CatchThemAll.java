package com.example.pokemaster;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.io.IOException;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;



public class CatchThemAll extends AppCompatActivity {
    private PokeDBHelper helper;
    private SQLiteDatabase myDB;
    private String selectedPoke1, selectedPoke2,selectedPoke3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catch_them_all);

        Spinner AlphabetSort = (Spinner) findViewById(R.id.Alphabet);
        Spinner IndexSort = (Spinner) findViewById(R.id.Index);

        initializeDB();
        initializeSpinner(AlphabetSort, IndexSort);
        btnlistener(AlphabetSort, IndexSort);
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

    public ArrayList<String> alphabeticalQuery() {
        ArrayList<String> alphabeticalOrder = new ArrayList<String>();
        Cursor cursor = myDB.rawQuery("SELECT Name FROM pkmn ORDER BY Name", null);

        if (cursor != null) {
            cursor.moveToFirst();
            do {
                String pName =  cursor.getString(cursor.getColumnIndex("Name"));
                alphabeticalOrder.add(pName);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return alphabeticalOrder;
    }

    public ArrayList<String> numericalQuery() {
        ArrayList<String> numericalOrder = new ArrayList<String>();
        Cursor cursor = myDB.rawQuery("SELECT PokeDex, Name FROM pkmn", null);

        if (cursor != null) {
            cursor.moveToFirst();
            do {
                String pName =  cursor.getString(cursor.getColumnIndex("Name"));
                int pNum =  cursor.getInt(cursor.getColumnIndex("PokeDex"));
                numericalOrder.add(pNum +": " + pName);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return numericalOrder;
    }

    public void initializeSpinner(Spinner AlphabetSort, Spinner IndexSort){
        ArrayList<String> alphaList = alphabeticalQuery();
        ArrayList<String> numList = numericalQuery();

        ArrayAdapter<String> nAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,numList);
        nAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        IndexSort.setAdapter(nAdapter);


        ArrayAdapter<String> alAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, alphaList);
        alAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        AlphabetSort.setAdapter(alAdapter);

    }


    public void btnlistener(final Spinner AlphabetSort, final Spinner IndexSort) {
        // Buttons used to view pokemon either by alphabetical order or numerical. Each passes the
        // pokemon name as a string and opens a new activity with that information displayed
        Button select = (Button)findViewById(R.id.select);
        Button select2 = (Button)findViewById(R.id.select2);


        select.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                selectedPoke1 = AlphabetSort.getSelectedItem().toString();
                Intent intent = new Intent(CatchThemAll.this, PokemonViewer.class);
                intent.putExtra("pokeName", selectedPoke1);
                intent.putExtra("login_name", getIntent().getStringExtra("login_name"));
                startActivity(intent);
            }
        });

        select2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                selectedPoke2 = IndexSort.getSelectedItem().toString();
                Intent intent = new Intent(CatchThemAll.this, PokemonViewer.class);
                int cut = selectedPoke2.indexOf(':') + 2;
                intent.putExtra("pokeName", selectedPoke2.substring(cut));
                intent.putExtra("login_name", getIntent().getStringExtra("login_name"));
                startActivity(intent);
            }
        });

    }
}
