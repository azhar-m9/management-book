package com.book.management.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookRequest {

  @NotNull
  private Integer bookId;

  @NotNull
  private Integer isbn;

  @NotNull
  @NotBlank
  private String bookTitle;

  @NotNull
  @NotBlank
  private String bookAuthor;

}
