package com.book.management.controller;

import com.book.management.dto.handler.ResponseHandler;
import com.book.management.dto.request.CreateBookRequest;
import com.book.management.dto.request.UpdateBookRequest;
import com.book.management.dto.response.BookResponse;
import com.book.management.dto.response.DataResponse;
import com.book.management.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;


@Slf4j
@RestController
@Tag(name = "BOOK API", description = "CRUD book data")
public class BookController {

  @Autowired
  private BookService bookService;

  @GetMapping("/api/books")
  @Operation(summary = "Get list of books that are available in the database", operationId = "Get List of Books")
  public HashMap<String, Object> getAllBooks(){
    log.info("Invoking get on /api/books");

    HashMap<String, Object> map = new HashMap<>();
    List<BookResponse> result = bookService.getAllBook();
    map.put("data", result);

    return map;
  }

  @GetMapping("/api/book/{bookId}")
  @Operation(summary = "Get book from bookId", operationId = "Get A Book")
  public ResponseEntity<Object> getBook(
    @PathVariable(value="bookId") Integer bookId
  ) {
    log.info("Invoking get on /api/book/{bookId} route");
    try {
      BookResponse result = bookService.getBook(bookId);
      return ResponseHandler.generateResponse(HttpStatus.OK, result);
    }catch (Exception e){
      return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND, "Not found");
    }
  }

  @PostMapping("/api/books")
  @Operation(summary = "Insert new book to the database", operationId = "New Book")
  public ResponseEntity<Object> newBook(
          @Valid @RequestBody CreateBookRequest createBookRequest
          ){
    log.info("Invoking post on api/books");
    try {
      BookResponse newBook = bookService.createBook(createBookRequest);
      return ResponseHandler.generateResponse(HttpStatus.OK, newBook);
    }catch (Exception e){
      return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, "Insertion failed");
    }
  }

  @PutMapping("/api/book/update")
  @Operation(summary = "Update book from bookId", operationId = "Update Book")
  public DataResponse updateBook(
    @Valid @RequestBody UpdateBookRequest bookRequest
    ) {
    log.info("Invoking put on api/book/update route");

    return DataResponse.builder()
            .data(bookService.updateBook(bookRequest))
            .build();
  }

  @DeleteMapping("/api/book/delete/{id}")
  @Operation(summary = "Delete book from provided bookId", operationId = "Delete Book")
  public void deleteBook(
          @PathVariable Integer id
  ){
    log.info("Invoking delete on /api/book/delete route");

    bookService.deleteBook(id);
  }

}
