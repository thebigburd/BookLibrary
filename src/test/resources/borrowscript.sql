-- Sql Script to populate the people, book and borrow table for integration testing.
INSERT INTO people (id, email, name, surname, date_of_birth) VALUES (1, 'johndoe@example.com', 'John', 'Doe', '1990-01-01');

INSERT INTO book (id, name, description, publish_year, borrowed) VALUES (1, 'A Book', 'A Description', 2023, 0, 1, BookStatus.UNAVAILABLE);

INSERT INTO borrow (id, book_id, user_id, borrow_date, return_date) VALUES (1, (SELECT id FROM book WHERE id = '1'), (SELECT id FROM people WHERE id = '1' ), '2023-01-01', '2023-01-10');
