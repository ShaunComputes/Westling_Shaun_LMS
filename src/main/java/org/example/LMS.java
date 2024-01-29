// Shaun Westling
// CEN3024C CRN24668
// 01/28/2024

package org.example;

import java.util.Scanner;

public class LMS {
    public static void main(String[] args)
    {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        Collection collection = new Collection();

        Scanner in = new Scanner(System.in);
        int option;

        while(true) {
            try {
                System.out.println();
                System.out.println("Available commands:");
                System.out.println("0 Exit");
                System.out.println("1 Import books from \"import.csv\".");
                System.out.println("2 Remove a book.");
                System.out.println("3 List books.");
                System.out.println();
                System.out.print("Choose a command: ");
                option = in.nextInt();
            }
            catch(Exception ignored) {
                in.next(); // Eat the invalid input
                System.out.println("Invalid input.");
                continue;
            }

            switch (option) {
                case 0:
                    return;

                case 1:
                    // TODO: Allow user to enter a file name, default to import.csv
                    int imported = collection.addBooksFromFile("import.csv");
                    System.out.println("Imported " + imported + " books.");
                    break;

                case 2:
                    int id;
                    while(true) {
                        try {
                            System.out.print("Enter the book id: ");
                            id = in.nextInt();
                        }
                        catch(Exception ignored) {
                            in.next(); // Eat the invalid input
                            System.out.println("Invalid id.");
                            continue;
                        }

                        try {
                            if (collection.removeBook(id)) {
                                System.out.println("Book removed from collection.");
                            }
                            else {
                                System.out.println("Book not in collection.");
                            }
                            break;
                        }
                        catch(Exception ignored) {
                            System.out.println("Invalid id.");
                        }
                    }
                    break;

                case 3:
                    collection.listBooks();
                    break;

                default:
                    System.out.println("Invalid command.");
                    break;
            }
        }
    }
}