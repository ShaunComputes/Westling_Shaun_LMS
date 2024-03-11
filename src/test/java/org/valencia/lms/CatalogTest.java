// Shaun Westling
// CEN3024C CRN24668
// 03/10/2024
package org.valencia.lms;

import org.junit.Assert;

import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

public class CatalogTest {

    private final Catalog catalog = new Catalog();

    @org.junit.Before
    public void setUp() throws Exception {
        var calendar = GregorianCalendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 28);

        catalog.addBook(new Book(  1, "Author 1", "Title 1", null));
        catalog.addBook(new Book(  2, "Author 2", "Title 2", calendar.getTime()));
        catalog.addBook(new Book(  3, "Author 3", "Title 3", null));
        catalog.addBook(new Book(  4, "Author 4", "Title 4", null));
        catalog.addBook(new Book(  5, "Author 4", "Title 4", calendar.getTime()));
        catalog.addBook(new Book(  6, "Author 4", "Title 4", null));
        catalog.addBook(new Book(  7, "Author 5", "Title 5", null));
        catalog.addBook(new Book(  8, "Author 5", "Title 5", calendar.getTime()));
        catalog.addBook(new Book(  9, "Author 5", "Title 5", calendar.getTime()));
        catalog.addBook(new Book( 10, "Author 5", "Title 5", calendar.getTime()));
    }

    //@org.junit.After
    //public void tearDown() throws Exception {
    //}

    //@org.junit.Test
    //public void addBooksFromFile() {
    //}

    //@org.junit.Test
    //public void getBooks() {
    //}

    @org.junit.Test
    public void addBook() {
        // Adding a new book
        Assert.assertNotNull(catalog.addBook(new Book(100, "Author 5", "Title 5", null)));

        // Adding a duplicate book id
        Assert.assertNull(catalog.addBook(new Book( 10, "Author 5", "Title 5", null)));
    }

    @org.junit.Test
    public void removeBook() {
        // Removing a valid book id
        Assert.assertNotNull(catalog.removeBook(3));

        // Removing an invalid book id
        Assert.assertNull(catalog.removeBook(333));
    }

    @org.junit.Test
    public void checkOut() {
        // Checking out a book by id
        Book book = catalog.checkOut(1);
        Assert.assertNotNull(book.dueDate());

        // Checking out a book that is already checked out
        // TODO

        // Checking out a book that doesn't exist
        // TODO

        // Checking out a book by title (without duplicates)
        // Checking out a book by title (with duplicates)
        // TODO: convert null return to error with duplicate list?
    }

    @org.junit.Test
    public void checkIn() {
        // Checking in a book by id
        Book book = catalog.checkIn(2);
        Assert.assertNull(book.dueDate());
    }

    @org.junit.Test
    public void getBooksForCheckOut() {
        Collection<Book> books = catalog.getBooksForCheckOut("Title 4");
        Assert.assertEquals(books.size(), 2);
    }

    @org.junit.Test
    public void getBooksForCheckIn() {
        Collection<Book> books = catalog.getBooksForCheckIn("Title 5");
        Assert.assertEquals(books.size(), 3);
    }
}