package com.thebigburd.LibraryApplication.Service;

import com.thebigburd.LibraryApplication.Entity.Book;
import com.thebigburd.LibraryApplication.Repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BookService {

    BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    public Book getBook(Long id){
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book does not exist with the id " +id + "."));
        return book;
    }

    public List<Book> getBookList(){
        return bookRepository.findAll();
    }

    public void addBook(Book book) {
        Optional<Book> bookOptional = bookRepository.findBookByName(book.getName());
        if(bookOptional.isPresent()){
            throw new IllegalArgumentException("This book already exists in the library.");
        }
        else{
            bookRepository.save(book);
        }

    }

    public void deleteBook(Long id) {
        if(!bookRepository.existsById(id)){
            throw new IllegalArgumentException("Book with id " +id +" does not exist.");
        }
        else{
            bookRepository.deleteById(id);
        }
    }

    @Transactional
    public void updateBook(Long id, String name, String description, int publishYear) {
        Book book = getBook(id);

        if(name != null &&
                name.length() > 0) {
            book.setName(name);
        }

        if(description != null &&
                description.length() > 0){
                book.setDescription(description);
        }

        if(publishYear > 0){
            book.setPublishYear(publishYear);
        }

        bookRepository.save(book);
    }
}
