package com.unir.books.catalogue.controller;

import com.unir.books.catalogue.controller.model.BookDto;
import com.unir.books.catalogue.controller.model.CreateBookRequest;
import com.unir.books.catalogue.data.model.Book;
import com.unir.books.catalogue.service.BooksCatalogueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Books Controller", description = "Microservicio encargado de exponer operaciones CRUD sobre el catalogo de libros.")
public class BooksCatalogueController {

    private final BooksCatalogueService service;

    @GetMapping("/books")
    @Operation(
            operationId = "Obtener lista de libros",
            description = "Operacion de lectura",
            summary = "Se devuelve una lista de todos los libros almacenados en la base de datos.")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
    public ResponseEntity<List<Book>> getBooks(
            @RequestHeader Map<String, String> headers,
            @Parameter(name = "title", description = "Titulo del libro. No debe ser exacto", example = "Harry Potter", required = false)
            @RequestParam(required = false) String title,
            @Parameter(name = "author", description = "Autor del libro. No debe ser exacto", example = "J. K. Rowling", required = false)
            @RequestParam(required = false) String author,
            @Parameter(name = "category", description = "Categoria en la que se clasifica el libro. Debe ser exacto para mostrar coincidencias", example = "Ciencia Ficcion", required = false)
            @RequestParam(required = false) String category,
            @Parameter(name = "isbn", description = "Codigo Internacional Normalizado para libros. Debe ser exacto para mostrar coincidencias", example = "978-84-08-18123-4", required = false)
            @RequestParam(required = false) String isbn,
            @Parameter(name = "publishedDate", description = "Fecha de publicacion del libro. Muestra los libros con fecha de publicacion mayor o actual a la fecha indicada", example = "Harry Potter", required = false)
            @RequestParam(required = false) Date publishedDate,
            @Parameter(name = "price", description = "Precio de venta del libro. Debe ser el valor exacto", example = "25.50", required = false)
            @RequestParam(required = false) Double price,
            @Parameter(name = "rating", description = "Valoracion del libro en una escala de 1 a 5. Muestra los libros cuya valoracion es igual o mayor al valor indicado", example = "3", required = false)
            @RequestParam(required = false) Integer rating,
            @Parameter(name = "visible", description = "Estado del libro, que permite saber si se muestra o no en pantalla", example = "true", required = false)
            @RequestParam(required = false) Boolean visible) {

        log.info("headers: {}", headers);
        List<Book> books = service.getBooks(title, author, category, isbn, publishedDate, price, rating, visible);

        if (books != null) {
            return ResponseEntity.ok(books);
        } else {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @GetMapping("/books/{bookId}")
    @Operation(
            operationId = "Obtener un libro dado su ID",
            description = "Operacion de lectura",
            summary = "Se devuelve un libro a partir de su identificador.")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "No se ha encontrado el libro con el identificador indicado.")
    public ResponseEntity<Book> getBook(@PathVariable String bookId) {

        log.info("Request received for book {}", bookId);
        Book book = service.getBook(bookId);

        if (book != null) {
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping("/books/{bookId}")
    @Operation(
            operationId = "Eliminar un libro dado su ID",
            description = "Operacion de escritura",
            summary = "Se elimina un libro a partir de su identificador.")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)))
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "No se ha encontrado el libro con el identificador indicado.")
    public ResponseEntity<Void> deleteBook(@PathVariable String bookId) {

        Boolean removed = service.removeBook(bookId);

        if (Boolean.TRUE.equals(removed)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping("/books")
    @Operation(
            operationId = "Insertar un nuevo libro",
            description = "Operacion de escritura",
            summary = "Se crea un libro a partir de sus datos.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del libro a ser creado",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateBookRequest.class))))
    @ApiResponse(
            responseCode = "201",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
    @ApiResponse(
            responseCode = "400",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "Datos incorrectos introducidos.")
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "No se ha encontrado el libro con el identificador indicado.")
    public ResponseEntity<Book> addBook(@RequestBody CreateBookRequest request) {

        Book createdBook = service.createBook(request);

        if (createdBook != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


    @PatchMapping("/books/{bookId}")
    @Operation(
            operationId = "Modificar parcialmente un libro",
            description = "RFC 7386. Operacion de escritura",
            summary = "RFC 7386. Se modifica parcialmente un libro.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del libro a modificar.",
                    required = true,
                    content = @Content(mediaType = "application/merge-patch+json", schema = @Schema(implementation = String.class))))
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
    @ApiResponse(
            responseCode = "400",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "Libro invalido o datos incorrectos introducidos.")
    public ResponseEntity<Book> patchBook(@PathVariable String bookId, @RequestBody String patchBody) {

        Book patched = service.updateBook(bookId, patchBody);
        if (patched != null) {
            return ResponseEntity.ok(patched);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


    @PutMapping("/books/{bookId}")
    @Operation(
            operationId = "Modificar por completo los datos de un libro",
            description = "Operacion de escritura",
            summary = "Se modifica todos los datos de un libro.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del libro a modificar.",
                    required = true,
                    content = @Content(mediaType = "application/merge-patch+json", schema = @Schema(implementation = BookDto.class))))
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "Libro no encontrado")
    public ResponseEntity<Book> updateBook(@PathVariable String bookId, @RequestBody BookDto body) {

        Book updatedBook = service.updateBook(bookId, body);
        if (updatedBook != null) {
            return ResponseEntity.ok(updatedBook);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
