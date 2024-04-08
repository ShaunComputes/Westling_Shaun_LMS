// Shaun Westling
// CEN3024C CRN24668
// 03/03/2024

package org.valencia.lms.core;

import java.sql.Date;

public record Book(int barcode, String author, String title, String genre, Date dueDate) {
    public String toString() {
        return "barcode: " + barcode + "  author: " + author + "  title: " + title + " genre: " + genre + " due date: " + dueDate;
    }
}
