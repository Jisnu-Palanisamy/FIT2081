package com.example.lab2.provider;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.util.ArrayList;
import java.util.List;

public class BookViewModel extends AndroidViewModel {

    private BookRepositary myRepositary;
    //private LiveData<List<bookItem>> myAllBooks;
    private MediatorLiveData<List<bookItem>> myAllBooks;

    public BookViewModel(@NonNull Application application) {
        super(application);
        myRepositary = new BookRepositary(application);
        //myAllBooks = myRepositary.getAllBooks();
        myAllBooks = new MediatorLiveData<>();
        myAllBooks.addSource(myRepositary.getAllBooks(), bookResponse ->{
            myAllBooks.setValue(bookResponse);
        });
    }

    public LiveData<List<bookItem>> getAllBooks() {
        return myAllBooks;
    }

    public void addBook(bookItem BookItem) {
        myRepositary.insert(BookItem);
    }

    public void deleteExpensiveBooks() {
        myRepositary.deleteExpensiveBooks();
    }

    public void deleteAll() {
        myRepositary.deleteAll();
    }

    public void getBooksByFilter(String title) {
        String query = "";
        List<Object> queryArgs = new ArrayList<>();
        boolean containsConditions = false;
        query += "SELECT * FROM books";

        if (!title.equals("")) {
            query += " WHERE bookTitle = ?";
            queryArgs.add(title);
            containsConditions = true;
        }
        SimpleSQLiteQuery sqlQuery = new SimpleSQLiteQuery(query, queryArgs.toArray());
        myAllBooks.addSource(myRepositary.getBooksByFilters(sqlQuery), bookResponse -> {
            myAllBooks.setValue(bookResponse);
        });
    }
}
