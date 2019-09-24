package com.example.pokemaster;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class PokemonViewer extends AppCompatActivity {

    private PokeDBHelper helper;
    private String selectedName,selectedMove;
    private CollectionDBHelper collection;
    private SQLiteDatabase myDB;

    private int num = -2;
    private String type1 = "", type2 = "", location = "", evol = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_viewer);
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
        selectedName = getIntent().getStringExtra("pokeName");
        String pName = "'" + selectedName + "'";

        TextView title = (TextView) findViewById(R.id.pokemonTitle);
        title.setText(selectedName);

        //Set useAgainst as the spinner that is connected to the useAgainst spinner in activity_pokemon_viewer
        Spinner useAgainst = (Spinner) findViewById(R.id.useAgainst);


        initializeDB();

        btnlistener(useAgainst);

        initializeData(pName);
        // Use useAgainst as a parameter in initializeSpinner
        initializeSpinner(useAgainst);
        initializeTexts();


        checkBoxlistener(selectedName);
    }

    public void initializeData(String pName){
        num = helper.getPokeDex(pName);
        String types[] = helper.getType(pName).split(":");
        type1 = types[0];
        type2 = types[1];
        location = helper.getLocation(pName);
        evol = helper.getEvol(pName);
        System.out.println(type1);

    }

    public void initializeTexts(){

        TextView numText = (TextView) findViewById(R.id.numText);
        numText.setText(Integer.toString(num));

        TextView typeText = (TextView) findViewById(R.id.typeText);
        if (type2.equals("None")){
            type2 = "";
        }
        typeText.setText(type1 + " " + type2);

        TextView locationText = (TextView) findViewById(R.id.locationText);
        locationText.setText(location);

    }

    public void checkBoxlistener(final String name) {
        CheckBox caught = (CheckBox) findViewById(R.id.checkBox);
        caught.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    System.out.println(selectedName);
                    Toast.makeText(PokemonViewer.this, name + " added to Collection", Toast.LENGTH_SHORT).show();
                    String query = "UPDATE pkmn SET isCaught = 1 WHERE Name = " + "'" +selectedName+"'";
                    Cursor cursor = myDB.rawQuery(query, null);
                    cursor.close();

//                    ContentValues cv = new ContentValues();
//                    cv.put("isCaught","1");
//                    long add = collection.insertPokemon(num, name, type1, type2, location, evol);
                        long add = 0;
                    if (add > 0) {
//                        Toast.makeText(PokemonViewer.this, name + " added to Collection", Toast.LENGTH_SHORT).show();
                    }else{
//                        Toast.makeText(PokemonViewer.this, "Error: unable to add to Collection", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    //long rmv = collection.deletePokemon(name);
                    long rmv = 0;
                    if (rmv > 0) {
                      //  Toast.makeText(PokemonViewer.this, name + " removed from Collection", Toast.LENGTH_SHORT).show();
                    }else{
                        //Toast.makeText(PokemonViewer.this, "Error: unable to remove to Collection", Toast.LENGTH_SHORT).show();
                    }
                }

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
            myDB = helper.getWritableDatabase();
        } catch (SQLException e) {
            throw e;
        }

    }

    public void initializeSpinner(Spinner useAgainst){
        // Calls a function moveQuery that returns an array of the top 20 moves to use agaainst that type of pokemon
        ArrayList<String> moveList = moveQuery();


        ArrayAdapter<String> moveAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,moveList);
        moveAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        useAgainst.setAdapter(moveAdapter);


    }

    public ArrayList<String> moveQuery() {
        //Pass in a parameter, probably a string, and figure out how to filter the selected moves based on type effectiveness
        ArrayList<String> moveOrder = new ArrayList<String>();
        // Use these arrays to somehow filter out moves that would be weak against that pokemon
       ArrayList<String> weaknesses = weaknesses(type1);
        ArrayList<String> strengths = strengths(type1);


        String beginSQL = "SELECT DISTINCT name,type,power FROM move WHERE ";
        String endSQL = " ORDER BY power DESC LIMIT 30";
        String filter1 = "";
        for(int i = 0; i <strengths.size(); i++){
            if(i == strengths.size() -1){
                filter1 = filter1 + "type != " +"'" + strengths.get(i) + "'" + " OR ";
            }
            else{
                filter1 = filter1 + "type != " +"'" + strengths.get(i) + "'" + " AND ";

            }

        }
        String filter2 = "";
        for(int i = 0; i <weaknesses.size(); i++){
            if(i == weaknesses.size() -1){
                filter2 = filter2 + "type = " +"'" + weaknesses.get(i) + "'";
            }
            else{
                filter2 = filter2 + "type = " +"'" + weaknesses.get(i) + "'" + " OR ";

            }

        }
        String query = beginSQL + filter1 + " " + filter2 + endSQL;
        System.out.println(query);



        Cursor cursor = myDB.rawQuery(query, null);

        if (cursor != null) {
            cursor.moveToFirst();
            do {
                String moveName =  cursor.getString(cursor.getColumnIndex("name"));
                String moveType =  cursor.getString(cursor.getColumnIndex("type"));
                int movePower = cursor.getInt(cursor.getColumnIndex("power"));
                moveOrder.add(moveName +": " + moveType +": " +movePower);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return moveOrder;
    }

    public void btnlistener(final Spinner moveSort){
        // Listens in on this button specifically
        Button select = (Button)findViewById(R.id.pokecheck);
        // Sets the value from the spinner to be the string that is passed on to the next class
        select.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                selectedMove = moveSort.getSelectedItem().toString();
                Intent intent = new Intent(PokemonViewer.this, activity_LearnThisMove.class);
                selectedMove = selectedMove.split(":")[0];

                intent.putExtra("move", selectedMove);
                intent.putExtra("login_name", getIntent().getStringExtra("login_name"));
                startActivity(intent);
            }
        });

    }



    // A function that returns an array list of weakness based on type
    public ArrayList<String> weaknesses(String type){
        ArrayList<String> weakness = new ArrayList<>();

        if(type.equals("Grass")){
            weakness.add("Fire");
            weakness.add("Ice");
            weakness.add("Poison");
            weakness.add("Flying");
            weakness.add("Bug");

        }
        else if(type.equals("Normal")){
            weakness.add("Fight");

        }
        else if(type.equals("Fire")){
            weakness.add("Water");
            weakness.add("Ground");
            weakness.add("Rock");
        }
        else if(type.equals("Water")){
            weakness.add("Electric");
            weakness.add("Grass");
        }
        else if(type.equals("Electric")){
            weakness.add("Ground");
        }
        else if(type.equals("Ice")){
            weakness.add("Fire");
            weakness.add("Fighting");
            weakness.add("Rock");
        }
        else if(type.equals("Fight")){
            weakness.add("Flying");
            weakness.add("Psychic");
        }
        else if(type.equals("Poison")){
            weakness.add("Ground");
            weakness.add("Psychic");
            weakness.add("Bug");
        }
        else if(type.equals("Ground")){
            weakness.add("Water");
            weakness.add("Grass");
            weakness.add("Ice");
        }
        else if(type.equals("Flying")){
            weakness.add("Electric");
            weakness.add("Ice");
            weakness.add("Rock");
        }
        else if(type.equals("Psychic")){
            weakness.add("Bug");
        }
        else if(type.equals("Bug")){
            weakness.add("Fire");
            weakness.add("Poison");
            weakness.add("Flying");
            weakness.add("Rock");
        }
        else if(type.equals("Rock")){
            weakness.add("Water");
            weakness.add("Grass");
            weakness.add("Fighting");
            weakness.add("Ground");

        }
        else if(type.equals("Ghost")){
            weakness.add("Ghost");
        }
        else if(type.equals("Dragon")){
            weakness.add("Ice");
            weakness.add("Dragon");

        }
        return weakness;

    }
    // A function that returns an array list of strengths based on type
    public ArrayList<String> strengths(String type){
        ArrayList<String> strengths = new ArrayList<>();

        if(type.equals("Grass")){
           strengths.add("Water");
            strengths.add("Ground");
            strengths.add("Rock");

        }
        else if(type.equals("Normal")){

        }
        else if(type.equals("Fire")){
            strengths.add("Grass");
            strengths.add("Ice");
            strengths.add("Bug");
        }
        else if(type.equals("Water")){
            strengths.add("Fire");
            strengths.add("Ground");
            strengths.add("Rock");
        }
        else if(type.equals("Electric")){
            strengths.add("Water");
            strengths.add("Flying");
        }
        else if(type.equals("Ice")){
            strengths.add("Grass");
            strengths.add("Ground");
            strengths.add("Flying");
            strengths.add("Dragon");

        }
        else if(type.equals("Fight")){
            strengths.add("Normal");
            strengths.add("Fight");
            strengths.add("Rock");
        }
        else if(type.equals(" Poison")){
            strengths.add("Grass");
            strengths.add("Bug");
        }
        else if(type.equals("Ground")){
            strengths.add("Fire");
            strengths.add("Electric");
            strengths.add("Poison");
            strengths.add("Rock");
        }
        else if(type.equals("Flying")){
            strengths.add("Grass");
            strengths.add("Fight");
            strengths.add("Bug");

        }
        else if(type.equals("Psychic")){
            strengths.add("Fight");
            strengths.add("Poison");

        }
        else if(type.equals("Bug")){
            strengths.add("Grass");
            strengths.add("Poison");
            strengths.add("Psychic");
        }
        else if(type.equals("Rock")){
            strengths.add("Fire");
            strengths.add("Ice");
            strengths.add("Flying");
            strengths.add("Rock");

        }
        else if(type.equals("Ghost")){
            strengths.add("Ghost");
        }
        else if(type.equals("Dragon")){
            strengths.add("Ice");
            strengths.add("Dragon");

        }
        return strengths;

    }

}
