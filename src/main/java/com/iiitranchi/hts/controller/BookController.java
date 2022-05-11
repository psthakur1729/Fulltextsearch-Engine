package com.iiitranchi.hts.controller;

import java.util.List;
import java.util.Optional;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.translate.AmazonTranslate;
import com.amazonaws.services.translate.AmazonTranslateClient;
import com.amazonaws.services.translate.model.TranslateTextRequest;
import com.amazonaws.services.translate.model.TranslateTextResult;
import com.iiitranchi.hts.builder.SearchQueryBuilder;
import com.iiitranchi.hts.model.Book;
import com.iiitranchi.hts.repository.BookRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class BookController {
    @Autowired
    SearchQueryBuilder searchQueryBuilder;
    @Autowired
    private BookRepository bookRepository;
    public static final String REGION = "us-east-1";

    @GetMapping(value = "/library")
    public Iterable<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @GetMapping(value = "/library/{isbn}")
    public Optional<Book> getByISBN(@PathVariable String isbn) {
        return bookRepository.findById(isbn);
    }

    @PostMapping(value = "/library")
    public String addBook(@RequestBody Book book) {
        bookRepository.save(book);
        return "Successfully added book to library.";
    }

    @PostMapping(value = "/libraryAddMany")
    public String addManyBooks(@RequestBody List<Book> list) {
        bookRepository.saveAll(list);
        return "Successfully added book to library.";
    }

    @DeleteMapping(value = "/library/{isbn}")
    public String deleteByISBN(@PathVariable String isbn) {
        bookRepository.deleteById(isbn);
        return "Successfully removed book with isbn : " + isbn;
    }

    @GetMapping(value = "/libraryFromName/{bookName}")
    public SearchHits<Book> getOne(@PathVariable String bookName) {
        return searchQueryBuilder.getBookByName(bookName);
    }

    @GetMapping(value = "/textSearch/{text}")
    public SearchHits<Book> getAll(@PathVariable final String text) {

        AWSCredentialsProvider awsCreds = DefaultAWSCredentialsProviderChain.getInstance();

        AmazonTranslate translate = AmazonTranslateClient.builder()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds.getCredentials()))
                .withRegion(REGION)
                .build();

        TranslateTextRequest request = new TranslateTextRequest()
                .withText(text)
                .withSourceLanguageCode("auto")
                .withTargetLanguageCode("hi");
        TranslateTextResult result = translate.translateText(request);
        String s = result.getTranslatedText();
        return searchQueryBuilder.getRelatedBooks(s);
    }

}
