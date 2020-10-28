package com.example.demo.services;


import com.example.demo.entities.Books;
import com.example.demo.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private  boolean available;



    @Cacheable(value = "bookCache")
    public List<Books> findAll(){
        log.info("Request to find all books");
        return bookRepository.findAll();
    }

    @Cacheable(value = "bookCache",key = "#id")
    public Books findById(String id){
        return bookRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("book not found %s.", id)));
    }

    @CachePut(value = "bookCache",key = "#result.id")
    public Books save(Books book){
        log.info("saving book.");
        return bookRepository.save(book);
    }

    @CachePut(value = "bookCache",key = "#id")
    public void update(String id, Books book){
        log.info("update a book.");
        if(!bookRepository.existsById(id)){
            log.error(String.format("Could not find book by id %s", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("not found book by id %s", id));
        }
        book.setId(id);
        bookRepository.save(book);
    }

    @CacheEvict(value = "bookCache",key = "#id")
    public void delete(String id){
        if(!bookRepository.existsById(id)){
            log.error(String.format("Could not find book by id %s", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Could not find book by id %s", id));
        }
        bookRepository.deleteById(id);
    }
    @CachePut(value = "bookCache", key = "#id")
    public void loanBook(String id, Books book) {
        if(!bookRepository.existsById(id)) {
            log.error(String.format("Could not find book by id %s.", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, //404 -> Not found
                    String.format("Could not find book by id %s.", id));
        }
        log.info("Loaning book.");
        book.setId(id);
        book.setAvailable(false);
        bookRepository.save(book);
    }

    @CachePut(value = "bookCache", key = "#id")
    public void returnBook(String id, Books book) {
        if(!bookRepository.existsById(id)) {
            log.error(String.format("Could not find book by id %s.", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, //404 -> Not found
                    String.format("Could not find book by id %s.", id));
        }
        log.info("Returning book.");
        book.setId(id);
        book.setAvailable(true);
        bookRepository.save(book);
    }

}
