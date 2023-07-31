package com.example.lab2.provider;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import kotlin.jvm.Transient;

@Entity(tableName = "books")
public class bookItem{
    static final String BOOK_TABLE = "books";

    @PrimaryKey(autoGenerate = false)
    @NonNull
    @ColumnInfo(name = "bookID")
    private String bookID;

    @ColumnInfo(name = "bookTitle")
    private String bookTitle;

    @ColumnInfo(name = "bookISBN")
    private String bookISBN;

    @ColumnInfo(name = "bookAuthor")
    private String bookAuthor;

    @ColumnInfo(name = "bookDesc")
    private String bookDesc;

    @ColumnInfo(name = "bookPrice")
    private String bookPrice;

    public bookItem(String bookID, String bookTitle, String bookISBN, String bookAuthor, String bookDesc, String bookPrice) {

        this.bookID = bookID;
        this.bookTitle = bookTitle;
        this.bookISBN = bookISBN;
        this.bookAuthor = bookAuthor;
        this.bookDesc = bookDesc;
        this.bookPrice = bookPrice;
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(@NonNull String bookID) {
        this.bookID = bookID;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookISBN() {
        return bookISBN;
    }

    public void setBookISBN(String bookISBN) {
        this.bookISBN = bookISBN;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookDesc() {
        return bookDesc;
    }

    public void setBookDesc(String bookDesc) {
        this.bookDesc = bookDesc;
    }

    public String getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(String bookPrice) {
        this.bookPrice = bookPrice;
    }
}
