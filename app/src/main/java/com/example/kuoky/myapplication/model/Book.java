package com.example.kuoky.myapplication.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class Book extends GenericJson {

    @Key
    private String name;

    @Key
    private String imageId;

    @Key
    private Author author;

    public Book(){}

    public Book(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
