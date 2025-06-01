// domain/repository/BookRepository.java
package com.example.bookreviewapp.domain.repository;

import androidx.lifecycle.LiveData;
import com.example.bookreviewapp.domain.model.Book;
import java.util.List;

public interface BookRepository {
    LiveData<List<Book>> getBooks(); // For book list (from API, with fallback)
    LiveData<Book> getBookDetails(String apiId); // For single book details (check favorites first)
    void saveBook(Book book);    // Save to local (favorite)
    void removeBook(Book book);  // Remove from local (unfavorite)
    LiveData<List<Book>> getFavoriteBooks(); // Get all favorite books for offline list
}