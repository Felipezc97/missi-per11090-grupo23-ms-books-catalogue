package com.unir.books.catalogue.data;

import com.unir.books.catalogue.data.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.List;

interface BookJpaRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    List<Book> findByTitle(String title);

    List<Book> findByAuthor(String author);

    List<Book> findByPublished(Date date);

    List<Book> findByCategory(String category);

    List<Book> findByIsbn(String isbn);

    List<Book> findByRating(Integer rating);

    List<Book> findByPrice(Double price);

}
