package com.example.pokemaster;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PokeDBHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "pokemaster.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE1_NAME = "pkmn";
    public static final String COL1_1 = "PokeDex";
    public static final String COL1_2 = "Name";
    public static final String COL1_3 = "Type1";
    public static final String COL1_4 = "Type2";
    public static final String COL1_5 = "Location";
    public static final String COL1_6 = "Evol";

    public static final String TABLE2_NAME = "move";
    public static final String COL2_1 = "name";
    public static final String COL2_2 = "type";
    public static final String COL2_3 = "power";
    public static final String COL2_4 = "accuracy";
    public static final String COL2_5 = "pkm";
    public static final String COL2_6 = "method";

    private final Context myContext;
    private final String pathName;
    private boolean changed = false;


    public PokeDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        if (android.os.Build.VERSION.SDK_INT >= 17)
            pathName = context.getApplicationInfo().dataDir + "/databases/";
        else
            pathName = "/data/data/" + context.getPackageName() + "/databases/";
        this.myContext = context;

        getDataBase();



        this.getReadableDatabase();
    }

    public void update() throws IOException {
        if (changed) {
            File file = new File(pathName + DATABASE_NAME);
            if (file.exists())
                file.delete();

            getDataBase();

            changed = false;
        }
    }

    private void getDataBase() {
        File file = new File(pathName + DATABASE_NAME);

        if (!file.exists()) {
            this.getReadableDatabase();
            this.close();
            try {
                InputStream in = myContext.getAssets().open(DATABASE_NAME);
                OutputStream out = new FileOutputStream(pathName + DATABASE_NAME);
                byte[] mBuffer = new byte[1024];
                int size;
                while ((size = in.read(mBuffer)) > 0)
                    out.write(mBuffer, 0, size);
                out.flush();
                out.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace(System.out);
            }
        }
    }

    public int getPokeDex(String pName){
        SQLiteDatabase myDB = getReadableDatabase();
        int number = -1;
        Cursor cursor = myDB.rawQuery("SELECT PokeDex FROM pkmn WHERE Name = " + pName, null);
        if (cursor != null) {
            cursor.moveToFirst();
            number = cursor.getInt(cursor.getColumnIndex("PokeDex"));
        }
        cursor.close();

        return number;
    }

    public String getType(String pName){
        SQLiteDatabase myDB = getReadableDatabase();
        String type1 = "", type2 = "";
        Cursor cursor = myDB.rawQuery("SELECT Type1, Type2 FROM pkmn WHERE Name = " + pName, null);
        if (cursor != null) {
            cursor.moveToFirst();
            type1 = cursor.getString(cursor.getColumnIndex("Type1"));
            type2 = cursor.getString(cursor.getColumnIndex("Type2"));
        }
        cursor.close();

        return type1 + ":" + type2;
    }

    public String getLocation(String pName){
        SQLiteDatabase myDB = getReadableDatabase();
        String location = "";
        Cursor cursor = myDB.rawQuery("SELECT Location FROM pkmn WHERE Name = " + pName, null);
        if (cursor != null) {
            cursor.moveToFirst();
            location = cursor.getString(cursor.getColumnIndex("Location"));
        }
        cursor.close();

        return location;
    }

    public String getEvol(String pName){
        SQLiteDatabase myDB = getReadableDatabase();
        String Evol = "";
        Cursor cursor = myDB.rawQuery("SELECT Evol FROM pkmn WHERE Name = " + pName, null);
        if (cursor != null) {
            cursor.moveToFirst();
            Evol = cursor.getString(cursor.getColumnIndex("Evol"));
        }
        cursor.close();

        return Evol;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            changed = true;
        }
    }


}
