package com.unir.products.service;

import com.unir.products.controller.model.BookDto;
import com.unir.products.controller.model.CreateBookRequest;
import com.unir.products.data.model.Book;

import java.util.Date;
import java.util.List;

public interface BooksCatalogueService {

    List<Book> getBooks(String title, String author, String category, String isbn,
                        Date publishedDate, Double price, Integer rating, Boolean visible);

    Book getBook(String productId);

    Boolean removeBook(String productId);

    Book createBook(CreateBookRequest request);

    Book updateBook(String productId, String updateRequest);

    Book updateBook(String productId, BookDto updateRequest);

}
