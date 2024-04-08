// Shaun Westling
// CEN3024C CRN24668
// 03/03/2024

package org.valencia.lms.core;

import com.opencsv.CSVReader;
import org.valencia.lms.database.JDBC;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.*;

public class Catalog {

    private final Connection connection;

    public Catalog() {
        try {
            JDBC.checkSqliteDriver();
            JDBC.createNewDatabase();
            connection = JDBC.connect();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds books from a CSV file, returns the number of books added.
     * @param filePath path to the csv file
     * @return number of books added
     */
    public int addBooksFromFile(String filePath) {
        try {
            FileReader filereader = new FileReader(filePath);
            CSVReader csvReader = new CSVReader(filereader);
            String[] nextRecord;

            // we are going to read data line by line
            int i;
            for (i = 0; (nextRecord = csvReader.readNext()) != null; i++) {
                if (nextRecord.length != 3) {
                    System.out.println("Invalid record length at record " + (i+1) + ".");
                    break;
                }

                String author = nextRecord[0];
                String title = nextRecord[1];
                String genre = nextRecord[2];

                if (!addBook(author, title, genre)) {
                    System.out.println("Error importing book at record " + (i+1) + ".");
                }
            }

            return i;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Adds a book to the catalog.
     * @param author The book's author.
     * @param title The book's title.
     * @param genre The book's genre.
     * @return The same book on success, otherwise null.
     */
    public boolean addBook(String author, String title, String genre) {
        try {
            JDBC.booksInsert(connection, author, title, genre);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Removes the book with the given ID from the collection.
     * @return the book if successful, otherwise null
     */
    public boolean removeBook(int barcode) {
        try {
            JDBC.booksDelete(connection, barcode);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    //Check Out A Book
    //The application allows the user to check out a book from the database by title, which is supplied by the user.
    //When a book is checked out, its status is changed to “checked out” and its due date is 4 weeks from the current date.
    //An error message must display if a book is already checked out.
    public Book checkOut(int barcode) {
        var calendar = GregorianCalendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 28);
        java.util.Date date = calendar.getTime();

        try {
            JDBC.booksCheckout(connection, barcode, new Date(date.getYear(), date.getMonth(), date.getDay()));
            return JDBC.booksSelect(connection, barcode);
        } catch (SQLException e) {
            return null;
        }
    }

    //Check In a Book
    //The application allows the user to check in a book from the database by title, which is supplied by the user.
    //When a book is checked in, its status is changed to “checked in” and its due date is “null”.
    public Book checkIn(int barcode) {
        try {
            JDBC.booksCheckin(connection, barcode);
            return JDBC.booksSelect(connection, barcode);
        } catch (SQLException e) {
            return null;
        }
    }

    public Collection<Book> getBooks() {
        try {
            return JDBC.booksSelect(connection);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Collection<Book> getBooks(String Title) {
        try {
            return JDBC.booksSelect(connection, Title);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Collection<Book> getBooksForCheckOut(String Title) {
        try {
            return JDBC.booksSelectCheckout(connection, Title);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Collection<Book> getBooksForCheckIn(String Title) {
        try {
            return JDBC.booksSelectCheckin(connection, Title);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}