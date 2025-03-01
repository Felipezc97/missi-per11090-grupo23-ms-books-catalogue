package com.unir.books.catalogue.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.unir.books.catalogue.controller.model.BookDto;
import com.unir.books.catalogue.controller.model.CreateBookRequest;
import com.unir.books.catalogue.data.BookRepository;
import com.unir.books.catalogue.data.model.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class BooksCatalogueServiceImpl implements BooksCatalogueService {

    @Autowired
    private BookRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<Book> getBooks(String title, String author, String category, String isbn,
                               Date publishedDate, Double price, Integer rating, Boolean visible) {

        if (StringUtils.hasLength(title) || StringUtils.hasLength(author) || StringUtils.hasLength(category)
                || StringUtils.hasLength(isbn) || publishedDate == null || price == null || rating == null
                || visible != null) {
            return repository.search(title, author, category, isbn, publishedDate, price, rating, visible);
        }

        List<Book> books = repository.getBooks();
        return books.isEmpty() ? null : books;
    }

    @Override
    public Book getBook(String bookId) {
        return repository.getBookById(Long.valueOf(bookId));
    }

    @Override
    public Boolean removeBook(String bookId) {

        Book book = repository.getBookById(Long.valueOf(bookId));

        if (book != null) {
            repository.delete(book);
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    @Override
    public Book createBook(CreateBookRequest request) {

        if (request != null && StringUtils.hasLength(request.getTitle().trim())
                && StringUtils.hasLength(request.getAuthor().trim())
                && StringUtils.hasLength(request.getCategory().trim())
                && StringUtils.hasLength(request.getIsbn().trim())
                && request.getPublished() != null
                && request.getPrice() != null
                && request.getRating() != null
                && request.getStock() != null
                && request.getVisible() != null) {

            Book book = Book.builder().title(request.getTitle())
                    .author(request.getAuthor())
                    .category(request.getCategory())
                    .isbn(request.getIsbn())
                    .published(request.getPublished())
                    .price(request.getPrice())
                    .rating(request.getRating())
                    .stock(request.getStock())
                    .visible(request.getVisible()).build();

            return repository.save(book);
        } else {
            return null;
        }
    }

    @Override
    public Book updateBook(String bookId, String request) {

        Book book = repository.getBookById(Long.valueOf(bookId));
        if (book != null) {
            try {
                JsonMergePatch jsonMergePatch = JsonMergePatch.fromJson(objectMapper.readTree(request));
                JsonNode target = jsonMergePatch.apply(objectMapper.readTree(objectMapper.writeValueAsString(book)));
                Book patched = objectMapper.treeToValue(target, Book.class);
                repository.save(patched);
                return patched;
            } catch (JsonProcessingException | JsonPatchException e) {
                log.error("Error updating book {}", bookId, e);
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public Book updateBook(String bookId, BookDto updateRequest) {
        Book book = repository.getBookById(Long.valueOf(bookId));
        if (book != null) {
            book.update(updateRequest);
            repository.save(book);
            return book;
        } else {
            return null;
        }
    }

}
