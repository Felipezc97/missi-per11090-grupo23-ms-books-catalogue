package com.unir.products.data;

import com.unir.products.data.model.Book;
import com.unir.products.data.utils.Constants;
import com.unir.products.data.utils.SearchCriteria;
import com.unir.products.data.utils.SearchOperation;
import com.unir.products.data.utils.SearchStatement;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookRepository {

    private final BookJpaRepository repository;

    public List<Book> getBooks() {
        return repository.findAll();
    }

    public Book getBookById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Book save(Book book) {
        return repository.save(book);
    }

    public void delete(Book book) {
        repository.delete(book);
    }

    public List<Book> search(String title, String author, String category, String isbn,
                             Date publishedDate, Double price, Integer rating, Boolean visible) {

        SearchCriteria<Book> spec = new SearchCriteria<>();

        if (StringUtils.isNotBlank(title)) {
            spec.add(new SearchStatement(Constants.TITLE, title, SearchOperation.MATCH));
        }

        if (StringUtils.isNotBlank(author)) {
            spec.add(new SearchStatement(Constants.AUTHOR, author, SearchOperation.MATCH));
        }

        if (StringUtils.isNotBlank(category)) {
            spec.add(new SearchStatement(Constants.CATEGORY, category, SearchOperation.EQUAL));
        }

        if (StringUtils.isNotBlank(isbn)) {
            spec.add(new SearchStatement(Constants.ISBN, isbn, SearchOperation.EQUAL));
        }

        if (publishedDate != null) {
            spec.add(new SearchStatement(Constants.PUBLISHED, publishedDate, SearchOperation.GREATER_THAN_EQUAL));
        }

        if (price != null) {
            spec.add(new SearchStatement(Constants.PRICE, price, SearchOperation.EQUAL));
        }

        if (rating != null) {
            spec.add(new SearchStatement(Constants.RATING, rating, SearchOperation.GREATER_THAN_EQUAL));
        }

        if (visible != null) {
            spec.add(new SearchStatement(Constants.VISIBLE, visible, SearchOperation.EQUAL));
        }

        return repository.findAll(spec);
    }

}
