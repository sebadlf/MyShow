package com.synergysolutions.myshow.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;

import com.synergysolutions.myshow.app.Entity.Article;
import com.synergysolutions.myshow.app.Entity.Section;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sdelafuente on 31/03/2014.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "MyShow";

    // Characters table name
    private static final String TABLE_ARTICLE = "Article";
    private static final String TABLE_SECTION = "Section";
    private static final String TABLE_SECTION_CONTENT = "SectionContent";
    private static final String TABLE_LIST_ELEMENT = "ListElement";
    private static final String TABLE_SECTION_IMAGE = "SectionImages";

    // Article Table Columns names
    private static final String ARTICLE_ID = "_id";
    private static final String ARTICLE_WIKIA_ID = "wikiaId";
    private static final String ARTICLE_TITLE = "title";
    private static final String ARTICLE_URL = "url";
    private static final String ARTICLE_TYPE = "articleType";
    private static final String ARTICLE_TEASER = "teaser";
    private static final String ARTICLE_THUMBNAIL = "thumbnail";
    private static final String ARTICLE_ORIGINAL_DIMENSIONS = "original_dimensions";

    // Section Table Columns names
    private static final String SECTION_ID = "_id";
    private static final String SECTION_ARTICLE_ID = "articleId";
    private static final String SECTION_TITLE = "title";
    private static final String SECTION_LEVEL = "level";

    // SectionContent Table Columns names
    private static final String SECTION_CONTENT_ID = "_id";
    private static final String SECTION_CONTENT_SECTION_ID = "sectionId";
    private static final String SECTION_CONTENT_TYPE = "type";
    private static final String SECTION_CONTENT_TEXT = "text";

    // ElementList Table Columns names
    private static final String ELEMENT_LIST_ID = "_id";
    private static final String ELEMENT_LIST_SECTION_CONTENT_ID = "sectionContentId";
    private static final String ELEMENT_LIST_CONTENT_TEXT = "text";

    // SectionContent Table Columns names
    private static final String SECTION_IMAGE_ID = "_id";
    private static final String SECTION_IMAGE_SECTION_ID = "sectionId";
    private static final String SECTION_IMAGE_SRC = "src";
    private static final String SECTION_IMAGE_CAPTION = "caption";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_ARTICLE_TABLE = "CREATE TABLE " + TABLE_ARTICLE
                + "("
                + ARTICLE_ID + " INTEGER PRIMARY KEY,"
                + ARTICLE_WIKIA_ID + " INTEGER,"
                + ARTICLE_TITLE + " TEXT,"
                + ARTICLE_URL + " TEXT,"
                + ARTICLE_TYPE + " TEXT,"
                + ARTICLE_TEASER + " TEXT,"
                + ARTICLE_THUMBNAIL + " TEXT,"
                + ARTICLE_ORIGINAL_DIMENSIONS + " TEXT"
                + ")";

        String CREATE_SECTION_TABLE = "CREATE TABLE " + TABLE_SECTION
                + "("
                + SECTION_ID + " INTEGER PRIMARY KEY,"
                + SECTION_ARTICLE_ID + " INTEGER REFERENCES " + TABLE_ARTICLE + "(" + ARTICLE_ID + "),"
                + SECTION_TITLE + " TEXT,"
                + SECTION_LEVEL + " TEXT"
                + ")";

        String CREATE_SECTION_CONTENT_TABLE = "CREATE TABLE " + TABLE_SECTION_CONTENT
                + "("
                + SECTION_CONTENT_ID + " INTEGER PRIMARY KEY,"
                + SECTION_CONTENT_SECTION_ID + " INTEGER REFERENCES " + TABLE_SECTION + "(" + SECTION_ID + "),"
                + SECTION_CONTENT_TYPE + " TEXT,"
                + SECTION_CONTENT_TEXT + " TEXT"
                + ")";

        String CREATE_LIST_ELEMENT_TABLE = "CREATE TABLE " + TABLE_LIST_ELEMENT
                + "("
                + ELEMENT_LIST_ID + " INTEGER PRIMARY KEY,"
                + ELEMENT_LIST_SECTION_CONTENT_ID + " INTEGER REFERENCES " + TABLE_SECTION_CONTENT + "(" + SECTION_CONTENT_ID + "),"
                + ELEMENT_LIST_CONTENT_TEXT + " TEXT"
                + ")";

        String CREATE_SECTION_IMAGE_TABLE = "CREATE TABLE " + TABLE_SECTION_IMAGE
                + "("
                + SECTION_IMAGE_ID + " INTEGER PRIMARY KEY,"
                + SECTION_IMAGE_SECTION_ID + " INTEGER REFERENCES " + TABLE_SECTION + "(" + SECTION_ID + "),"
                + SECTION_IMAGE_SRC + " TEXT,"
                + SECTION_IMAGE_CAPTION + " TEXT"
                + ")";

        db.execSQL(CREATE_ARTICLE_TABLE);
        db.execSQL(CREATE_SECTION_TABLE);
        db.execSQL(CREATE_SECTION_CONTENT_TABLE);
        db.execSQL(CREATE_LIST_ELEMENT_TABLE);
        db.execSQL(CREATE_SECTION_IMAGE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SECTION_IMAGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIST_ELEMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SECTION_CONTENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SECTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLE);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new character
    void saveArticle(Article article) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(ARTICLE_WIKIA_ID, article.getWikiaId());
        values.put(ARTICLE_TITLE, article.getTitle());
        values.put(ARTICLE_URL, article.getUrl());
        values.put(ARTICLE_TYPE, article.getTitle());
        values.put(ARTICLE_TEASER, article.getTeaser());
        values.put(ARTICLE_THUMBNAIL, article.getThumbnail());
        values.put(ARTICLE_ORIGINAL_DIMENSIONS, article.getOriginalDimensions());

        if (article.getId() == 0){
            db.insert(TABLE_ARTICLE, null, values);
            article.setId(this.getLastInsertedId(db, TABLE_ARTICLE));
        } else {
            db.update(TABLE_ARTICLE, values, ARTICLE_ID + "= ?",  new String[] { String.valueOf(article.getId()) });
        }

        for (Section section : article.getSections()){

            values = new ContentValues();

            values.put(SECTION_ARTICLE_ID, article.getWikiaId());
            values.put(SECTION_TITLE, article.getTitle());
            values.put(SECTION_LEVEL, article.getUrl());

            if (section.getId() == 0){
                db.insert(TABLE_SECTION, null, values);
                section.setId(this.getLastInsertedId(db, TABLE_SECTION));
            } else {
                db.update(TABLE_SECTION, values, SECTION_ID + "= ?",  new String[] { String.valueOf(section.getId()) });
            }

        }

        db.close(); // Closing database connection
    }

    private int getLastInsertedId(SQLiteDatabase db, String table){
        int lastId = 0;

        String query = "SELECT ROWID from " + table + " order by ROWID DESC limit 1";
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.moveToFirst()) {
            lastId = c.getInt(0); //The 0 is the column index, we only have 1 column, so the index is 0
        }

        return lastId;
    }

    // Getting single character
    List<Article> getAllArticles() {

        List<Article> articleList = new ArrayList<Article>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ARTICLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        HashMap<Integer, Article> articlesCache = new HashMap<Integer, Article>();

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Article article = new Article();

                article.setId(cursor.getInt(cursor.getColumnIndex(ARTICLE_ID)));
                article.setWikiaId(cursor.getInt(cursor.getColumnIndex(ARTICLE_WIKIA_ID)));

                article.setTitle(cursor.getString(cursor.getColumnIndex(ARTICLE_TITLE)));
                article.setUrl(cursor.getString(cursor.getColumnIndex(ARTICLE_URL)));
                article.setArticleType(cursor.getString(cursor.getColumnIndex(ARTICLE_TYPE)));
                article.setTeaser(cursor.getString(cursor.getColumnIndex(ARTICLE_TEASER)));

                article.setThumbnail(cursor.getString(cursor.getColumnIndex(ARTICLE_THUMBNAIL)));
                article.setOriginalDimensions(cursor.getString(cursor.getColumnIndex(ARTICLE_ORIGINAL_DIMENSIONS)));

                articlesCache.put(article.getId(), article);

                articleList.add(article);
            } while (cursor.moveToNext());
        }

        //Sections

        String filter = TextUtils.join(",", articlesCache.keySet());

        selectQuery = "SELECT  * FROM " + TABLE_SECTION + " WHERE " + SECTION_ARTICLE_ID + " IN (" + filter + ")";
        cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Section section = new Section();

                Article article = articlesCache.get(cursor.getInt(cursor.getColumnIndex(SECTION_ARTICLE_ID)));

                article.setId(cursor.getInt(cursor.getColumnIndex(ARTICLE_ID)));
                article.setWikiaId(cursor.getInt(cursor.getColumnIndex(ARTICLE_WIKIA_ID)));

                article.setTitle(cursor.getString(cursor.getColumnIndex(ARTICLE_TITLE)));
                article.setUrl(cursor.getString(cursor.getColumnIndex(ARTICLE_URL)));
                article.setArticleType(cursor.getString(cursor.getColumnIndex(ARTICLE_TYPE)));
                article.setTeaser(cursor.getString(cursor.getColumnIndex(ARTICLE_TEASER)));

                article.setThumbnail(cursor.getString(cursor.getColumnIndex(ARTICLE_THUMBNAIL)));
                article.setOriginalDimensions(cursor.getString(cursor.getColumnIndex(ARTICLE_ORIGINAL_DIMENSIONS)));

                articlesCache.put(article.getId(), article);

                articleList.add(article);
            } while (cursor.moveToNext());
        }


        // return character list
        return articleList;
    }

    // Getting single character
    List<Article> getAllArticlesWithoutDetails() {

        List<Article> articleList = new ArrayList<Article>();
        // Select All Query
        String selectQuery = null; //"SELECT  * FROM " + TABLE_ARTICLE + " where " + ARTICLE_DETAILS_DOWNLOADED + " != 1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Article article = new Article();

                article.setId(cursor.getInt(cursor.getColumnIndex(ARTICLE_ID)));
                article.setWikiaId(cursor.getInt(cursor.getColumnIndex(ARTICLE_WIKIA_ID)));

                article.setTitle(cursor.getString(cursor.getColumnIndex(ARTICLE_TITLE)));
                article.setUrl(cursor.getString(cursor.getColumnIndex(ARTICLE_URL)));
                article.setArticleType(cursor.getString(cursor.getColumnIndex(ARTICLE_TYPE)));
                article.setTeaser(cursor.getString(cursor.getColumnIndex(ARTICLE_TEASER)));

                article.setThumbnail(cursor.getString(cursor.getColumnIndex(ARTICLE_THUMBNAIL)));
                article.setOriginalDimensions(cursor.getString(cursor.getColumnIndex(ARTICLE_ORIGINAL_DIMENSIONS)));

                // Adding character to list
                articleList.add(article);
            } while (cursor.moveToNext());
        }

        // return character list
        return articleList;
    }


    long getArticlesCount() {

        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT COUNT(*) FROM " + TABLE_ARTICLE;
        SQLiteStatement statement = db.compileStatement(sql);
        long count = statement.simpleQueryForLong();
        return count;
    }

    Article getArticle(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ARTICLE, new String[] {
                        ARTICLE_ID,
                        ARTICLE_WIKIA_ID,
                        ARTICLE_TITLE,
                        ARTICLE_URL,
                        ARTICLE_TYPE,
                        ARTICLE_ABSTRACT,
                        ARTICLE_THUMBNAIL,
                        ARTICLE_ORIGINAL_DIMENSIONS,
                }, ARTICLE_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Article article = new Article();

        article.setId(cursor.getInt(cursor.getColumnIndex(ARTICLE_ID)));
        article.setWikiaId(cursor.getInt(cursor.getColumnIndex(ARTICLE_WIKIA_ID)));

        article.setTitle(cursor.getString(cursor.getColumnIndex(ARTICLE_TITLE)));
        article.setUrl(cursor.getString(cursor.getColumnIndex(ARTICLE_URL)));
        article.setType(cursor.getString(cursor.getColumnIndex(ARTICLE_TYPE)));
        article.setAbstractDesc(cursor.getString(cursor.getColumnIndex(ARTICLE_ABSTRACT)));

        article.setThumbnail(cursor.getString(cursor.getColumnIndex(ARTICLE_THUMBNAIL)));
        article.setOriginalDimensions(cursor.getString(cursor.getColumnIndex(ARTICLE_ORIGINAL_DIMENSIONS)));

        // return character
        return article;
    }

    Article getArticle(String title) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ARTICLE, new String[] {
                        ARTICLE_ID,
                        ARTICLE_WIKIA_ID,
                        ARTICLE_TITLE,
                        ARTICLE_URL,
                        ARTICLE_TYPE,
                        ARTICLE_ABSTRACT,
                        ARTICLE_THUMBNAIL,
                        ARTICLE_ORIGINAL_DIMENSIONS,
                }, ARTICLE_TITLE + "=?",
                new String[] { title }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Article article = new Article();

        article.setId(cursor.getInt(cursor.getColumnIndex(ARTICLE_ID)));
        article.setWikiaId(cursor.getInt(cursor.getColumnIndex(ARTICLE_WIKIA_ID)));

        article.setTitle(cursor.getString(cursor.getColumnIndex(ARTICLE_TITLE)));
        article.setUrl(cursor.getString(cursor.getColumnIndex(ARTICLE_URL)));
        article.setType(cursor.getString(cursor.getColumnIndex(ARTICLE_TYPE)));
        article.setAbstractDesc(cursor.getString(cursor.getColumnIndex(ARTICLE_ABSTRACT)));

        article.setThumbnail(cursor.getString(cursor.getColumnIndex(ARTICLE_THUMBNAIL)));
        article.setOriginalDimensions(cursor.getString(cursor.getColumnIndex(ARTICLE_ORIGINAL_DIMENSIONS)));

        // return character
        return article;
    }

    /*
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
    */
}