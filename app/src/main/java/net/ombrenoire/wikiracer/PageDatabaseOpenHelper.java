package net.ombrenoire.wikiracer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PageDatabaseOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "WikiracerDatabase.db";
    private static final int DATABASE_VERSION = 6;
    private static final String PAGES_TABLE_NAME = "pages";
    private static final String ID = "ID";
    private static final String TITLE = "TITLE";
    private static final String CONTENT = "CONTENT";
    private static final String WIKI = "WIKI";
    private static final String PAGES_TABLE_CREATE =
            "CREATE TABLE " + PAGES_TABLE_NAME + " (" +
                    ID + " INTEGER PRIMARY KEY, " +
                    TITLE + " TEXT, " +
                    WIKI + " TEXT, " +
                    CONTENT + " TEXT "+
                     ");";

    PageDatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PAGES_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PAGES_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertPage(String title, String content, String wiki) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Log.v("Database", "Content : " + content);
        contentValues.put(TITLE, title);
        contentValues.put(CONTENT, content);
        contentValues.put(WIKI, wiki);
        db.insert(PAGES_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getPage(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery( "SELECT * FROM " + PAGES_TABLE_NAME + " WHERE " +
                ID + "=?", new String[] { Integer.toString(id) } );
    }

    public Cursor getPage(String title, String wiki) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery( "SELECT * FROM " + PAGES_TABLE_NAME + " WHERE " +
                TITLE + "=?" + "AND " + WIKI + "=?", new String[] { title, wiki } );
    }
}
