package com.synergysolutions.myshow.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sdelafuente on 31/03/2014.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "charactersManager";

    // Characters table name
    private static final String TABLE_CHARACTERS = "characters";

    // Characters Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ALIAS = "alias";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CHARACTERS_TABLE = "CREATE TABLE " + TABLE_CHARACTERS
                + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_ALIAS + " TEXT"
                + ")";
        db.execSQL(CREATE_CHARACTERS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHARACTERS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new character
    void addCharacter(Character character) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, character.getName()); // Character Name
        values.put(KEY_ALIAS, character.getAlias()); // Character Phone

        // Inserting Row
        db.insert(TABLE_CHARACTERS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single character
    Character getCharacter(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CHARACTERS, new String[] { KEY_ID,
                        KEY_NAME, KEY_ALIAS }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Character character = new Character(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
        // return character
        return character;
    }

    // Getting All Characters
    public List<Character> getAllCharacters() {
        List<Character> characterList = new ArrayList<Character>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CHARACTERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Character character = new Character();
                character.setId(Integer.parseInt(cursor.getString(0)));
                character.setName(cursor.getString(1));
                character.setAlias(cursor.getString(2));
                // Adding character to list
                characterList.add(character);
            } while (cursor.moveToNext());
        }

        // return character list
        return characterList;
    }

    // Updating single character
    public int updateCharacter(Character character) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, character.getName());
        values.put(KEY_ALIAS, character.getAlias());

        // updating row
        return db.update(TABLE_CHARACTERS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(character.getId()) });
    }

    // Deleting single character
    public void deleteCharacter(Character character) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CHARACTERS, KEY_ID + " = ?",
                new String[] { String.valueOf(character.getId()) });
        db.close();
    }


    // Getting characters Count
    public int getCharactersCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CHARACTERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}