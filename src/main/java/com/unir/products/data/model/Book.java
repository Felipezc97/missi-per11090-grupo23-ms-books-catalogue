package com.unir.products.data.model;

import com.unir.products.controller.model.BookDto;
import com.unir.products.data.utils.Constants;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "books")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = Constants.TITLE, unique = true)
    private String title;

    @Column(name = Constants.AUTHOR)
    private String author;

    @Column(name = Constants.PUBLISHED)
    private Date published;

    @Column(name = Constants.CATEGORY)
    private String category;

    @Column(name = Constants.ISBN)
    private String isbn;

    @Column(name = Constants.RATING)
    private Integer rating;

    @Column(name = Constants.VISIBLE)
    private Boolean visible;

    @Column(name = Constants.PRICE)
    private Double price;

    @Column(name = Constants.STOCK)
    private Integer stock;


    public void update(BookDto bookDto) {
        this.title = bookDto.getTitle();
        this.author = bookDto.getAuthor();
        this.published = bookDto.getPublished();
        this.category = bookDto.getCategory();
        this.isbn = bookDto.getIsbn();
        this.rating = bookDto.getRating();
        this.price = bookDto.getPrice();
        this.stock = bookDto.getStock();
        this.visible = bookDto.getVisible();
    }

}
