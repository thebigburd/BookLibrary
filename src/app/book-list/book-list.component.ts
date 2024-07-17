import { Component, Input, OnInit } from '@angular/core';
import { Book } from '../model/book.interface.';
import { BookService } from '../service/book.service';
import { CommonModule } from '@angular/common';


@Component({
	selector: 'app-book-list',
	standalone: true,
	imports: [CommonModule],
	templateUrl: './book-list.component.html',
	styleUrl: './book-list.component.css',
	providers: [BookService],
})
export class BookListComponent {
	@Input() books: Book[] = [];
  }
