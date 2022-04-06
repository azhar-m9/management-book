package com.book.management.service.impl;

import com.book.management.dto.request.CreateBookRequest;
import com.book.management.dto.request.UpdateBookRequest;
import com.book.management.dto.response.BookResponse;
import com.book.management.entity.Book;
import com.book.management.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private BookServiceImpl bookServiceImpl;

    Book bookModel, bookModel2;

    @BeforeEach
    void setUp() {
        bookModel =
                Book.builder()
                        .bookId(2)
                        .bookTitle("The Alchemist")
                        .bookAuthor("Paulo Coelho")
                        .isbn(123456)
                        .build();
        bookModel2 =
                Book.builder()
                        .bookTitle("The Alchemist")
                        .bookAuthor("Paulo Coelho")
                        .isbn(123456)
                        .build();
    }

    @Test
    void getBook() {
        given(bookRepository.findById(2)).willReturn(Optional.of(bookModel));

        BookResponse dataBook = BookResponse.builder()
                .bookId(bookModel.getBookId())
                .bookTitle(bookModel.getBookTitle())
                .bookAuthor(bookModel.getBookAuthor())
                .isbn(bookModel.getIsbn())
                .build();


        BookResponse response = bookServiceImpl.getBook(2);

        verify(bookRepository, times(1)).findById(2);
        assertThat(response).isNotNull();
        assertEquals(response, dataBook);

        //System.out.println(response);
        log.info(response.toString());
    }

    @Test
    void getBookIsNotFound() {

        assertThrows(ResponseStatusException.class, () -> {

            when(bookRepository.findById(2)).thenReturn(Optional.empty());
            BookResponse bookResponse = bookServiceImpl.getBook(2);
        });
    }

    @Test
    void getAllBook() {
        List<Book> bookList = new ArrayList<>();
        bookList.add(bookModel);

        List<BookResponse> list = new ArrayList<>();
        bookList.forEach(
                data-> list.add(
                        BookResponse.builder()
                                .bookId(data.getBookId())
                                .bookTitle(data.getBookTitle())
                                .bookAuthor(data.getBookAuthor())
                                .isbn(data.getIsbn())
                                .build()
                ));

        given(bookRepository.findAllBook()).willReturn(bookList);

        List<BookResponse> expected = bookServiceImpl.getAllBook();

        assertEquals(expected, list);
    }

    @Test
    void getAllBookIsEmpty() {
        given(bookRepository.findAllBook()).willReturn(Collections.emptyList());
        bookServiceImpl.getAllBook();
    }

    @Test
    void createBook() {

        given(bookRepository.save(bookModel2)).willAnswer(invocation -> invocation.getArgument(0));
        entityManager.persist(bookModel2);

        CreateBookRequest createBookRequest = CreateBookRequest.builder()
                .bookTitle(bookModel2.getBookTitle())
                .bookAuthor(bookModel2.getBookAuthor())
                .isbn(bookModel2.getIsbn())
                .build();

        BookResponse expected = BookResponse.builder()
                .bookId(bookModel2.getBookId())
                .bookTitle(bookModel2.getBookTitle())
                .bookAuthor(bookModel2.getBookAuthor())
                .isbn(bookModel2.getIsbn())
                .build();
        BookResponse actual = bookServiceImpl.createBook(createBookRequest);

        assertThat(actual).isNotNull();
        assertEquals(expected, actual);
        verify(bookRepository).save(bookModel2);
    }

    @Test
    void updateBook() {
        UpdateBookRequest updateBookRequest = UpdateBookRequest.builder()
                .bookId(bookModel.getBookId())
                .bookTitle(bookModel.getBookTitle())
                .bookAuthor(bookModel.getBookAuthor())
                .isbn(bookModel.getIsbn())
                .build();

        when(bookRepository.findById(2)).thenReturn(Optional.of(bookModel));
        given(bookRepository.save(bookModel)).willReturn(bookModel);


        BookResponse expected = BookResponse.builder()
                .bookId(bookModel.getBookId())
                .bookTitle(bookModel.getBookTitle())
                .bookAuthor(bookModel.getBookAuthor())
                .isbn(bookModel.getIsbn())
                .build();
        BookResponse actual = bookServiceImpl.updateBook(updateBookRequest);

        assertThat(actual).isNotNull();
        assertEquals(expected, actual);
        verify(bookRepository).save(bookModel);
    }

    @Test
    void deleteBook() {
        bookServiceImpl.deleteBook(2);
        verify(bookRepository, times(1)).deleteById(2);
    }

}