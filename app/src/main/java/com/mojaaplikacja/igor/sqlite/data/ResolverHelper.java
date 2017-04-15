package com.mojaaplikacja.igor.sqlite.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/**
 * Zawiera metody pracujące na bazie danych
 * Dodawanie usuwanie modyfikacja telefonow itp
 */
public final class ResolverHelper {
    private static final int ALL_RECORDS = -1;

    /**
     * Tworzy nowy model telefonu
     * @param values wartosci nowe
     * @param contentResolver podajemy contentResolver activity
     */
    public static void createPhoneModel(ContentValues values, ContentResolver contentResolver){
        PhonesQueryHandler handler = new PhonesQueryHandler(
                contentResolver
        );
        handler.startInsert(1,null, PhonesContract.PhonesEntry.CONTENT_URI,values);
        Log.i("INSERTED","Phone");
    }

    /**
     * Pobiera całą tabele
     * @param contentResolver podajemy contentResolver activity
     * @return zwraca cursor zawierajacy dane wyciagniete z bazy
     */
    public static Cursor getData(ContentResolver contentResolver){
        String[] projecion = {
                PhonesContract.PhonesEntry.TABLE_NAME + "." + PhonesContract.PhonesEntry._ID,
                PhonesContract.PhonesEntry.COLUMN_PRODUCENT,
                PhonesContract.PhonesEntry.COLUMN_MODEL,
                PhonesContract.PhonesEntry.COLUMN_ANDR_VERSION,
                PhonesContract.PhonesEntry.COLUMN_WWW
        };
        Cursor cursor = contentResolver
                .query(PhonesContract.PhonesEntry.CONTENT_URI,projecion,null,null,null);
        Log.i("SELECTED RECORD COUNT",""+cursor.getCount());
        return cursor;
    }

    /**
     * Pobiera jeden wiersz
     * @param id podajemy id wybieranego elementu
     * @param contentResolver podajemy contentResolver activity
     * @return Cursor zawierajacy dany element
     */
    public static Cursor getData(int id,ContentResolver contentResolver){
        String[] projecion = {
                PhonesContract.PhonesEntry.TABLE_NAME + "." + PhonesContract.PhonesEntry._ID,
                PhonesContract.PhonesEntry.COLUMN_PRODUCENT,
                PhonesContract.PhonesEntry.COLUMN_MODEL,
                PhonesContract.PhonesEntry.COLUMN_ANDR_VERSION,
                PhonesContract.PhonesEntry.COLUMN_WWW
        };
        Cursor cursor = contentResolver
                .query(Uri.withAppendedPath(PhonesContract.PhonesEntry.CONTENT_URI,String.valueOf(id)),projecion,null,null,null);
        Log.i("SELECTED RECORD COUNT",""+cursor.getCount());
        return cursor;
    }

    /**
     * Wyswietla dane na konsoli
     * @param cursor - dane
     */
    public static void printData(Cursor cursor){
        String rowContent="";
        while(cursor.moveToNext()){
            for(int i = 0; i<5;i++){
                rowContent += cursor.getString(i) + " - ";
            }
            Log.i("Row "+String.valueOf(cursor.getPosition()),rowContent);
            rowContent="";
        }
        cursor.close();
    }

    /**
     * modyfikacja rekordu
     * @param id id modyfikowanego elementu
     * @param contentResolver podajemy contentResolver activity
     */
    public static void updateData(int id,ContentResolver contentResolver,ContentValues values){
        //test
        String[] args = {String.valueOf(id)};
        int rows = contentResolver
                .update(PhonesContract.PhonesEntry.CONTENT_URI,values, PhonesContract.PhonesEntry._ID + "=?",args);
        Log.i("HEY!","Updated rows count: " + String.valueOf(rows));
    }

    /**
     * usuwa rekord
     * @param id id rekordu, podaj ALL_RECORDS aby usunac wszstko
     * @param contentResolver podajemy contentResolver activity
     */
    public static void deleteData(int id,ContentResolver contentResolver){
        String[] args = {String.valueOf(id)};
        Log.i("DELETEID",""+id);
        if( id == ALL_RECORDS)
            args=null;
        int rows = contentResolver
                .delete(PhonesContract.PhonesEntry.CONTENT_URI, PhonesContract.PhonesEntry._ID + "=?",args);
        Log.i("HEY!","Deleted rows count: " + String.valueOf(rows));
    }
}
