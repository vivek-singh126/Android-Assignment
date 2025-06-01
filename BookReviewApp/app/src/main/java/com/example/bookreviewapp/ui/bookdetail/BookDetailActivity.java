// ui/bookdetail/BookDetailActivity.java
package com.example.bookreviewapp.ui.bookdetail;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.bookreviewapp.R;
import com.example.bookreviewapp.domain.model.Book;
import com.example.bookreviewapp.utils.ViewModelFactory;
import com.google.gson.Gson; // For parsing JSON string back to Book object

public class BookDetailActivity extends AppCompatActivity {

    private BookDetailViewModel bookDetailViewModel;
    private Book currentBook; // Holds the book object passed from list
    private TextView tvTitle, tvAuthor, tvDescription;
    private RatingBar ratingBar;
    private ImageView ivBookImage; // Placeholder for image
    private Button btnToggleFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        // Initialize views
        tvTitle = findViewById(R.id.tvDetailTitle);
        tvAuthor = findViewById(R.id.tvDetailAuthor);
        tvDescription = findViewById(R.id.tvDetailDescription);
        ratingBar = findViewById(R.id.ratingBar);
        ivBookImage = findViewById(R.id.ivDetailImage);
        btnToggleFavorite = findViewById(R.id.btnToggleFavorite);

        // Initialize ViewModel
        bookDetailViewModel = new ViewModelProvider(this, new ViewModelFactory(getApplication()))
                .get(BookDetailViewModel.class);

        // Get book data from intent
        String bookJson = getIntent().getStringExtra("book_json");
        if (bookJson != null) {
            currentBook = new Gson().fromJson(bookJson, Book.class);
            bookDetailViewModel.setBook(currentBook); // Set the book in ViewModel
            displayBookDetails(currentBook);
        } else {
            Toast.makeText(this, "Book details not available.", Toast.LENGTH_SHORT).show();
            finish(); // Close activity if no book data
            return;
        }

        // Observe favorite status from ViewModel
        bookDetailViewModel.getFavoriteStatusBook().observe(this, new Observer<Book>() {
            @Override
            public void onChanged(Book favoriteBook) {
                // If favoriteBook is not null, it means this book is in favorites
                if (favoriteBook != null) {
                    btnToggleFavorite.setText("Remove from Favorites");
                    currentBook.setFavorite(true); // Update currentBook's favorite status
                } else {
                    btnToggleFavorite.setText("Add to Favorites");
                    currentBook.setFavorite(false); // Update currentBook's favorite status
                }
            }
        });

        btnToggleFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentBook != null) {
                    bookDetailViewModel.toggleFavorite(currentBook);
                    // Toast for immediate feedback (LiveData observer will update button text)
                    if (currentBook.isFavorite()) {
                        Toast.makeText(BookDetailActivity.this, "Book removed from favorites!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(BookDetailActivity.this, "Book added to favorites!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void displayBookDetails(Book book) {
        tvTitle.setText(book.getTitle());
        tvAuthor.setText(book.getAuthor());
        tvDescription.setText(book.getDescription());
        ratingBar.setRating((float) book.getRating());

        // Simulate image loading for full image
        ivBookImage.setImageResource(R.drawable.book_detail_placeholder); // Create this drawable
        // Or handle based on image URL:
        // if (book.getImageUrl() != null && !book.getImageUrl().isEmpty()) {
        //     // You'd have your custom image loading logic here for a real app
        //     // For this MVP, we are not using external image loading libraries.
        //     // So, show placeholder or a generic image.
        // } else {
        //    ivBookImage.setImageResource(R.drawable.book_detail_placeholder);
        // }
    }
}