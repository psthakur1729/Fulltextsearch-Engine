package com.iiitranchi.hts.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "bookstore", shards = 2)
public class Book {
    @Id
    private String isbn;
    private String name;
    private String content;

    public Book(String isbn, String name, String content) {
        this.isbn = isbn;
        this.name = name;
        this.content = content;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Book() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
