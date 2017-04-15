package com.mojaaplikacja.igor.sqlite.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mojaaplikacja.igor.sqlite.data.PhonesContract.PhonesEntry;

import static com.mojaaplikacja.igor.sqlite.data.PhonesContract.CONTENT_AUTHORITY;
import static com.mojaaplikacja.igor.sqlite.data.PhonesContract.PATH_PHONES;

public class PhonesProvider extends ContentProvider {

    //stałe potrzebne do operacji
    private static final int PHONES = 1;
    private static final int PHONES_ID = 2;

    //uriMatcher
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_PHONES,PHONES);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_PHONES + "/#",PHONES_ID);
    }

    private DBHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return false;
    }

    /**
     * wybieramy wszystkie telefony albo pojedynczy
     * @param uri uri do zmatchowania
     * @param projection kolumny
     * @param selection where
     * @param selectionArgs where argumenty
     * @param sortOrder sortowanie
     * @return cursor z wybranymi danymi
     */
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase readableDatabase = dbHelper.getReadableDatabase();
        Cursor cursor;
        int match = uriMatcher.match(uri);

        switch (match){
            case PHONES:
                cursor = readableDatabase.query(
                        PhonesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case PHONES_ID:
                selection = PhonesEntry._ID+"=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = readableDatabase.query(
                        PhonesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    /**
     * Dodawanie nowego telefonu
     * @param uri uri do identyfikacji
     * @param values ContentValues jakie przekazujemy
     * @return
     */
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int match = uriMatcher.match(uri);
        switch (match){
            case PHONES:
                return insertRecord(uri,values,PhonesEntry.TABLE_NAME);
            default:
                throw new IllegalArgumentException("Insert: Unknown URI: "+ uri);
        }
    }

    private Uri insertRecord(Uri uri, ContentValues values, String table){
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        long rowID = writableDatabase.insert(table, null, values);
        if(rowID == -1){
            Log.e("PhonesProvider","Error insert "+uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri,rowID);
    }

    /**
     * Usuwanie danych lub całej tabeli
     * @param uri to indetyfikacji
     * @param selection where
     * @param selectionArgs where arg
     * @return ilosc usunietych wierszy
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int match = uriMatcher.match(uri);
        switch (match){
            case PHONES:
                return deleteRecord(uri,selection,selectionArgs,PhonesEntry.TABLE_NAME);
            case PHONES_ID:
                selection = PhonesEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return deleteRecord(uri,selection,selectionArgs,PhonesEntry.TABLE_NAME);
            default:
                throw new IllegalArgumentException("Delete unknown URI "+uri);
        }
    }

    private int deleteRecord(Uri uri, String selection, String[] selectionArgs, String tableName){
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        int rowID = writableDatabase.delete(tableName, selection, selectionArgs);
        if(rowID == 0){
            Log.e("Error","Delete error for URI "+uri);
            return -1;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowID;
    }

    /**
     * Modyfikacja wiersza
     * @param uri do ident.
     * @param values wartosci ContentValue
     * @param selection where
     * @param selectionArgs where arg.
     * @return ilosc updateowanych wierszy
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int match = uriMatcher.match(uri);
        switch (match){
            case PHONES:
                return updateRecord(uri,values,selection,selectionArgs,PhonesEntry.TABLE_NAME);
            case PHONES_ID:
                selection = PhonesEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updateRecord(uri,values,selection,selectionArgs,PhonesEntry.TABLE_NAME);
            default:
                throw new IllegalArgumentException("Update unknown URI "+uri);
        }
    }

    private int updateRecord(Uri uri, ContentValues values,
                             String selection, String[] selectionArgs,String tableName){
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        int rowID = writableDatabase.update(tableName, values, selection, selectionArgs);
        if(rowID == 0){
            Log.e("Error","Update error for URI "+uri);
            return -1;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowID;
    }
}
