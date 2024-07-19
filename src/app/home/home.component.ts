import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; // <-- NgModel lives here
import { BookListComponent } from '../book-list/book-list.component';
import { Book } from '../model/book.interface.';
import { debounceTime, distinctUntilChanged, Subject } from 'rxjs';
import { BookService } from '../service/book.service';

@Component({
	selector: 'app-home',
	standalone: true,
	imports: [CommonModule, FormsModule, BookListComponent],
	template: `
		<div class="container">
			<h1>Book Listings</h1>
			<input
				type="text"
				[(ngModel)]="searchTerm"
				(input)="onSearchInput()"
				placeholder="Search books..."
				class="search-bar"
			/>
			<app-book-list [books]="filteredBooks"></app-book-list>
		</div>
	`,
	styleUrls: ['./home.component.css'],
})
export class HomeComponent {
	books: Book[] = [];
	filteredBooks: Book[] = [];
	bookService: BookService = inject(BookService);
	searchTerm: string = '';
	private searchSubject = new Subject<string>();

	constructor() {
		this.searchSubject
			.pipe(
				debounceTime(300), // Delay Output if User still typing.
				distinctUntilChanged()
			)
			.subscribe(() => {
				this.filterBooks();
			});
	}

	ngOnInit() {
		this.fetchBooks();
	}

	fetchBooks() {
		this.bookService.getBooks().subscribe((books) => {
			this.books = books;
			this.filteredBooks = books;
		});
	}

	onSearchInput() {
		this.searchSubject.next(this.searchTerm);
	}

	filterBooks() {
		this.filteredBooks = this.bookService.filterBooks(
			this.books,
			this.searchTerm
		);
	}
}
