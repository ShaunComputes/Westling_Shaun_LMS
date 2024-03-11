// Shaun Westling
// CEN3024C CRN24668
// 03/03/2024

package org.valencia.lms;

import java.util.Date;

public record Book(int id, String author, String title, Date dueDate) {
    public String toString() {
        return "id: " + id + "  author: " + author + "  title: " + title + " due date: " + dueDate;
    }
}
