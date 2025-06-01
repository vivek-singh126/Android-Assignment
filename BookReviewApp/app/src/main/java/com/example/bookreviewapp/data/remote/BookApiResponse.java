// data/remote/BookApiResponse.java (If your API returns a wrapper object)
package com.example.bookreviewapp.data.remote;

import com.example.bookreviewapp.domain.model.Book;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class BookApiResponse {
    @SerializedName("books")
    private List<Book> books;

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}