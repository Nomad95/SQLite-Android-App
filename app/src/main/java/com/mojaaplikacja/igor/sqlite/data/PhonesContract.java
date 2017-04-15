package com.mojaaplikacja.igor.sqlite.data;


import android.net.Uri;
import android.provider.BaseColumns;

public final class PhonesContract {
    public static final String CONTENT_AUTHORITY = "com.mojaaplikacja.igor.sqlite.phonesprovider";
    public static final String PATH_PHONES = "phones";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

    public final static class PhonesEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_PHONES);
        // Table name
        public static final String TABLE_NAME = "phones";
        //column (field) names
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PRODUCENT = "producent";
        public static final String COLUMN_MODEL = "model";
        public static final String COLUMN_ANDR_VERSION = "andr_version";
        public static final String COLUMN_WWW = "www";
    }


}

