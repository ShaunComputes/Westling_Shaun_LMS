// Shaun Westling
// CEN3024C CRN24668
// 03/03/2024

package org.example;

import com.opencsv.CSVReader;
import java.io.FileReader;
import java.util.*;

public class Catalog {

    // Book by id
    private final HashMap<Integer, Book> idMap = new HashMap<>(){};

    // Book by title
    private final HashMap<String, List<Book>> titleMap = new HashMap<>(){};

    /**
     * Adds books from a CSV file, returns the number of books added.
     * @param filePath path to the csv file
     * @return number of books added
     */
    int addBooksFromFile(String filePath) {
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

                int id = Integer.parseInt(nextRecord[0]);
                String author = nextRecord[1];
                String title = nextRecord[2];
                Book book = new Book(id, author, title, null);

                // Check for duplicates?
                if (idMap.containsKey(id)) {
                    System.out.println("Duplicate id at record " + (i+1) + ".");
                    break;
                }

                // Add to idMap
                idMap.put(id, book);

                // Add to titleMap
                //List<Book> list =
                //        titleMap.containsKey(title)
                //        ? titleMap.get(title)
                //        : titleMap.put(title, new ArrayList<>());
                //list.add(book);

                if (titleMap.containsKey(title)) {
                    List<Book> list = titleMap.get(title);
                    list.add(book);
                } else {
                    List<Book> list = new ArrayList<>();
                    titleMap.put(title, list);
                    list.add(book);
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
     * Removes the book with the given ID from the collection.
     * @return true if successful
     */
    Book removeBook(int Id) {
        Book book = idMap.remove(Id);
        if (book == null) {
            return null;
        }

        List<Book> list = titleMap.get(book.title());
        list.remove(book);
        if (list.isEmpty()) {
            titleMap.remove(book.title());
        }
        return book;
    }

    //Check Out A Book
    //The application allows the user to check out a book from the database by title, which is supplied by the user.
    //When a book is checked out, its status is changed to “checked out” and its due date is 4 weeks from the current date.
    //An error message must display if a book is already checked out.
    Book checkOut(int Id) {
        Book book = idMap.get(Id);
        if (book.dueDate() != null)
            return null;

        var calendar = GregorianCalendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 28);
        book = new Book(book.id(), book.author(), book.title(), calendar.getTime());
        idMap.replace(Id, book);
        return book;
    }

    //Check In a Book
    //The application allows the user to check in a book from the database by title, which is supplied by the user.
    //When a book is checked in, its status is changed to “checked in” and its due date is “null”.
    Book checkIn(int Id) {
        Book book = idMap.get(Id);
        if (book.dueDate() == null)
            return null;

        book = new Book(book.id(), book.author(), book.title(), null);
        idMap.replace(Id, book);
        return book;
    }

    Collection<Book> getBooks() {
        return idMap.values();
    }

    Collection<Book> getBooks(String title) {
        return titleMap.get(title);
    }

    Collection<Book> getBooksForCheckOut(String Title) {
        if (!titleMap.containsKey(Title)) {
            return null;
        }

        List<Book> list = new ArrayList<>();
        for (Book book : titleMap.get(Title)) {
            if (book.dueDate() == null)
                list.add(book);
        }
        return list;
    }

    Collection<Book> getBooksForCheckIn(String Title) {
        if (!titleMap.containsKey(Title)) {
            return null;
        }

        List<Book> list = new ArrayList<Book>();
        for (Book book : titleMap.get(Title)) {
            if (book.dueDate() != null)
                list.add(book);
        }
        return list;
    }
}