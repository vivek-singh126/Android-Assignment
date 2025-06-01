// data/repository/BookRepositoryImpl.java
package com.example.bookreviewapp.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bookreviewapp.data.local.BookDao;
import com.example.bookreviewapp.data.remote.BookApiService;
import com.example.bookreviewapp.data.remote.BookApiResponse;
import com.example.bookreviewapp.domain.model.Book;
import com.example.bookreviewapp.domain.repository.BookRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookRepositoryImpl implements BookRepository {

    private final BookApiService apiService;
    private final BookDao bookDao;
    private final ExecutorService executorService; // For Room operations on background thread

    public BookRepositoryImpl(BookApiService apiService, BookDao bookDao) {
        this.apiService = apiService;
        this.bookDao = bookDao;
        this.executorService = Executors.newSingleThreadExecutor(); // For Room ops
    }

    @Override
    public LiveData<List<Book>> getBooks() {
        MutableLiveData<List<Book>> liveData = new MutableLiveData<>();

        // Try to fetch from API first
        apiService.getBooks().enqueue(new Callback<BookApiResponse>() {
            @Override
            public void onResponse(Call<BookApiResponse> call, Response<BookApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Book> apiBooks = response.body().getBooks();

                    // Merge with favorite status from local DB if applicable
                    // This is a simplified merge. A more robust solution might involve
                    // fetching all favorites first and then merging.
                    executorService.execute(() -> {
                        List<Book> mergedBooks = new ArrayList<>(apiBooks);
                        for (Book apiBook : mergedBooks) {
                            LiveData<Book> favoriteBookLiveData = bookDao.getFavoriteBookByApiId(apiBook.getApiId());
                            // This would ideally be observed or fetched synchronously for initial merge
                            // For simplicity here, we're relying on a background thread operation.
                            // A better approach would be to fetch all favorite IDs first.
                            // For MVP, we might skip this complex merge logic initially for the list screen.
                            // The detail screen handles favorite status specifically.
                        }
                        liveData.postValue(mergedBooks); // Post to main thread
                    });
                } else {
                    // Handle API error or empty response
                    // In offline mode, this would trigger loading from local storage
                    System.out.println("API Error: " + response.code() + " " + response.message());
                    // Fallback to local books if API fails
                    executorService.execute(() -> {
                        liveData.postValue(bookDao.getAllFavoriteBooks().getValue());
                    });
                }
            }

            @Override
            public void onFailure(Call<BookApiResponse> call, Throwable t) {
                // Network error, load from local storage (offline mode)
                System.out.println("Network Error: " + t.getMessage());
                executorService.execute(() -> {
                    liveData.postValue(bookDao.getAllFavoriteBooks().getValue());
                });
            }
        });
        return liveData;
    }

    @Override
    public LiveData<Book> getBookDetails(String apiId) {
        // For book details, we first check if it's a favorite to show its status
        return bookDao.getFavoriteBookByApiId(apiId);
        // If not found in favorites, you'd typically fetch from API and then set details.
        // For this MVP, we assume getBooks already provides enough info,
        // or we need a specific API endpoint for single book details.
        // If the latter, you'd do:
        // apiService.getBook(apiId).enqueue(...) and then update LiveData
    }

    @Override
    public void saveBook(Book book) {
        executorService.execute(() -> {
            book.setFavorite(true); // Mark as favorite before saving
            bookDao.insertBook(book);
        });
    }

    @Override
    public void removeBook(Book book) {
        executorService.execute(() -> {
            book.setFavorite(false); // Unmark as favorite (optional, for UI consistency)
            bookDao.deleteBook(book);
        });
    }

    @Override
    public LiveData<List<Book>> getFavoriteBooks() {
        return bookDao.getAllFavoriteBooks();
    }
}