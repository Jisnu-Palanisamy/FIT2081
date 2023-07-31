package com.example.lab2.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class BookContentProvider extends ContentProvider {

    private BookDatabase db;
    public static final String CONTENT_AUTHORITY = "fit2081.app.PJISNU";
    public static final Uri CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public BookContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deletionCount;
        deletionCount = db
                .getOpenHelper()
                .getWritableDatabase()
                .delete(bookItem.BOOK_TABLE, selection, selectionArgs);
        return deletionCount;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowId = db
                .getOpenHelper()
                .getWritableDatabase()
                .insert(bookItem.BOOK_TABLE, 0, values);
        return ContentUris.withAppendedId(CONTENT_URI, rowId);
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        db = BookDatabase.getDatabase(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(bookItem.BOOK_TABLE);
        String query = builder.buildQuery(projection, selection, null, null, sortOrder, null);
        final Cursor cursor = db
                .getOpenHelper()
                .getReadableDatabase()
                .query(query);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int updateCount;
        updateCount = db
                .getOpenHelper()
                .getWritableDatabase()
                .update(bookItem.BOOK_TABLE, 0, values, selection, selectionArgs);
        return updateCount;
    }
}