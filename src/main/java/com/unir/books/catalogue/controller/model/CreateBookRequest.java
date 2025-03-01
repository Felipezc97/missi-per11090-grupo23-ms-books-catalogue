package com.unir.books.catalogue.controller.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookRequest {

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
