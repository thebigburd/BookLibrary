package com.thebigburd.LibraryApplication.Book;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {


    public List<Book> getBooks(){
        return List.of(
                new Book("A beginner's guide to Spring", "A book describing the basics of the Java Spring framework.", 2022)
        );
    }
}
