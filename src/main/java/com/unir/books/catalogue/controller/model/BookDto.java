package com.unir.books.catalogue.controller.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BookDto {

    private String title;
    private String author;
    private Date published;
    private String category;
    private String isbn;
    private Integer rating;
    private Double price;
    private Integer stock;
    private Boolean visible;

}
