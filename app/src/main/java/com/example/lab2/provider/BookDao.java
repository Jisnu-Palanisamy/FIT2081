package com.example.lab2.provider;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.util.List;

@Dao
public interface BookDao {

    @Query("select * from books")
    LiveData<List<bookItem>> getAllBooks();

    @Query("delete from books where CAST(bookPrice as INT) > 50")
    void deleteExpensiveBooks();

    @Insert
    void addBook(bookItem BookItem);

    @Query("delete from books")
    void deleteAllBooks();

    @RawQuery(observedEntities = bookItem.class)
    LiveData<List<bookItem>> getBooksByFilters(SimpleSQLiteQuery filterQuery);
}
