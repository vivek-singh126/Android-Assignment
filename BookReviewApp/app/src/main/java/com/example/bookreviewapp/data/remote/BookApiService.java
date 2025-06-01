// data/remote/BookApiService.java
package com.example.bookreviewapp.data.remote;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BookApiService {
    @GET("books") // Assuming books.json is hosted at your base URL
    Call<BookApiResponse> getBooks(); // If the response is wrapped
    // Or if direct list: Call<List<Book>> getBooks();
}