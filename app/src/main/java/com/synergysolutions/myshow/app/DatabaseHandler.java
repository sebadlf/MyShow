package com.synergysolutions.myshow.app;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;

import com.synergysolutions.myshow.app.Entity.Alias;
import com.synergysolutions.myshow.app.Entity.Article;
import com.synergysolutions.myshow.app.Entity.LinkedArticle;
import com.synergysolutions.myshow.app.Entity.ListElement;
import com.synergysolutions.myshow.app.Entity.Section;
import com.synergysolutions.myshow.app.Entity.SectionContent;
import com.synergysolutions.myshow.app.Entity.SectionImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sdelafuente on 31/03/2014.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = BuildConfig.VERSION_CODE;

    // Database Name
    private static final String DATABASE_NAME = "MyShow";

    // Characters table name
    private static final String TABLE_ARTICLE = "Article";
    private static final String TABLE_ALIAS = "Alias";
    private static final String TABLE_SECTION = "Section";
    private static final String TABLE_SECTION_CONTENT = "SectionContent";
    private static final String TABLE_LIST_ELEMENT = "ListElement";
    private static final String TABLE_SECTION_IMAGE = "SectionImages";
    private static final String TABLE_LINKED_ARTICLE = "LinkedArticle";

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
    private static final String LIST_ELEMENT_ID = "_id";
    private static final String LIST_ELEMENT_SECTION_CONTENT_ID = "sectionContentId";
    private static final String LIST_ELEMENT_TEXT = "text";

    // SectionContent Table Columns names
    private static final String SECTION_IMAGE_ID = "_id";
    private static final String SECTION_IMAGE_SECTION_ID = "sectionId";
    private static final String SECTION_IMAGE_SRC = "src";
    private static final String SECTION_IMAGE_CAPTION = "caption";

    // LinkedArticle Table Columns names
    private static final String LINKED_ARTICLE_ID = "_id";
    private static final String LINKED_ARTICLE_SECTION_CONTENT_ID = "sectionContentId";
    private static final String LINKED_ARTICLE_LIST_ELEMENT_ID = "listElementId";
    private static final String LINKED_ARTICLE_ALIAS = "alias";

    // LinkedArticle Table Columns names
    private static final String ALIAS_ID = "_id";
    private static final String ALIAS_ARTICLE_ID = "articleId";
    private static final String ALIAS_TITLE = "title";

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

        String CREATE_ALIAS_TABLE = "CREATE TABLE " + TABLE_ALIAS
                + "("
                + ALIAS_ID + " INTEGER PRIMARY KEY,"
                + ALIAS_ARTICLE_ID + " INTEGER REFERENCES " + TABLE_ARTICLE + "(" + ARTICLE_ID + "),"
                + ALIAS_TITLE + " TEXT"
                + ")";

        String CREATE_SECTION_TABLE = "CREATE TABLE " + TABLE_SECTION
                + "("
                + SECTION_ID + " INTEGER PRIMARY KEY,"
                + SECTION_ARTICLE_ID + " INTEGER REFERENCES " + TABLE_ARTICLE + "(" + ARTICLE_ID + "),"
                + SECTION_TITLE + " TEXT,"
                + SECTION_LEVEL + " INTEGER"
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
                + LIST_ELEMENT_ID + " INTEGER PRIMARY KEY,"
                + LIST_ELEMENT_SECTION_CONTENT_ID + " INTEGER REFERENCES " + TABLE_SECTION_CONTENT + "(" + SECTION_CONTENT_ID + "),"
                + LIST_ELEMENT_TEXT + " TEXT"
                + ")";

        String CREATE_SECTION_IMAGE_TABLE = "CREATE TABLE " + TABLE_SECTION_IMAGE
                + "("
                + SECTION_IMAGE_ID + " INTEGER PRIMARY KEY,"
                + SECTION_IMAGE_SECTION_ID + " INTEGER REFERENCES " + TABLE_SECTION + "(" + SECTION_ID + "),"
                + SECTION_IMAGE_SRC + " TEXT,"
                + SECTION_IMAGE_CAPTION + " TEXT"
                + ")";

        String CREATE_LINKED_ARTICLES_TABLE = "CREATE TABLE " + TABLE_LINKED_ARTICLE
                + "("
                + LINKED_ARTICLE_ID + " INTEGER PRIMARY KEY,"
                + LINKED_ARTICLE_SECTION_CONTENT_ID + " INTEGER REFERENCES " + TABLE_SECTION_CONTENT + "(" + SECTION_CONTENT_ID + "),"
                + LINKED_ARTICLE_LIST_ELEMENT_ID + " INTEGER REFERENCES " + TABLE_LIST_ELEMENT + "(" + LIST_ELEMENT_ID + "),"
                + LINKED_ARTICLE_ALIAS + " TEXT"
                + ")";

        db.execSQL(CREATE_ARTICLE_TABLE);
        db.execSQL(CREATE_ALIAS_TABLE);
        db.execSQL(CREATE_SECTION_TABLE);
        db.execSQL(CREATE_SECTION_CONTENT_TABLE);
        db.execSQL(CREATE_LIST_ELEMENT_TABLE);
        db.execSQL(CREATE_SECTION_IMAGE_TABLE);
        db.execSQL(CREATE_LINKED_ARTICLES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LINKED_ARTICLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SECTION_IMAGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIST_ELEMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SECTION_CONTENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SECTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALIAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLE);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new character
    void insertArticles(List<Article> articleList) {
        long lastId = 0;

        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();

        for (Article article : articleList) {

            ContentValues values = new ContentValues();

            values.put(ARTICLE_WIKIA_ID, article.getWikiaId());
            values.put(ARTICLE_TITLE, article.getTitle());
            values.put(ARTICLE_URL, article.getUrl());
            values.put(ARTICLE_TYPE, article.getArticleType());
            values.put(ARTICLE_TEASER, article.getTeaser());
            values.put(ARTICLE_THUMBNAIL, article.getThumbnail());
            values.put(ARTICLE_ORIGINAL_DIMENSIONS, article.getOriginalDimensions());

            lastId = db.insert(TABLE_ARTICLE, null, values);
            article.setId(lastId);

            for (Alias alias : article.getAliases()) {

                values = new ContentValues();

                values.put(ALIAS_ARTICLE_ID, article.getId());
                values.put(ALIAS_TITLE, alias.getTitle());

                lastId = db.insert(TABLE_ALIAS, null, values);
                alias.setId(lastId);
            }

            for (Section section : article.getSections()) {

                values = new ContentValues();

                values.put(SECTION_ARTICLE_ID, article.getId());
                values.put(SECTION_TITLE, section.getTitle());
                values.put(SECTION_LEVEL, section.getLevel());

                lastId = db.insert(TABLE_SECTION, null, values);
                section.setId(lastId);

                for (SectionContent sectionContent : section.getSectionContents()) {

                    values = new ContentValues();

                    values.put(SECTION_CONTENT_SECTION_ID, section.getId());
                    values.put(SECTION_CONTENT_TYPE, sectionContent.getType());
                    values.put(SECTION_CONTENT_TEXT, sectionContent.getText());

                    lastId = db.insert(TABLE_SECTION_CONTENT, null, values);
                    sectionContent.setId(lastId);

                    for (LinkedArticle linkedArticle : sectionContent.getLinkedArticles()) {

                        values = new ContentValues();

                        values.put(LINKED_ARTICLE_SECTION_CONTENT_ID, sectionContent.getId());
                        values.put(LINKED_ARTICLE_ALIAS, linkedArticle.getAlias());

                        lastId = db.insert(TABLE_LINKED_ARTICLE, null, values);
                        linkedArticle.setId(lastId);
                    }

                    for (ListElement listElement : sectionContent.getListElements()) {

                        values = new ContentValues();

                        values.put(LIST_ELEMENT_SECTION_CONTENT_ID, sectionContent.getId());
                        values.put(LIST_ELEMENT_TEXT, listElement.getText());

                        lastId = db.insert(TABLE_LIST_ELEMENT, null, values);
                        listElement.setId(lastId);

                        for (LinkedArticle linkedArticle : listElement.getLinkedArticles()) {

                            values = new ContentValues();

                            values.put(LINKED_ARTICLE_LIST_ELEMENT_ID, listElement.getId());
                            values.put(LINKED_ARTICLE_ALIAS, linkedArticle.getAlias());

                            lastId = db.insert(TABLE_LINKED_ARTICLE, null, values);
                            linkedArticle.setId(lastId);
                        }
                    }
                }

                for (SectionImage sectionImage : section.getSectionImages()) {

                    values = new ContentValues();

                    values.put(SECTION_IMAGE_SECTION_ID, section.getId());
                    values.put(SECTION_IMAGE_SRC, sectionImage.getSrc());
                    values.put(SECTION_IMAGE_CAPTION, sectionImage.getCaption());

                    lastId = db.insert(TABLE_SECTION_IMAGE, null, values);
                    sectionImage.setId(lastId);
                }

            }
        }

        db.setTransactionSuccessful();
        db.endTransaction();

        db.close(); // Closing database connection
    }

    private int getLastInsertedId(SQLiteDatabase db, String table){
        int lastId = 0;

        String query = "SELECT ROWID from " + table + " order by ROWID DESC limit 1";
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.moveToFirst()) {
            lastId = c.getInt(0); //The 0 is the column index, we only have 1 column, so the index is 0
        }

        c.close();

        return lastId;
    }

    // Getting single character
    List<Article> getAllArticles() {

        List<Article> articleList = new ArrayList<Article>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ARTICLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        HashMap<Long, Article> articlesCache = new HashMap<Long, Article>();

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

        db.close();

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

        db.close();

        // return character list
        return articleList;
    }

    long getArticlesCount() {

        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT COUNT(*) FROM " + TABLE_ARTICLE;
        SQLiteStatement statement = db.compileStatement(sql);
        long count = statement.simpleQueryForLong();

        db.close();

        return count;
    }

    Article getArticle(long id) {

        String field = ARTICLE_ID;

        Object value = id;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ARTICLE, new String[] {
                        ARTICLE_ID,
                        ARTICLE_WIKIA_ID,
                        ARTICLE_TITLE,
                        ARTICLE_URL,
                        ARTICLE_TYPE,
                        ARTICLE_TEASER,
                        ARTICLE_THUMBNAIL,
                        ARTICLE_ORIGINAL_DIMENSIONS,
                }, field + "=?",
                new String[] { String.valueOf(value) }, null, null, null, null);

        Article article = null;

        if (cursor.moveToFirst()){
            article = this.getArticle(db, cursor, true);
        }

        db.close();

        return article;
    }

    Article getArticle(String title, boolean getSections) {

        title = title.replace("'", "''");

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT " + TABLE_ARTICLE + ".* FROM " + TABLE_ARTICLE
                            + " JOIN " + TABLE_ALIAS
                            + " ON " + TABLE_ARTICLE + "." + ARTICLE_ID + " = " + TABLE_ALIAS + "." + ALIAS_ARTICLE_ID
                            + " WHERE " + TABLE_ALIAS + "." + ALIAS_TITLE + " = " + "'" + title + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        Article article = null;

        if (cursor.moveToFirst()){
            article = this.getArticle(db, cursor, getSections);
        }

        db.close();

        return article;
    }

    private Article getArticle(SQLiteDatabase db, Cursor cursor, boolean getSections) {

        Article article = new Article();

        article.setId(cursor.getInt(cursor.getColumnIndex(ARTICLE_ID)));
        article.setWikiaId(cursor.getInt(cursor.getColumnIndex(ARTICLE_WIKIA_ID)));

        article.setTitle(cursor.getString(cursor.getColumnIndex(ARTICLE_TITLE)));
        article.setUrl(cursor.getString(cursor.getColumnIndex(ARTICLE_URL)));
        article.setArticleType(cursor.getString(cursor.getColumnIndex(ARTICLE_TYPE)));
        article.setTeaser(cursor.getString(cursor.getColumnIndex(ARTICLE_TEASER)));

        article.setThumbnail(cursor.getString(cursor.getColumnIndex(ARTICLE_THUMBNAIL)));
        article.setOriginalDimensions(cursor.getString(cursor.getColumnIndex(ARTICLE_ORIGINAL_DIMENSIONS)));

        if (getSections == true) {

            //Sections
            String selectQuery = "SELECT  * FROM " + TABLE_SECTION + " WHERE " + SECTION_ARTICLE_ID + " = " + article.getId();
            cursor = db.rawQuery(selectQuery, null);

            HashMap<Long, Section> sectionHashMap = new HashMap<Long, Section>();

            if (cursor.moveToFirst()) {
                do {
                    Section section = new Section();

                    article.getSections().add(section);
                    section.setArticle(article);

                    section.setId(cursor.getInt(cursor.getColumnIndex(SECTION_ID)));
                    section.setTitle(cursor.getString(cursor.getColumnIndex(SECTION_TITLE)));
                    section.setLevel(cursor.getInt(cursor.getColumnIndex(SECTION_LEVEL)));

                    sectionHashMap.put(section.getId(), section);

                } while (cursor.moveToNext());
            }

            if (sectionHashMap.isEmpty() == false) {

                String ids = TextUtils.join(",", sectionHashMap.keySet());

                //Section Images
                selectQuery = "SELECT  * FROM " + TABLE_SECTION_IMAGE + " WHERE " + SECTION_IMAGE_SECTION_ID + " IN (" + ids + ")";
                cursor = db.rawQuery(selectQuery, null);

                if (cursor.moveToFirst()) {
                    do {
                        SectionImage sectionImage = new SectionImage();

                        Long sectionId = cursor.getLong(cursor.getColumnIndex(SECTION_IMAGE_SECTION_ID));
                        Section section = sectionHashMap.get(sectionId);

                        section.getSectionImages().add(sectionImage);
                        sectionImage.setSection(section);

                        sectionImage.setId(cursor.getInt(cursor.getColumnIndex(SECTION_IMAGE_ID)));
                        sectionImage.setSrc(cursor.getString(cursor.getColumnIndex(SECTION_IMAGE_SRC)));
                        sectionImage.setCaption(cursor.getString(cursor.getColumnIndex(SECTION_IMAGE_CAPTION)));

                    } while (cursor.moveToNext());
                }

                //Section Content

                selectQuery = "SELECT  * FROM " + TABLE_SECTION_CONTENT + " WHERE " + SECTION_CONTENT_SECTION_ID + " IN (" + ids + ")";
                cursor = db.rawQuery(selectQuery, null);

                HashMap<Long, SectionContent> sectionContentHashMap = new HashMap<Long, SectionContent>();

                if (cursor.moveToFirst()) {
                    do {
                        SectionContent sectionContent = new SectionContent();

                        Long sectionId = cursor.getLong(cursor.getColumnIndex(SECTION_CONTENT_SECTION_ID));
                        Section section = sectionHashMap.get(sectionId);

                        section.getSectionContents().add(sectionContent);
                        sectionContent.setSection(section);

                        sectionContent.setId(cursor.getInt(cursor.getColumnIndex(SECTION_CONTENT_ID)));
                        sectionContent.setType(cursor.getString(cursor.getColumnIndex(SECTION_CONTENT_TYPE)));
                        sectionContent.setText(cursor.getString(cursor.getColumnIndex(SECTION_CONTENT_TEXT)));

                        sectionContentHashMap.put(sectionContent.getId(), sectionContent);

                    } while (cursor.moveToNext());
                }

                if (sectionContentHashMap.isEmpty() == false) {

                    ids = TextUtils.join(",", sectionContentHashMap.keySet());

                    //SectionContents
                    selectQuery = "SELECT  * FROM " + TABLE_LIST_ELEMENT + " WHERE " + LIST_ELEMENT_SECTION_CONTENT_ID + " IN (" + ids + ")";
                    cursor = db.rawQuery(selectQuery, null);

                    HashMap<Long, ListElement> listElementHashMap = new HashMap<Long, ListElement>();

                    if (cursor.moveToFirst()) {
                        do {
                            ListElement listElement = new ListElement();

                            Long sectionContentId = cursor.getLong(cursor.getColumnIndex(LIST_ELEMENT_SECTION_CONTENT_ID));
                            SectionContent sectionContent = sectionContentHashMap.get(sectionContentId);

                            sectionContent.getListElements().add(listElement);
                            listElement.setSectionContent(sectionContent);

                            listElement.setId(cursor.getInt(cursor.getColumnIndex(LIST_ELEMENT_ID)));
                            listElement.setText(cursor.getString(cursor.getColumnIndex(LIST_ELEMENT_TEXT)));

                            listElementHashMap.put(listElement.getId(), listElement);

                        } while (cursor.moveToNext());
                    }

                    if (listElementHashMap.isEmpty()) {
                        listElementHashMap.put(-1L, null);
                    }

                    String listElementIds = TextUtils.join(",", listElementHashMap.keySet());


                    //LinkedArticles
                    selectQuery = "SELECT  * FROM " + TABLE_LINKED_ARTICLE
                            + " WHERE (" + LINKED_ARTICLE_SECTION_CONTENT_ID + " IN (" + ids + ")"
                            + " OR " + LINKED_ARTICLE_LIST_ELEMENT_ID + " IN (" + listElementIds + "))"
                            + " AND " + LINKED_ARTICLE_ALIAS + " IN (SELECT " + ALIAS_TITLE + " FROM " + TABLE_ALIAS + ")";
                    cursor = db.rawQuery(selectQuery, null);

                    if (cursor.moveToFirst()) {
                        do {
                            LinkedArticle linkedArticle = new LinkedArticle();

                            linkedArticle.setId(cursor.getInt(cursor.getColumnIndex(LINKED_ARTICLE_ID)));

                            Long sectionConentId = cursor.getLong(cursor.getColumnIndex(LINKED_ARTICLE_SECTION_CONTENT_ID));
                            Long listElementId = cursor.getLong(cursor.getColumnIndex(LINKED_ARTICLE_LIST_ELEMENT_ID));

                            if (sectionConentId != 0L) {
                                SectionContent sectionContent = sectionContentHashMap.get(sectionConentId);

                                sectionContent.getLinkedArticles().add(linkedArticle);
                                linkedArticle.setSectionContent(sectionContent);

                            } else if (listElementId != 0L) {

                                ListElement listElement = listElementHashMap.get(listElementId);

                                listElement.getLinkedArticles().add(linkedArticle);
                                linkedArticle.setListElement(listElement);

                            }

                            linkedArticle.setAlias(cursor.getString(cursor.getColumnIndex(LINKED_ARTICLE_ALIAS)));


                        } while (cursor.moveToNext());
                    }

                }

            }

        }

        // return character
        return article;
    }
}