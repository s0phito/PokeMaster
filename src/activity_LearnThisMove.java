package com.example.pokemaster;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class activity_LearnThisMove extends AppCompatActivity {
    public String move;
    private PokeDBHelper helper;
    private SQLiteDatabase myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__learn_this_move);

        //Retrieve the name that is put in
        this.move = getIntent().getStringExtra("move");


        TextView title = (TextView) findViewById(R.id.moveTitle);
        title.setText(move);
        Spinner checkPKMN = (Spinner) findViewById(R.id.checkPKMN);

        initializeDB();
        initializeSpinner(checkPKMN);
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



    public void initializeSpinner(Spinner pokemon){
        // Calls a function moveQuery that returns an array of the top 20 moves to use agaainst that type of pokemon
        ArrayList<String> pokeList = checkPokemon();
        //System.out.println(this.move);


        ArrayAdapter<String> pkmnAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,pokeList);
        pkmnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pokemon.setAdapter(pkmnAdapter);


    }

    public ArrayList<String> checkPokemon( ) {
        //Pass in a parameter, probably a string, and figure out how to filter the selected moves based on type effectiveness
        ArrayList<String> pkmnlist = new ArrayList<String>();
        System.out.println(this.move);
        String movename = "'"+ move +"'";
        String query = "SELECT pkmn FROM move WHERE name ="+"'" +this.move + "' ";
        Cursor cursor = myDB.rawQuery(query, null);

        if (cursor != null) {
            cursor.moveToFirst();
            do {
                String name =  cursor.getString(cursor.getColumnIndex("pkmn"));

                pkmnlist.add(name);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return pkmnlist;
    }

}


