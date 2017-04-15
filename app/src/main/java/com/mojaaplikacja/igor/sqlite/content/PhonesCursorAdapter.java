package com.mojaaplikacja.igor.sqlite.content;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.mojaaplikacja.igor.sqlite.R;
import com.mojaaplikacja.igor.sqlite.data.PhonesContract;

public class PhonesCursorAdapter extends CursorAdapter{
    public PhonesCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    /**
     * inflatujemy nowy widok jednego wiersza
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(
                R.layout.row_item,parent,false
        );
    }

    /**
     * Wpisujamy dane z cursora do TextVIewów
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView producentTextView = (TextView) view.findViewById(R.id.producentTextView);
        TextView modelTextView = (TextView) view.findViewById(R.id.modelTextView);

        int textColumn = cursor.getColumnIndex(PhonesContract.PhonesEntry.COLUMN_PRODUCENT);
        String text = cursor.getString(textColumn);
        producentTextView.setText(text);

        textColumn = cursor.getColumnIndex(PhonesContract.PhonesEntry.COLUMN_MODEL);
        text = cursor.getString(textColumn);
        modelTextView.setText(text);
    }

}
