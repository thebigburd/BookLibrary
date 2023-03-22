package com.thebigburd.LibraryApplication.Book;

import com.thebigburd.LibraryApplication.User.UserRepository;
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
        if(bookRepository.existsById(id)){
            return bookRepository.findById(id).get();
        }
        else{
            throw new IllegalArgumentException("Book does not exist with the id " +id + ".");
        }
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
                name.length() > 0){
            if(Objects.equals(book.getName(), name)){
                throw new IllegalArgumentException("This is already the current name of the book.");
            }
            else{
                book.setName(name);
            }
        }

        if(description != null &&
                description.length() > 0){
            if(Objects.equals(book.getDescription(), description)){
                throw new IllegalArgumentException("This is already the current description of the book.");
            }
            else{
                book.setDescription(description);
            }
        }

        if(Objects.equals(book.getPublishYear(), publishYear)){
            throw new IllegalArgumentException("This is already the current publish year.");
        }
        else{
            book.setPublishYear(publishYear);
        }

    }
}
