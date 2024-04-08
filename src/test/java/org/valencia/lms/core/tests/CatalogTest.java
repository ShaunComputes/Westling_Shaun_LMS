// Shaun Westling
// CEN3024C CRN24668
// 03/10/2024
package org.valencia.lms.core.tests;

import org.junit.Assert;
import org.valencia.lms.core.Book;
import org.valencia.lms.core.Catalog;

import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

public class CatalogTest {
    private final Catalog catalog = new Catalog();

    @org.junit.Before
    public void setUp() {
        var calendar = GregorianCalendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 28);
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
        Assert.assertTrue(catalog.addBook("Author 5", "Title 5", "Genre 5"));

        // Adding a duplicate book id
        //Assert.assertFalse(catalog.addBook("Author 5", "Title 5", "Genre 5"));
    }

    @org.junit.Test
    public void removeBook() {
        // Removing a valid book id
        Assert.assertTrue(catalog.removeBook(3));

        // Removing an invalid book id
        Assert.assertFalse(catalog.removeBook(333));
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