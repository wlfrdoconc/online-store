package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Store {
    public static void main(String[] args) {

        // Project Requirements
        // 1. Create a command line store
        // 2. This CLI store lets users browse products loaded from products.csv
        // 3. It should let users search, add or remove them from a cart
        // 4. Home -> Display Products ->  Display Cart

        // What we will need
        // 1. BufferedReader to load products, BufferedWriter for the receipt
        // 2. ArrayList for inventory and cart, HashMap for quantities (bonus)
        // 3. LocalDateTime for the order date and receipt file name
        // 4. User stories on a GitHub project board, one branch per feature

        // Done means
        // 1. All requirements work, code compiles, and run without errors
        // 2. A public repo with a README, screenshots, and one interesting piece of code

        // Will's Methods
        // 1. loadInventory()
        // 2. displayProducts()
        // 3. searchProducts()
        // 4. filterByName()
        // 5. filterByPrice()
        // 6. filterByDepartment()
        // 7. addProduct()
        // 9. createReec

        // Initialize inventory
        ArrayList<Product> inventory = loadInventory("products.csv");

        // Initialize cart
        ArrayList<Product> cart = new ArrayList<>();

        // Initialize scanner
        Scanner scanner = new Scanner(System.in);

        // Display main menu
        homeScreen(inventory, cart, scanner);
    }

    // homeScreen() Method: display menu, read user selection, call the right method
    public static void homeScreen(ArrayList<Product> inventory, ArrayList<Product> cart, Scanner scanner) {

        // Initialize user selection.
        String userChoice = "";

        // Display menu options until the user selects Exit.
        while (!userChoice.equals("3")) {
            System.out.println();
            System.out.println("*~*~*~*~*~* Welcome to Bay Bytes *~*~*~*~*~*");
            System.out.println("1) Display Products");
            System.out.println("2) Display Cart");
            System.out.println("3) Exit");
            System.out.print("Choose an option: ");

            // Read the user's selection.
            userChoice = scanner.nextLine().trim();

            // Call the correct method based on user's menu selection.
            switch (userChoice) {
                case "1":
                    displayProducts(inventory, cart, scanner);
                    break;
                case "2":
                    displayCart(cart, scanner);
                    break;
                case "3":
                    System.out.println("Thank you for shopping with us! :)");
                    break;
                default:
                    System.out.println("Please enter number 1, 2 or 3.");
            }
        }
    }

    // 1. loadInventory()
    public static ArrayList<Product> loadInventory(String fileName) {
        ArrayList<Product> inventory = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            reader.readLine(); // skip the header line
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                String sku = parts[0];
                String name = parts[1];
                double price = Double.parseDouble(parts[2]);
                String department = parts[3];
                inventory.add(new Product(sku, name, price, department));
            }
        } catch (IOException e) {
            System.out.println("Could not load " + fileName + ": " + e.getMessage());
        }
        return inventory;
    }

    // 2. displayProducts()
    public static void displayProducts(ArrayList<Product> inventory, ArrayList<Product> cart, Scanner scanner) {
        System.out.println();
        System.out.println("=== Products ===");
        for (Product product : inventory) {
            System.out.println(product);
        }
        // TODO: let the customer search or filter the list
        // TODO: let the customer add a product to the cart (by SKU)
        // TODO: let the customer go back to the home screen
    }

    public static void displayCart(ArrayList<Product> cart, Scanner scanner) {
        System.out.println();
        System.out.println("=== Your Cart ===");
        // TODO: show every product in the cart and the total amount
        // TODO: let the customer check out, remove a product, or go back
    }

    // Will's Additional Methods

    // 3. searchProducts()
    public static void searchProducts() {

    }

    // 4. filterByName()
    public static void filterByName(ArrayList<Product> inventory, Scanner scanner) {

        // Ask the user for the product name
        System.out.print("Enter the product name: ");
        String searchName = scanner.nextLine().trim();

        // Tracks if the product was found
        boolean found = false;

        // Loop through the inventory using an enhanced for loop
        for (Product product: inventory) {

            // Checks if the product name matches the search
            if (product.getName().toLowerCase().contains(searchName.toLowerCase())) { // Checks what a user searches for regardless of capitalization
                // Display matching products
                System.out.println(product);
                found = true;
            }

            // Handles the case in where no searched items are found
            if (!found) {
                System.out.println("No matching products found.");
            }
        }
    }

    // 5. filterByPrice()
    public static void filterByPrice() {

    }

    // 6. filterByDepartment()
    public static void filterByDepartment() {

    }

    // 7. addProduct()
    public static void addProduct() {

    }

    // 8. createReceiptFile()
    public static void createReceiptFile() {

    }

}