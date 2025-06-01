// ui/bookdetail/BookDetailViewModel.java
package com.example.bookreviewapp.ui.bookdetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookreviewapp.domain.model.Book;
import com.example.bookreviewapp.domain.repository.BookRepository;

public class BookDetailViewModel extends ViewModel {
    private final BookRepository bookRepository;
    private MutableLiveData<Book> selectedBook = new MutableLiveData<>();
    private LiveData<Book> favoriteStatusBook; // To observe if the current book is favorited

    public BookDetailViewModel(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public LiveData<Book> getSelectedBook() {
        return selectedBook;
    }

    public LiveData<Book> getFavoriteStatusBook() {
        return favoriteStatusBook;
    }

    public void setBook(Book book) {
        selectedBook.setValue(book);
        // Observe if this specific book is already a favorite
        if (book != null && book.getApiId() != null) {
            // CORRECTED LINE: Call getBookDetails from the repository
            favoriteStatusBook = bookRepository.getBookDetails(book.getApiId());
        }
    }

    public void toggleFavorite(Book book) {
        if (book == null || book.getApiId() == null) return;

        // Check current favorite status
        if (favoriteStatusBook != null && favoriteStatusBook.getValue() != null) {
            // It's currently favorited, so remove it
            bookRepository.removeBook(book);
        } else {
            // Not favorited, so save it
            bookRepository.saveBook(book);
        }
        // Update the book's favorite status in selectedBook if needed for immediate UI feedback
        // The LiveData from Room will also update `favoriteStatusBook` automatically
    }
}