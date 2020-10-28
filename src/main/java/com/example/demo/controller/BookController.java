package com.example.demo.controller;


import com.example.demo.entities.Books;
import com.example.demo.services.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@Slf4j
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<List<Books>> findAllBooks(){

        return ResponseEntity.ok(bookService.findAll());
    }

    @GetMapping("/{id}")
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    public ResponseEntity<Books> findBookById(String id){
        return ResponseEntity.ok(bookService.findById(id));
    }

    @PostMapping
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Books> saveBook(@RequestBody Books book){
        return ResponseEntity.ok(bookService.save(book));
    }
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured({"ROLE_ADMIN"})
    public void updateBook(@Validated @PathVariable String id, @RequestBody Books book) {
        bookService.update(id, book);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured({"ROLE_ADMIN"})
    public void deleteBook(@PathVariable String id) {
        bookService.delete(id);
    }


}
