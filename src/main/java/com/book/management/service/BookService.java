package com.book.management.service;

import com.book.management.dto.request.CreateBookRequest;
import com.book.management.dto.response.BookResponse;
import com.book.management.dto.request.UpdateBookRequest;
import com.book.management.entity.Book;

import java.util.List;

public interface BookService {
  List<BookResponse> getAllBook();
  BookResponse getBook(Integer bookId);
  BookResponse createBook(CreateBookRequest createBookRequest);
  BookResponse updateBook(UpdateBookRequest bookRequest);

  void deleteBook(Integer bookId);
}
