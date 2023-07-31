package com.example.lab2.provider;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.util.List;

public class BookRepositary {

    private BookDao myBookDao;
    private LiveData<List<bookItem>> myAllBooks;
    public BookRepositary(Application application) {
        BookDatabase db = BookDatabase.getDatabase(application);
        myBookDao = db.bookDao();
        myAllBooks = myBookDao.getAllBooks();
    }

    public LiveData<List<bookItem>> getAllBooks() {
        return myAllBooks;
    }

    public void insert(bookItem BookItem) {
        BookDatabase.databaseWriteExecutor.execute(() -> myBookDao.addBook(BookItem));
    }

    public void deleteExpensiveBooks() {
        BookDatabase.databaseWriteExecutor.execute(() ->{
            myBookDao.deleteExpensiveBooks();
        });
    }

    public void deleteAll() {
        BookDatabase.databaseWriteExecutor.execute(() ->{
            myBookDao.deleteAllBooks();
        });
    }

    public LiveData<List<bookItem>> getBooksByFilters(SimpleSQLiteQuery filterQuery){
        myAllBooks = myBookDao.getBooksByFilters(filterQuery);
        return myAllBooks;
    }
}
