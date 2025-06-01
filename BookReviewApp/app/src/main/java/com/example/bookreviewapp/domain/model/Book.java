package com.example.bookreviewapp.domain.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName; // Import SerializedName

@Entity(tableName = "books")
public class Book {
    @PrimaryKey(autoGenerate = true)
    @Expose(serialize = false, deserialize = false)
    public int id; // Room ID, separate from API ID if any

    @SerializedName("id") // <--- ADD THIS ANNOTATION
    public String apiId; // ID from the fake API

    public String title;
    public String author;
    public String description;
    public String thumbnailUrl;
    public String imageUrl;
    public double rating;
    public boolean isFavorite; // To mark if saved locally

    // Constructor 1: This is the one Room SHOULD use.
    public Book(int id, String apiId, String title, String author, String description,
                String thumbnailUrl, String imageUrl, double rating, boolean isFavorite) {
        this.id = id;
        this.apiId = apiId;
        this.title = title;
        this.author = author;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.isFavorite = isFavorite;
    }

    // Constructor 2: This one is for creating Book objects from API responses.
    @Ignore
    public Book(String apiId, String title, String author, String description,
                String thumbnailUrl, String imageUrl, double rating) {
        this.apiId = apiId;
        this.title = title;
        this.author = author;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.isFavorite = false; // Default to not favorite
    }

    // Getters and Setters (as you had them)
    // Ensure you have getters and setters for all fields for Gson to work correctly,
    // especially for apiId.
    public int getId() { return id; }
    public String getApiId() { return apiId; } // Make sure this getter exists
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getDescription() { return description; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public String getImageUrl() { return imageUrl; }
    public double getRating() { return rating; }
    public boolean isFavorite() { return isFavorite; }

    public void setFavorite(boolean favorite) { isFavorite = favorite; }
    public void setId(int id) { this.id = id; }
    public void setApiId(String apiId) { this.apiId = apiId; } // Make sure this setter exists
}