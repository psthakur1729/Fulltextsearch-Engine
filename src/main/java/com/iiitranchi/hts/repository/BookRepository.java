package com.iiitranchi.hts.repository;

import com.iiitranchi.hts.model.Book;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BookRepository extends ElasticsearchRepository<Book, String> {
}
