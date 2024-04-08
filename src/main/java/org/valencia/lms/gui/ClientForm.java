// Shaun Westling
// CEN3024C CRN24668
// 03/24/2024

package org.valencia.lms.gui;

import org.valencia.lms.core.Book;
import org.valencia.lms.core.Catalog;

import javax.swing.*;
import java.util.Collection;

public class ClientForm extends JFrame {
    private JButton importFileButton;
    private JPanel panel1;
    private JButton viewCatalogButton;
    private JButton removeBarcodeButton;
    private JButton removeTitleButton;
    private JButton checkOutButton;
    private JButton checkInButton;
    private JButton exitButton;
    private JTextArea textArea1;

    private final Catalog catalog = new Catalog();

    public static void main(String[] args) {
        JFrame clientForm = new ClientForm();
        clientForm.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        clientForm.setVisible(true);
    }

    public ClientForm() {
        setTitle("LMS Client");
        setSize(450, 300);
        setContentPane(panel1);

        importFileButton.addActionListener(e -> importFileAction());
        viewCatalogButton.addActionListener(e -> viewCatalogAction());
        checkInButton.addActionListener(e -> checkInAction());
        checkOutButton.addActionListener(e -> checkOutAction());
        removeBarcodeButton.addActionListener(e -> removeBarcodeAction());
        removeTitleButton.addActionListener(e -> removeTitleAction());
        exitButton.addActionListener(e -> exitAction());

        viewCatalogAction();
    }

    /**
     * Ask the user for a file name, and receive it from the user.  Open the file supplied by the user, and add each book to the LMS database.  Display an error message if the file was not found.
     */
    private void importFileAction() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File file = fileChooser.getSelectedFile();
                if (!file.exists()) {
                    showMessage("File does not exist.");
                    return;
                }

                catalog.addBooksFromFile(file.getAbsolutePath());
            } catch (Exception e) {
                showMessage(e.getMessage());
            }
        }

        showMessage("Import complete.");
    }

    /**
     * Print the database to the screen.
     */
    private void viewCatalogAction() {
        textArea1.setText("");
        Collection<Book> books = catalog.getBooks();

        if (books.isEmpty()) {
            textArea1.setText("No books in catalog.");
            return;
        }

        for (Book book : books) {
            textArea1.append(book.toString());
            textArea1.append("\n");
        }
    }

    /**
     * Ask the user for a barcode number to remove.  Receive the barcode, remove the corresponding book from the database, and print a confirmation message that the book was deleted.  Display an error message if the barcode was not found.
     */
    private void removeBarcodeAction() {
        String input = JOptionPane.showInputDialog("Enter the book id: ");
        int id;
        try {
            id = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            showMessage("Invalid Id.");
            return;
        }

        String message = catalog.removeBook(id) ? "Book " + id + " removed successfully." : "Book " + id + " not found.";
        showMessage(message);
    }

    /**
     * Ask the user for a title number to remove.  Receive the title, remove the corresponding book from the database, and print a confirmation message that the book was deleted. Display an error message if the title was not found.
     */
    private void removeTitleAction() {
        String title = JOptionPane.showInputDialog("Enter the book title: ");
        if (title == null) {
            return;
        }

        Collection<Book> books = catalog.getBooks(title);

        if (books == null) {
            showMessage("Title not found.");
            return;
        }

        if (books.isEmpty()) {
            showMessage("No books are available.");
            return;
        }

        int id;
        if (books.size() == 1)
        {
            Book book = books.iterator().next();
            id = book.barcode();
        }
        else
        {
            StringBuilder ids = new StringBuilder();
            for (Book book : books)
            {
                if (!ids.isEmpty()) {
                    ids.append(", ");
                }
                ids.append(book.barcode());
            }

            String input = JOptionPane.showInputDialog("Which book id [" + ids + "]: ");
            if (input == null) return;

            try {
                id = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                showMessage("Invalid Id.");
                return;
            }
        }

        showMessage(catalog.removeBook(id) ? "Book " + id + " removed successfully." : "Book " + id + " not found.");
    }

    /**
     * Ask the user for a title to check out. Check out the book, and print a confirmation message.  Display an error message if the book is already checked out.
     */
    private void checkOutAction() {
        String title = JOptionPane.showInputDialog("Enter the book title: ");

        if (title == null) return;

        Collection<Book> books = catalog.getBooksForCheckOut(title);

        if (books == null) {
            showMessage("Title not found.");
            return;
        }

        if (books.isEmpty()) {
            showMessage("No books are available.");
            return;
        }

        int id;
        if (books.size() == 1) {
            id = books.iterator().next().barcode();
        }
        else
        {
            StringBuilder ids = new StringBuilder();
            for (Book book : books)
            {
                if (!ids.isEmpty()) {
                    ids.append(", ");
                }
                ids.append(book.barcode());
            }

            String input = JOptionPane.showInputDialog("Which book id [" + ids + "]: ");
            if (input == null) return;

            try {
                id = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                showMessage("Invalid Id.");
                return;
            }
        }

        if (catalog.checkOut(id) == null) {
            showMessage("Book " + id + " not found.");
            return;
        }

        showMessage("Book " + id + " checked out.");
    }

    /**
     * Ask the user for a title to check in. Check in the book, and print a confirmation message.
     */
    private void checkInAction() {
        String title = JOptionPane.showInputDialog("Enter the book title: ");

        if (title == null) return;

        Collection<Book> books = catalog.getBooksForCheckIn(title);

        if (books == null) {
            showMessage("Title not found.");
            return;
        }

        if (books.isEmpty()) {
            showMessage("No books are available.");
            return;
        }

        int id;
        if (books.size() == 1) {
            id = books.iterator().next().barcode();
        }
        else
        {
            StringBuilder ids = new StringBuilder();
            for (Book book : books)
            {
                if (!ids.isEmpty()) {
                    ids.append(", ");
                }
                ids.append(book.barcode());
            }

            String input = JOptionPane.showInputDialog("Which book id [" + ids + "]: ");
            if (input == null) return;

            try {
                id = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                showMessage("Invalid Id.");
                return;
            }
        }

        if (catalog.checkIn(id) == null) {
            showMessage("Book " + id + " not found.");
            return;
        }

        showMessage("Book " + id + " checked in.");
    }

    private void showMessage(String message) {
        //textArea1.setText(message);
        JOptionPane.showMessageDialog(this, message);
        viewCatalogAction();
    }

    /**
     * Don't forget to add an Exit button so that users can leave the GUI when finished!
     */
    private void exitAction() {
        dispose();
    }
}
