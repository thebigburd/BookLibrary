import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { Book } from '../model/book.interface.';

const httpOptions = {
  headers: new HttpHeaders({
      'Accept': 'application/json',
      'Content-Type': 'application/json'
  })
};

@Injectable({
  providedIn: 'root'
})
export class BookService {

  private baseApiUrl: string;

  constructor(private http: HttpClient) {
    this.baseApiUrl = 'http://localhost:8080/athena';
  }

  getBooks(): Observable<any> {
    return this.http.get<any>(`${this.baseApiUrl}/library/list`).pipe(
      map(response => response.data.books)
    );
  }

  filterBooks(books: Book[], searchTerm: string): Book[] {
    return books.filter(book =>
      book.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      book.description.toLowerCase().includes(searchTerm.toLowerCase())
    );
  }
  
}
