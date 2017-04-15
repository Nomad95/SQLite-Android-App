package com.mojaaplikacja.igor.sqlite;


import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mojaaplikacja.igor.sqlite.content.PhonesCursorAdapter;
import com.mojaaplikacja.igor.sqlite.data.ResolverHelper;

import static com.mojaaplikacja.igor.sqlite.data.PhonesContract.*;

public class MainListaActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int URL_LOADER = 0;
    private Cursor cursor;
    private PhonesCursorAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lista);
        getSupportLoaderManager().initLoader(URL_LOADER,null,this);

/*//        //insert
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PhonesEntry.COLUMN_PRODUCENT,"Samsung");
        long insert_id = writableDatabase.insert(PhonesEntry.TABLE_NAME, null, contentValues);

        //select
        SQLite
        Database readableDatabase = dbHelper.getReadableDatabase();

        createPhoneModel();

        //update
//        int id = 2;
//        String[] args = {String.valueOf(id)};
//        ContentValues contentValues2 = new ContentValues();
//        contentValues2.put(PhonesEntry.COLUMN_PRODUCENT,"NIEWAZNE");
//        int update = readableDatabase.update(PhonesEntry.TABLE_NAME, contentValues2, PhonesEntry._ID + " =?", args);
//        Log.i("HEY!","Updated rows count: " + String.valueOf(update));

        //select2
        String[] projection ={
                PhonesEntry.COLUMN_PRODUCENT
        };
        Cursor cursor = readableDatabase.query(PhonesEntry.TABLE_NAME,
                projection, null, null, null, null, null);
        int count = cursor.getCount();
        Log.i("Record count",String.valueOf(count));
        String rowValue ="";
        while(cursor.moveToNext()){
            rowValue = cursor.getString(0);
            Log.i("Row " + String.valueOf(cursor.getPosition()),rowValue);
        }
        cursor.close();

        dbHelper.close();*/

       // createPhoneModel("Samsung","S8","7.0","http://cos.pl");
       // createPhoneModel("LG","K8LTE","6.0","http://cos2.pl");
//        Cursor cursor = getData();
//        printData(cursor);
//        updateData(2);
//        printData(getData());
//        deleteData(1);
//        printData(getData());
//
//        createPhoneModel("Huwawei","L605","6.0","hoole.pl");
//        createPhoneModel("Huwawei","L505","5.0","hoole.pl");
//        printData(getData());
//        deleteData(22);
//        printData(getData());
        initView();
    }

    /**
     * inicjalizujemy widoki,
     * ustawiamy adapter do listView,
     * inicjalizujemy textListenery
     */
    private void initView() {
        listView = (ListView)findViewById(R.id.phonesListView);
        listView.setEmptyView(findViewById(R.id.emptyElement));
        adapter = new PhonesCursorAdapter(this,cursor,false);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater menuInflater = mode.getMenuInflater();
                menuInflater.inflate(R.menu.list_menu,menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()){
                    case R.id.deletePhones:
                        deletePhones();
                        return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Pobierz dane z bazy na temat wybranego obiektu i przekazuje do
                //kolejnej aktywnosci

                //pobieramy dane wybranego telefonu
                Cursor cursor = ResolverHelper.getData((int) id, MainListaActivity.this.getContentResolver());
                Bundle bundle = new Bundle();
                while(cursor.moveToNext()){
                    bundle.putString(Constants._ID,cursor.getString(0));
                    bundle.putString(Constants.PRODUCENT,cursor.getString(1));
                    bundle.putString(Constants.MODEL_NAME,cursor.getString(2));
                    bundle.putString(Constants.ANDR_VER,cursor.getString(3));
                    bundle.putString(Constants.WWW,cursor.getString(4));
                }
                cursor.close();
                Intent intent = new Intent(MainListaActivity.this,PhoneActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    /**
     * usuwa wybrane pozycje (menu glowne)
     */
    private void deletePhones() {
        //pobieramy id wybranych pozycji i w petli je usuwamy
        long[] checkedItemIds = listView.getCheckedItemIds();
        for (long id:checkedItemIds) {
            ResolverHelper.deleteData((int)id,this.getContentResolver());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //tworzymy menu
        getMenuInflater().inflate(R.menu.activity_main_lista,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.addPhone:
                startActivity(new Intent(this,PhoneActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {PhonesEntry.COLUMN_PRODUCENT,
                PhonesEntry.TABLE_NAME + "." + PhonesEntry._ID,
                PhonesEntry.COLUMN_MODEL,
                PhonesEntry.COLUMN_ANDR_VERSION,
                PhonesEntry.COLUMN_WWW
        };
        return new CursorLoader(
                this,
                PhonesEntry.CONTENT_URI,
                projection,
                null,null,null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
