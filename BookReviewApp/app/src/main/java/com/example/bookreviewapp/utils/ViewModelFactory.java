// utils/ViewModelFactory.java
package com.example.bookreviewapp.utils;

import android.app.Application;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.bookreviewapp.data.local.AppDatabase;
import com.example.bookreviewapp.data.remote.RetrofitClient;
import com.example.bookreviewapp.data.repository.BookRepositoryImpl;
import com.example.bookreviewapp.domain.repository.BookRepository;
import com.example.bookreviewapp.ui.bookdetail.BookDetailViewModel;
import com.example.bookreviewapp.ui.booklist.BookListViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private final Application application; // Needed for Room context
    private final BookRepository bookRepository;

    public ViewModelFactory(Application application) {
        this.application = application;
        // Initialize dependencies here once
        this.bookRepository = new BookRepositoryImpl(
                RetrofitClient.getClient().create(com.example.bookreviewapp.data.remote.BookApiService.class),
                AppDatabase.getDatabase(application.getApplicationContext()).bookDao()
        );
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(BookListViewModel.class)) {
            return (T) new BookListViewModel(bookRepository);
        } else if (modelClass.isAssignableFrom(BookDetailViewModel.class)) {
            return (T) new BookDetailViewModel(bookRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}