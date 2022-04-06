package com.book.management.repository;

import com.book.management.entity.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, Integer> {
  @Query(value = "SELECT * FROM Book ORDER BY book_id ASC", nativeQuery = true)
  public List<Book> findAllBook();

  public List<Book> findAllBookOrderByBookIdDesc();
}
