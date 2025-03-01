package com.unir.books.catalogue.service;

import com.unir.books.catalogue.controller.model.BookDto;
import com.unir.books.catalogue.controller.model.CreateBookRequest;
import com.unir.books.catalogue.data.model.Book;

import java.util.Date;
import java.util.List;

public interface BooksCatalogueService {

    List<Book> getBooks(String title, String author, String category, String isbn,
                        Date publishedDate, Double price, Integer rating, Boolean visible);

    Book getBook(String bookId);

    Boolean removeBook(String bookId);

    Book createBook(CreateBookRequest request);

    Book updateBook(String bookId, String updateRequest);

    Book updateBook(String bookId, BookDto updateRequest);

}
