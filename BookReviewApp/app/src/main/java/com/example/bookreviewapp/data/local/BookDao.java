// data/local/BookDao.java
package com.example.bookreviewapp.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bookreviewapp.domain.model.Book;

import java.util.List;

@Dao
public interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertBook(Book book); // Returns the rowId of the inserted book

    @Update
    void updateBook(Book book);

    @Delete
    void deleteBook(Book book);

    @Query("SELECT * FROM books WHERE apiId = :apiId")
    LiveData<Book> getFavoriteBookByApiId(String apiId); // For detail screen

    @Query("SELECT * FROM books")
    LiveData<List<Book>> getAllFavoriteBooks(); // For offline list
}