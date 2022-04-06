package com.book.management.dto.response;

import com.book.management.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {

  private Integer bookId;
  private Integer isbn;
  private String bookTitle;
  private String bookAuthor;

}
