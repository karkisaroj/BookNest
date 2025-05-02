package com.booknest.model;

/**
 * AuthorModel represents the structure of the author entity.
 */
public class AuthorModel {
    private int authorId;
    private String authorName;
    // Default constructor
    public AuthorModel() {
    }

    // Parameterized constructor
    public AuthorModel(int authorId, String authorName) {
        this.authorId = authorId;
        this.authorName = authorName;
    }
    // Getters and Setters
    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}