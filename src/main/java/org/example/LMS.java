// Shaun Westling
// CEN3024C CRN24668
// 03/03/2024

package org.example;

import java.util.Collection;
import java.util.Scanner;

public class LMS {
    static Catalog catalog = new Catalog();
    static Scanner in      = new Scanner(System.in);

    public static void main(String[] args)
    {
        //System.out.println("Working Directory = " + System.getProperty("user.dir"));

        int option;

        while(true) {
            System.out.println();
            System.out.println("Available commands:");
            System.out.println("0 Exit");
            System.out.println("1 List all books.");
            System.out.println("2 Check out a book.");
            System.out.println("3 Check in a book.");
            System.out.println("7 Import books from a comma-delimited text file.");
            System.out.println("8 Remove a book by Barcode.");
            System.out.println("9 Remove a book by Title.");

            while(true) {
                try {
                    System.out.println();
                    System.out.print("Choose a command: ");
                    option = in.nextInt();
                    in.nextLine();
                    break;
                } catch (Exception ignored) {
                    in.next(); // Eat the invalid input
                    System.out.println("Invalid input.");
                }
            }

            System.out.println();
            switch (option) {
                case 0  -> { return; }
                case 1  -> listBooks();
                case 2  -> checkOutBook();
                case 3  -> checkInBook();
                case 7  -> addBooks();
                case 8  -> removeBookByBarcode();
                case 9  -> removeBookByTitle();
                default -> System.out.println("Invalid command.");
            }
        }
    }

    /**
     * Display the Contents of the Database
     * The application is able to display the contents of the database on the screen.
     */
    public static void listBooks() {
        Collection<Book> books = catalog.getBooks();
        if (books.isEmpty()) {
            System.out.println("The collection is empty.");
            return;
        }
        for (Book book : books) {
            System.out.println(book);
        }
    }

    /**
     * Check Out A Book
     * The application allows the user to check out a book from the database by title, which is supplied by the user.
     * When a book is checked out, its status is changed to “checked out” and its due date is 4 weeks from the current date.
     * An error message must display if a book is already checked out.
     */
    public static void checkOutBook() {
        String title;
        while (true) {
            try {
                System.out.print("Book title: ");
                title = in.next();
                break;
            } catch (Exception ignored) {
                in.next(); // Eat the invalid input
                System.out.println("Invalid input.");
            }
        }

        Collection<Book> books = catalog.getBooksForCheckOut(title);

        if (books == null) {
            System.out.println();
            System.out.println("Title not found.");
            return;
        }

        if (books.isEmpty()) {
            System.out.println();
            System.out.println("No books are available.");
            return;
        }

        if (books.size() == 1) {
            Book book = books.iterator().next();
            catalog.checkOut(book.id());
            System.out.println();
            System.out.println("Book " + book.id() + " checked out.");
            return;
        }

        System.out.println();
        for (Book book : books) {
            System.out.println(book);
        }

        int id;
        while (true) {
            try {
                System.out.print("Book id: ");
                id = in.nextInt();
                in.nextLine();
                break;
            } catch (Exception ignored) {
                in.next(); // Eat the invalid input
                System.out.println("Invalid input.");
            }
        }

        if (catalog.checkOut(id) == null) {
            System.out.println();
            System.out.println("Book " + id + " not found.");
            return;
        }

        System.out.println();
        System.out.println("Book " + id + " checked out.");
    }

    /**
     * Check In a Book
     * The application allows the user to check in a book from the database by title, which is supplied by the user.
     * When a book is checked in, its status is changed to “checked in” and its due date is “null”.
     */
    public static void checkInBook() {
        String title;
        while (true) {
            try {
                System.out.print("Book title: ");
                title = in.next();
                break;
            } catch (Exception ignored) {
                in.next(); // Eat the invalid input
                System.out.println("Invalid input.");
            }
        }

        Collection<Book> books = catalog.getBooksForCheckIn(title);

        if (books == null) {
            System.out.println();
            System.out.println("Title not found.");
            return;
        }

        if (books.isEmpty()) {
            System.out.println();
            System.out.println("No books are available.");
            return;
        }

        if (books.size() == 1) {
            Book book = books.iterator().next();
            catalog.checkIn(book.id());
            System.out.println();
            System.out.println("Book " + book.id() + " checked in.");
            return;
        }

        System.out.println();
        for (Book book : books) {
            System.out.println(book);
        }

        int id;
        while (true) {
            try {
                System.out.print("Book id: ");
                id = in.nextInt();
                in.nextLine();
                break;
            } catch (Exception ignored) {
                in.next(); // Eat the invalid input
                System.out.println("Invalid input.");
            }
        }

        if (catalog.checkIn(id) == null) {
            System.out.println();
            System.out.println("Book " + id + " not found.");
            return;
        }

        System.out.println();
        System.out.println("Book " + id + " checked in.");
    }

    /**
     * Add Books
     * The application allows the user to add books to the LMS by reading a user-supplied comma-delimited text file.
     */
    public static void addBooks() {
        String filePath; // = "import.csv";
        while (true) {
            try {
                System.out.print("File path [import.csv]: ");
                filePath = in.nextLine();
                if (filePath.isEmpty()) filePath = "import.csv";
                //String line = in.nextLine();
                //if (line.isEmpty()) filePath = "import.csv";
                //else filePath = line;
                break;
            } catch (Exception ignored) {
                in.next(); // Eat the invalid input
                System.out.println("Invalid input.");
            }
        }

        int imported = catalog.addBooksFromFile(filePath);
        System.out.println("Imported " + imported + " books.");
    }

    /**
     * Remove Books by Barcode
     * The application allows the user to remove a book from the database by barcode number, which is supplied by the user.
     */
    public static void removeBookByBarcode() {
        int id;
        while (true) {
            try {
                System.out.print("Enter the book id: ");
                id = in.nextInt();
            } catch (Exception ignored) {
                in.next(); // Eat the invalid input
                System.out.println("Invalid id.");
                continue;
            }

            try {
                if (catalog.removeBook(id) != null) {
                    System.out.println("Book removed from collection.");
                } else {
                    System.out.println("Book not in collection.");
                }
                break;
            } catch (Exception ignored) {
                System.out.println("Invalid id.");
            }
        }
    }

    /**
     * Remove Books by Title
     * The application allows the user to remove a book from the database by title, which is supplied by the user.
     */
    public static void removeBookByTitle() {
        String title;
        while (true) {
            try {
                System.out.print("Book title: ");
                title = in.next();
                break;
            } catch (Exception ignored) {
                in.next(); // Eat the invalid input
                System.out.println("Invalid input.");
            }
        }

        Collection<Book> books = catalog.getBooks(title);

        if (books == null) {
            System.out.println();
            System.out.println("Title not found.");
            return;
        }

        if (books.isEmpty()) {
            System.out.println();
            System.out.println("No books are available.");
            return;
        }

        // TODO: Add Y/N confirmation if size = 1
        //if (books.size() == 1) {
        //    Book book = books.iterator().next();
        //    catalog.removeBook(book.id());
        //    System.out.println();
        //    System.out.println("Book " + book.id() + " removed.");
        //    return;
        //}

        System.out.println();
        for (Book book : books) {
            System.out.println(book);
        }

        int id;
        while (true) {
            try {
                System.out.print("Book id: ");
                id = in.nextInt();
                break;
            } catch (Exception ignored) {
                in.next(); // Eat the invalid input
                System.out.println("Invalid input.");
            }
        }

        // TODO: Warn if the book is checked out?

        Book book = catalog.removeBook(id);
        if (book == null) {
            System.out.println();
            System.out.println("Book " + id + " not found.");
            return;
        }

        System.out.println();
        System.out.println("Book " + id + " removed.");
    }
}