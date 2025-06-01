// ui/booklist/BookListActivity.java
package com.example.bookreviewapp.ui.booklist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookreviewapp.R; // Make sure R file is correctly imported
import com.example.bookreviewapp.domain.model.Book;
import com.example.bookreviewapp.ui.bookdetail.BookDetailActivity;
import com.example.bookreviewapp.utils.ViewModelFactory;
import com.google.gson.Gson; // For passing Book object as JSON string

import java.util.ArrayList;
import java.util.List;

public class BookListActivity extends AppCompatActivity implements BookAdapter.OnBookClickListener {

    private BookListViewModel bookListViewModel;
    private BookAdapter bookAdapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        recyclerView = findViewById(R.id.recyclerViewBooks);
        progressBar = findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookAdapter = new BookAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(bookAdapter);

        // Initialize ViewModel with factory
        bookListViewModel = new ViewModelProvider(this, new ViewModelFactory(getApplication()))
                .get(BookListViewModel.class);

        observeBooks();
    }

    private void observeBooks() {
        progressBar.setVisibility(View.VISIBLE); // Show loading indicator
        bookListViewModel.getBooks().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                progressBar.setVisibility(View.GONE); // Hide loading indicator
                if (books != null && !books.isEmpty()) {
                    // Update the adapter with the fetched books
                    bookAdapter.setBooks(books);
                    // Optionally, observe favoriteBooks to update UI for saved status
                    // This is more complex and might be better handled in adapter or detail view.
                    // For MVP, just showing list is enough.
                } else {
                    Toast.makeText(BookListActivity.this, "No books found or network error. Showing saved books if any.", Toast.LENGTH_LONG).show();
                    // If no API books, try to show favorite books directly
                    bookListViewModel.getFavoriteBooks().observe(BookListActivity.this, new Observer<List<Book>>() {
                        @Override
                        public void onChanged(List<Book> favoriteBooks) {
                            if (favoriteBooks != null && !favoriteBooks.isEmpty()) {
                                bookAdapter.setBooks(favoriteBooks);
                            } else {
                                Toast.makeText(BookListActivity.this, "No saved books found.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBookClick(Book book) {
        // Handle click, navigate to BookDetailActivity
        Intent intent = new Intent(BookListActivity.this, BookDetailActivity.class);
        // Pass the entire Book object (serialize to JSON string)
        intent.putExtra("book_json", new Gson().toJson(book));
        startActivity(intent);
    }
}