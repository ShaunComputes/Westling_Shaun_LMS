// Shaun Westling
// CEN3024C CRN24668
// 04/07/2024

package org.valencia.lms.database;

import org.valencia.lms.core.Book;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

/**
 * Contains methods for interacting with the database.
 */
public class JDBC {

    /**
     * Check if the driver is installed before trying to query the database.
     */
    public static void checkSqliteDriver() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a re-usable connection the to database.
     * @return a connection to the database.
     * @throws SQLException
     */
    public static Connection connect() throws SQLException {
        String url = "jdbc:sqlite:LMS.sqlite.db";
        return DriverManager.getConnection(url);
    }

    /**
     * Deletes and re-creates database with default data
     */
    public static void createNewDatabase() {
        File f = new File("LMS.sqlite.db");
        if(f.exists() && !f.isDirectory()) {
            if (f.delete()) {
                System.out.println("Old database deleted.");
            } else {
                System.out.println("Old database not deleted.");
                exit(1);
            }
        }

        try (Connection conn = connect()) {
            DatabaseMetaData meta = conn.getMetaData();
            System.out.println("The driver name is " + meta.getDriverName());

            // Table: Books
            booksCreate(conn);

            booksInsert(conn, "George Orwell", "Nineteen Eighty-Four", "Science fiction");
            booksInsert(conn, "Charles Dickens", "A Tale of Two Cities", "Historical fiction");
            booksInsert(conn, "J. K. Rowling", "Harry Potter and the Philosopher's Stone", "Children's fiction");
            booksInsert(conn, "Cao Xueqin", "Dream of the Red Chamber", "Family saga");
            booksInsert(conn, "Agatha Christie", "And Then There Were None", "Mystery");

            booksInsert(conn, "George Orwell", "Nineteen Eighty-Four", "Science fiction");
            booksInsert(conn, "William Griffith Wilson", "Alcoholics Anonymous", "Self-Help");
            booksInsert(conn, "Stephen Hawking", "A Brief History of Time", "Popular science");
            booksInsert(conn, "E. L. James", "Fifty Shades of Grey", "Erotica");
            booksInsert(conn, "Lois Lowry", "The Giver", "Dystopian fiction");

            booksInsert(conn, "George Orwell", "Nineteen Eighty-Four", "Science fiction", true, new Date(2024-1900, 4, 11));
            booksInsert(conn, "Lois Lowry", "The Giver", "Dystopian fiction", true, new Date(2024-1900, 4, 17));
            booksInsert(conn, "Thich Nhat Hanh", "For a Future to Be Possible", "Philosphy", true, new Date(2024-1900, 4, 10));
            booksInsert(conn, "James Clavell", "Shogun", "Historical novel");
            booksInsert(conn, "Desmond Morris", "The Naked Ape", "Psychology");

            booksInsert(conn, "George Orwell", "Nineteen Eighty-Four", "Science fiction", true, new Date(2024-1900, 4, 9));
            booksInsert(conn, "Kugane Maruyama", "Overlord", "Dark fantasy", true, new Date(2024-1900, 4, 18));
            booksInsert(conn, "Kumo Kagyu", "Goblin Slayer", "Light novel", true, new Date(2024-1900, 4, 16));
            booksInsert(conn, "The Bakeoff Team", "The Official 2023 Great British Bake Off Book", "Cooking", true, new Date(2024-1900, 4, 1));
            booksInsert(conn, "Robert T Kiyosaki", "Rich Dad Poor Dad", "Personal finance", true, new Date(2024-1900, 4, 16));

            System.out.println("New database created.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * SQL query to create the Books table.
     * @param conn Connection to the database.
     * @throws SQLException
     */
    private static void booksCreate(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Books (\n"
                + "barcode INTEGER PRIMARY KEY,"
                + "title VARCHAR(200),"
                + "author VARCHAR(100),"
                + "genre VARCHAR(100),"
                + "status BOOLEAN NOT NULL DEFAULT FALSE,"
                + "duedate DATE NULL"
                + ");";

        Statement stmt = conn.createStatement();
        stmt.execute(sql);
        System.out.println("Books table created.");
    }

    /**
     * Query to insert a book into the Books table.
     * @param conn Connection to the database.
     * @param barcode The book's barcode.
     * @param title The book's title.
     * @param author The book's author.
     * @param genre The book's genre.
     * @throws SQLException
     */
    public static void booksInsert(Connection conn, int barcode, String title, String author, String genre) throws SQLException {
        String sql = "INSERT INTO Books (barcode, title, author, genre) VALUES(?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, barcode);
            pstmt.setString(2, title);
            pstmt.setString(3, author);
            pstmt.setString(4, genre);
            pstmt.executeUpdate();
            System.out.println("Book " + title + " inserted.");
        }
    }

    /**
     * Query to insert a book into the Books table.
     * The book's barcode will be generated automatically.
     * @param conn Connection to the database.
     * @param author The book's author.
     * @param title The book's title.
     * @param genre The book's genre.
     * @throws SQLException
     */
    public static void booksInsert(Connection conn, String author, String title, String genre) throws SQLException {
        String sql = "INSERT INTO Books (title, author, genre) VALUES(?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setString(3, genre);
            pstmt.executeUpdate();
            System.out.println("Book " + title + " inserted.");
        }
    }

    /**
     * Query to insert a book into the Books table.
     * The book's barcode will be generated automatically.
     * @param conn Connection to the database.
     * @param author The book's author.
     * @param title The book's title.
     * @param genre The book's genre.
     * @param status Whether the book is checked in or checked out.
     * @param duedate The book's due date.
     * @throws SQLException
     */
    public static void booksInsert(Connection conn, String author, String title, String genre, boolean status, Date duedate) throws SQLException {
        String sql = "INSERT INTO Books (title, author, genre, status, duedate) VALUES(?,?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setString(3, genre);
            pstmt.setBoolean(4, status);
            pstmt.setDate(5, duedate);
            pstmt.executeUpdate();
            System.out.println("Book " + title + " inserted.");
        }
    }

    /**
     * Query to delete a book from the Books table.
     * @param conn Connection to the database.
     * @param barcode The book's unique barcode.
     * @throws SQLException
     */
    public static void booksDelete(Connection conn, int barcode) throws SQLException {
        String sql = "DELETE FROM Books WHERE barcode = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, barcode);
            pstmt.executeUpdate();
            System.out.println("Book " + barcode + " deleted.");
        }
    }

    /**
     * Query to mark a book as checked out.
     * @param conn Connection to the database.
     * @param barcode The book's unique barcode.
     * @param duedate The due date for checking the book back in.
     * @throws SQLException
     */
    public static void booksCheckout(Connection conn, int barcode, Date duedate) throws SQLException {
        String sql = "UPDATE Books SET status = 1, duedate = ? WHERE barcode = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, duedate);
            pstmt.setInt(2, barcode);
            pstmt.executeUpdate();
            System.out.println("Book " + barcode + " checked out.");
        }
    }

    /**
     * Query to mark a book as checked in.
     * @param conn Connection to the database.
     * @param barcode The book's unique barcode.
     * @throws SQLException
     */
    public static void booksCheckin(Connection conn, int barcode) throws SQLException {
        String sql = "UPDATE Books SET status = 0, duedate = NULL WHERE barcode = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, barcode);
            pstmt.executeUpdate();
            System.out.println("Book " + barcode + " checked in.");
        }
    }

    /**
     * Query to receive all books from the database.
     * @param conn Connection to the database.
     * @return a list of books.
     * @throws SQLException
     */
    public static List<Book> booksSelect(Connection conn) throws SQLException {
        String sql = "SELECT barcode, title, author, genre, status, duedate FROM Books";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            return booksSelect(rs);
        }
    }

    /**
     * Query to retrieve a book from the database.
     * @param conn Connection to the database.
     * @param barcode The unique barcode to use in the query.
     * @return the book with the given barcode.
     * @throws SQLException
     */
    public static Book booksSelect(Connection conn, int barcode) throws SQLException {
        String sql = "SELECT barcode, title, author, genre, status, duedate FROM Books WHERE barcode = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, barcode);
            ResultSet rs = pstmt.executeQuery();
            List<Book> books = booksSelect(rs);
            return books.get(0);
        }
    }

    /**
     * Query to retrieve a book from the database.
     * @param conn Connection to the database.
     * @param title The title to search for.
     * @return a list of books matching the title.
     * @throws SQLException
     */
    public static List<Book> booksSelect(Connection conn, String title) throws SQLException {
        String sql = "SELECT barcode, title, author, genre, status, duedate FROM Books WHERE title LIKE ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();
            return booksSelect(rs);
        }
    }

    /**
     * Query to retrieve a list of books available for check out.
     * @param conn Connection to the database.
     * @param title The title to search for.
     * @return a list of available books.
     * @throws SQLException
     */
    public static List<Book> booksSelectCheckout(Connection conn, String title) throws SQLException {
        String sql = "SELECT barcode, title, author, genre, status, duedate FROM Books WHERE title LIKE ? AND status = 0";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();
            return booksSelect(rs);
        }
    }

    /**
     * Query to retrieve a list of books available for check in.
     * @param conn Connection to the database.
     * @param title The title to search for.
     * @return a list of available books.
     * @throws SQLException
     */
    public static List<Book> booksSelectCheckin(Connection conn, String title) throws SQLException {
        String sql = "SELECT barcode, title, author, genre, status, duedate FROM Books WHERE title LIKE ? AND status = 1";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();
            return booksSelect(rs);
        }
    }

    /**
     * Helper method to parse the SQL result into a list of Books.
     * @param rs The SQL result set.
     * @return the books from the result.
     * @throws SQLException
     */
    private static List<Book> booksSelect(ResultSet rs) throws SQLException {
        List<Book> books = new ArrayList<>();
        while (rs.next()) {
            Book book = new Book(
                    rs.getInt("barcode"),
                    rs.getString("author"),
                    rs.getString("title"),
                    rs.getString("genre"),
                    //rs.getBoolean("status"),
                    rs.getDate("duedate"));
            books.add(book);
        }

        return books;
    }
}
