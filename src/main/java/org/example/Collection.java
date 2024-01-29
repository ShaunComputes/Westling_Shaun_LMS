// Shaun Westling
// CEN3024C CRN24668
// 01/28/2024

package org.example;

import com.opencsv.CSVReader;
import java.io.FileReader;
import java.util.HashMap;

public class Collection {

    private final HashMap<Integer, Book> map = new HashMap<>(){};

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

                // TODO: Check for duplicates?
                map.put(id, new Book(id, author, title));
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
    boolean removeBook(int Id) {
        Book book = map.remove(Id);
        return book != null;
    }

    /**
     * Writes all books to the console
     */
    void listBooks() {
        if (map.isEmpty())
        {
            System.out.println("The collection is empty.");
            return;
        }

        for (Book book : map.values()) {
            //System.out.println(book.id() + " " + book.author() + " " + book.title());
            System.out.println(book);
        }
    }
}
