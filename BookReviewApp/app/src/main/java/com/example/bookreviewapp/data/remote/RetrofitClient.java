// data/remote/RetrofitClient.java
package com.example.bookreviewapp.data.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;
    // IMPORTANT: Replace with your actual base URL where books.json is hosted
   // private static final String BASE_URL = "http://10.0.2.2:8080/"; // For AVD localhost
    private static final String BASE_URL = "http://10.0.2.2:3000/"; //r AVD localhost

    public static Retrofit getClient() {
        if (retrofit == null) {
            // Create a Gson instance that will respect the @Expose annotation
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation() // <--- ADD THIS LINE
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson)) // <--- USE THE CUSTOM GSON INSTANCE
                    .build();
        }
        return retrofit;
    }
}