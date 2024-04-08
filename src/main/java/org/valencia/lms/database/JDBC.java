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

public class JDBC {
    public static void checkSqliteDriver() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection connect() throws SQLException {
        String url = "jdbc:sqlite:LMS.sqlite.db";
        return DriverManager.getConnection(url);
    }

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

    public static void booksDelete(Connection conn, int barcode) throws SQLException {
        String sql = "DELETE FROM Books WHERE barcode = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, barcode);
            pstmt.executeUpdate();
            System.out.println("Book " + barcode + " deleted.");
        }
    }

    public static void booksCheckout(Connection conn, int barcode, Date duedate) throws SQLException {
        String sql = "UPDATE Books SET status = 1, duedate = ? WHERE barcode = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, duedate);
            pstmt.setInt(2, barcode);
            pstmt.executeUpdate();
            System.out.println("Book " + barcode + " checked out.");
        }
    }

    public static void booksCheckin(Connection conn, int barcode) throws SQLException {
        String sql = "UPDATE Books SET status = 0, duedate = NULL WHERE barcode = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, barcode);
            pstmt.executeUpdate();
            System.out.println("Book " + barcode + " checked in.");
        }
    }

    public static List<Book> booksSelect(Connection conn) throws SQLException {
        String sql = "SELECT barcode, title, author, genre, status, duedate FROM Books";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            return booksSelect(rs);
        }
    }

    public static Book booksSelect(Connection conn, int barcode) throws SQLException {
        String sql = "SELECT barcode, title, author, genre, status, duedate FROM Books WHERE barcode = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, barcode);
            ResultSet rs = pstmt.executeQuery();
            List<Book> books = booksSelect(rs);
            return books.get(0);
        }
    }

    public static List<Book> booksSelect(Connection conn, String title) throws SQLException {
        String sql = "SELECT barcode, title, author, genre, status, duedate FROM Books WHERE title LIKE ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();
            return booksSelect(rs);
        }
    }

    public static List<Book> booksSelectCheckout(Connection conn, String title) throws SQLException {
        String sql = "SELECT barcode, title, author, genre, status, duedate FROM Books WHERE title LIKE ? AND status = 0";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();
            return booksSelect(rs);
        }
    }

    public static List<Book> booksSelectCheckin(Connection conn, String title) throws SQLException {
        String sql = "SELECT barcode, title, author, genre, status, duedate FROM Books WHERE title LIKE ? AND status = 1";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();
            return booksSelect(rs);
        }
    }

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
