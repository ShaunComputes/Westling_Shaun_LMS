// Shaun Westling
// CEN3024C CRN24668
// 01/28/2024

package org.example;

public record Book(int id, String author, String title) {
    public String toString() {
        return "id: " + id + "  author: " + author + "  title: " + title;
    }
}
