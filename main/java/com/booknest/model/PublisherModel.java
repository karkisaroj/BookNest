package com.booknest.model;

public class PublisherModel {
    private int publisherID; // Property name as expected by EL
    private String publisherName;

    // Parameterized Constructor
    public PublisherModel(int publisherID, String publisherName) {
        this.publisherID = publisherID;
        this.publisherName = publisherName;
    }

    // Default Constructor
    public PublisherModel() {
    }

    // Getter for publisherID
    public int getPublisherID() {
        return publisherID;
    }

    // Setter for publisherID
    public void setPublisherID(int publisherID) {
        this.publisherID = publisherID;
    }

    // Getter for publisherName
    public String getPublisherName() {
        return publisherName;
    }

    // Setter for publisherName
    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    @Override
    public String toString() {
        return "PublisherModel{" +
                "publisherID=" + publisherID +
                ", publisherName='" + publisherName + '\'' +
                '}';
    }
}