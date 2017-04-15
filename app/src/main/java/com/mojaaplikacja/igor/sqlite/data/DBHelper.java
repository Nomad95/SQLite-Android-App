package com.mojaaplikacja.igor.sqlite.data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.mojaaplikacja.igor.sqlite.data.PhonesContract.*;


/**
 * definicja bazy danych
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PhoneDatabase.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_PHONES_CREATE = "CREATE TABLE " + PhonesEntry.TABLE_NAME +
            " (" + PhonesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PhonesEntry.COLUMN_PRODUCENT + " TEXT, " +
            PhonesEntry.COLUMN_MODEL + " TEXT, " +
            PhonesEntry.COLUMN_ANDR_VERSION + " TEXT, " +
            PhonesEntry.COLUMN_WWW + " TEXT " + ") ";

    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_PHONES_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXTSTS " + PhonesEntry.TABLE_NAME);
        onCreate(db);
    }
}
