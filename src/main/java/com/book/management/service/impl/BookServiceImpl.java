package com.book.management.service.impl;

import com.book.management.dto.request.CreateBookRequest;
import com.book.management.dto.response.BookResponse;
import com.book.management.dto.request.UpdateBookRequest;
import com.book.management.entity.Book;
import com.book.management.repository.BookRepository;
import com.book.management.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

  @Autowired
  private final BookRepository bookRepository;

  private final EntityManager entityManager;

  @Override
  public List<BookResponse> getAllBook() throws ResponseStatusException{
    List<Book> bookModel = bookRepository.findAllBook();

    log.info("List of all book found!");

    List<BookResponse> list = new ArrayList<>();
    bookModel.forEach(
            data -> list.add(
                    BookResponse.builder()
                            .bookId(data.getBookId())
                            .bookTitle(data.getBookTitle())
                            .bookAuthor(data.getBookAuthor())
                            .isbn(data.getIsbn())
                            .build()
            ));
    return list;
  }

  @Override
  public BookResponse getBook(Integer id) {

    Book bookModel = bookRepository.findById(id).orElseThrow(() ->
      new ResponseStatusException(HttpStatus.NOT_FOUND));

      log.info("Latest assignment record found !");

    return BookResponse.builder()
            .bookId(bookModel.getBookId())
            .bookTitle(bookModel.getBookTitle())
            .bookAuthor(bookModel.getBookAuthor())
            .isbn(bookModel.getIsbn())
            .build();
  }

  @Override
  @Transactional
  public BookResponse createBook(CreateBookRequest createBookRequest) {
    Book newBook = bookRepository.save(
            Book.builder()
                    .bookTitle(createBookRequest.getBookTitle())
                    .bookAuthor(createBookRequest.getBookAuthor())
                    .isbn(createBookRequest.getIsbn())
                    .build());

    entityManager.persist(newBook);

    return BookResponse.builder()
            .bookId(newBook.getBookId())
            .bookTitle(newBook.getBookTitle())
            .bookAuthor(newBook.getBookAuthor())
            .isbn(newBook.getIsbn())
            .build();
  }

  @Override
  public BookResponse updateBook(UpdateBookRequest updateBookRequest){
    Book bookModel = bookRepository.findById(updateBookRequest.getBookId()).orElseThrow(() ->
      new ResponseStatusException(HttpStatus.NOT_FOUND));

    bookModel.setBookId(updateBookRequest.getBookId());
    bookModel.setIsbn(updateBookRequest.getIsbn());
    bookModel.setBookTitle(updateBookRequest.getBookTitle());
    bookModel.setBookAuthor(updateBookRequest.getBookAuthor());

    Book bookResponse = bookRepository.save(bookModel);

    log.info("Book record with bookId {} found and updated !", updateBookRequest.getBookId());

    return BookResponse.builder()
            .bookId(bookResponse.getBookId())
            .bookTitle(bookResponse.getBookTitle())
            .bookAuthor(bookResponse.getBookAuthor())
            .isbn(bookResponse.getIsbn())
            .build();
  }

  @Override
  public void deleteBook(Integer bookId) {
    try{
      bookRepository.deleteById(bookId);
      log.info("Book with bookId: "+bookId+" was deleted from database!");
    }catch (ResponseStatusException e){
      e.printStackTrace();
    }
  }

}
