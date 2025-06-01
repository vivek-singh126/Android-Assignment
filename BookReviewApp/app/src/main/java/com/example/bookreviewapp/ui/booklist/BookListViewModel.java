// ui/booklist/BookListViewModel.java
package com.example.bookreviewapp.ui.booklist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookreviewapp.domain.model.Book;
import com.example.bookreviewapp.domain.repository.BookRepository;

import java.util.List;

public class BookListViewModel extends ViewModel {
    private final BookRepository bookRepository;
    private LiveData<List<Book>> books;
    private LiveData<List<Book>> favoriteBooks; // To check favorite status for UI

    public BookListViewModel(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
        this.books = bookRepository.getBooks(); // Fetches from API/Local
        this.favoriteBooks = bookRepository.getFavoriteBooks(); // For merging favorite status
    }

    public LiveData<List<Book>> getBooks() {
        return books;
    }

    public LiveData<List<Book>> getFavoriteBooks() {
        return favoriteBooks;
    }

    // You can add methods to refresh the list if needed
    public void refreshBooks() {
        this.books = bookRepository.getBooks(); // Re-fetch
    }
}