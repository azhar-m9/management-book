package com.book.management.controller;

import com.book.management.dto.request.CreateBookRequest;
import com.book.management.dto.request.UpdateBookRequest;
import com.book.management.dto.response.BookResponse;
import com.book.management.entity.Book;
import com.book.management.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookController.class)
@ActiveProfiles("test")
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    Book bookModel, bookModel2;
    List<Book> bookList;

    @BeforeEach
    void setUp() {
        //given
        bookModel = new Book(
                LocalDateTime.parse("2022-01-02T14:06:59.972177"),
                LocalDateTime.parse("2022-01-02T14:06:59.972177"),
                2,
                123456,
                "The Alchemist",
                "Paulo Coelho");

        bookList = List.of(bookModel);

        bookModel2 =
                Book.builder()
                        .bookTitle("The Alchemist")
                        .bookAuthor("Paulo Coelho")
                        .isbn(123456)
                        .build();
    }

    @Test
    void shouldGetABook() throws Exception {
        BookResponse builder = BookResponse.builder()
                .bookId(bookModel.getBookId())
                .bookTitle(bookModel.getBookTitle())
                .bookAuthor(bookModel.getBookAuthor())
                .isbn(bookModel.getIsbn())
                .build();
        given(bookService.getBook(2)).willReturn(builder);

        BookResponse response = bookService.getBook(2);

        //when
        mockMvc.perform(get("/api/book/{bookId}", bookModel.getBookId()))

        //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.bookId").value(bookModel.getBookId()))
                .andExpect(jsonPath("$.data.isbn").value(bookModel.getIsbn()))
                .andExpect(jsonPath("$.data.bookTitle").value(bookModel.getBookTitle()))
                .andExpect(jsonPath("$.data.bookAuthor").value(bookModel.getBookAuthor()));

        assertEquals(builder, response);
        verify(bookService, times(2)).getBook(2);

    }

    @Test
    void notFoundABook() throws Exception {
        BookResponse builder = BookResponse.builder().build();
        given(bookService.getBook(2)).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/api/book/{bookId}", 2))
                .andExpect(status().isNotFound());

    }

    @Test
    void getAllBooks() throws Exception {
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

        HashMap<String, Object> map = new HashMap<>();
        map.put("data", list);

        given(bookService.getAllBook()).willReturn(list);

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.[0].bookId").value(bookModel.getBookId()))
                .andExpect(jsonPath("$.data.[0].isbn").value(bookModel.getIsbn()))
                .andExpect(jsonPath("$.data.[0].bookTitle").value(bookModel.getBookTitle()))
                .andExpect(jsonPath("$.data.[0].bookAuthor").value(bookModel.getBookAuthor()));
    }

    @Test
    void getAllBookEmpty() throws Exception {
        when(bookService.getAllBook()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));
    }

    @Test
    void newBook() throws Exception {
        CreateBookRequest createBookRequest = CreateBookRequest.builder()
                .bookId(bookModel.getBookId())
                .bookTitle(bookModel.getBookTitle())
                .bookAuthor(bookModel.getBookAuthor())
                .isbn(bookModel.getIsbn())
                .build();

        BookResponse expected = BookResponse.builder()
                .bookId(createBookRequest.getBookId())
                .bookTitle(createBookRequest.getBookTitle())
                .bookAuthor(createBookRequest.getBookAuthor())
                .isbn(createBookRequest.getIsbn())
                .build();
        given(bookService.createBook(createBookRequest)).willReturn(expected);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookRequest));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.bookId").value(bookModel.getBookId()))
                .andExpect(jsonPath("$.data.bookTitle").value(bookModel.getBookTitle()))
                .andExpect(jsonPath("$.data.bookAuthor").value(bookModel.getBookAuthor()))
                .andExpect(jsonPath("$.data.isbn").value(bookModel.getIsbn()));
    }

    @Test
    void updateBook() throws Exception {
        UpdateBookRequest updateBookRequest = UpdateBookRequest.builder()
                .bookId(bookModel.getBookId())
                .bookTitle(bookModel.getBookTitle())
                .bookAuthor(bookModel.getBookAuthor())
                .isbn(bookModel.getIsbn())
                .build();

        BookResponse expected = BookResponse.builder()
                .bookId(updateBookRequest.getBookId())
                .bookTitle(updateBookRequest.getBookTitle())
                .bookAuthor(updateBookRequest.getBookAuthor())
                .isbn(updateBookRequest.getIsbn())
                .build();

        given(bookService.updateBook(updateBookRequest)).willReturn(expected);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/api/book/update")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateBookRequest));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.bookId").value(bookModel.getBookId()))
                .andExpect(jsonPath("$.data.bookTitle").value(bookModel.getBookTitle()))
                .andExpect(jsonPath("$.data.bookAuthor").value(bookModel.getBookAuthor()))
                .andExpect(jsonPath("$.data.isbn").value(bookModel.getIsbn()));
    }

    @Test
    void deleteBook() throws Exception {
        BookResponse builder = BookResponse.builder()
                .bookId(bookModel.getBookId())
                .bookTitle(bookModel.getBookTitle())
                .bookAuthor(bookModel.getBookAuthor())
                .isbn(bookModel.getIsbn())
                .build();

        given(bookService.getBook(2)).willReturn(builder);
        doNothing().when(bookService).deleteBook(2);

        mockMvc.perform(delete("/api/book/delete/{id}", bookModel.getBookId()))
                .andExpect(status().isOk());

        verify(bookService, times(1)).deleteBook(2);
    }
}