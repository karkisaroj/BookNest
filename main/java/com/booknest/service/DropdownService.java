package com.booknest.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.booknest.config.DbConfiguration;
import com.booknest.model.PublisherModel;
import com.booknest.model.AuthorModel;
import com.booknest.model.CategoryModel;

public class DropdownService {
    private Connection dbConn;

    public DropdownService() {
        try {
            dbConn = DbConfiguration.getDbConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public List<PublisherModel> getPublishers() {
        List<PublisherModel> publishers = new ArrayList<>();
        String query = "SELECT publisherID, publisher_name FROM publisher";

        try (PreparedStatement stmt = dbConn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Use the parameterized constructor
                PublisherModel publisher = new PublisherModel(rs.getInt("publisherID"), rs.getString("publisher_name"));
                publishers.add(publisher);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return publishers;
    }

    public List<AuthorModel> getAuthors() {
        List<AuthorModel> authors = new ArrayList<>();
        String query = "SELECT authorID, author_name FROM author";

        try (PreparedStatement stmt = dbConn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Use the parameterized constructor
                AuthorModel author = new AuthorModel(rs.getInt("authorID"), rs.getString("author_name"));
                authors.add(author);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return authors;
    }

    public List<CategoryModel> getCategories() {
        List<CategoryModel> categories = new ArrayList<>();
        String query = "SELECT categoryID, category_name, category_description FROM categories";

        try (PreparedStatement stmt = dbConn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Use the parameterized constructor
                CategoryModel category = new CategoryModel(rs.getInt("categoryID"), rs.getString("category_name"),
                        rs.getString("category_description"));
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }

    public PublisherModel getPublisherById(int publisherID) {
        String query = "SELECT publisherID, publisher_name FROM publisher WHERE publisherID = ?";
        PublisherModel publisher = null;

        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setInt(1, publisherID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Use the parameterized constructor
                    publisher = new PublisherModel(rs.getInt("publisherID"), rs.getString("publisher_name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return publisher;
    }
}