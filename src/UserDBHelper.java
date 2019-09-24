package com.example.pokemaster;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDBHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "users.db";
    public static final String TABLE_NAME = "registered";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "username";
    public static final String COL_3 = "password";
    public static final String COL_4 = "nickname";
    public static final String COL_5 = "birthdate";

    public UserDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE registered (ID INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT, nickname TEXT, birthdate TEXT)");
}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public long insertUser(String username, String password, String nickname, String birthdate){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("password", password);
        cv.put("nickname", nickname);
        cv.put("birthdate", birthdate);
        long regUser = db.insert("registered", null, cv);

        db.close();

        return regUser;
    }

    public boolean verifyUser(String user, String password) {
        String[] columns = {COL_1};
        SQLiteDatabase db = getReadableDatabase();
        String selection = COL_2 + "=?" + " and " + COL_3 + "=?";
        String[] selectionArgs = {user, password};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int count = ((Cursor) cursor).getCount();
        cursor.close();
        db.close();

        if (count == 1) return true;
        else return false;
    }

}
