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
        // 8. createReceiptFile()

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
        // System.out.println("*~*~*~*~*~* Products *~*~*~*~*~*  ");
        for (Product product : inventory) {
            System.out.println(product);
        }

        String enteredChoice = ""; // Empty string to update the enteredChoice of the user

        // Wrap in a while loop to ensure program keeps running until the user enters 3

       while (!enteredChoice.equals("3")) {
           // Menu that lets users choose to search products, add products, or go back
           System.out.println("*~*~*~*~*~* Products *~*~*~*~*~*  ");
           System.out.print("1. Search for products\n2. Add a product\n3. Go back\n\nChoose an option: ");
           enteredChoice = scanner.nextLine().trim();

           switch (enteredChoice) {
               case "1":
                   searchProducts(inventory, scanner); // Choosing 1 opens the searchProducts sub-menu
                   return;
               case "2":
                   // addProduct() placeholder
           }

           // TODO: let the customer search or filter the list
           // TODO: let the customer add a product to the cart (by SKU)
           // TODO: let the customer go back to the home screen
       }


    }

    public static void displayCart(ArrayList<Product> cart, Scanner scanner) {
        System.out.println();
        System.out.println("=== Your Cart ===");
        // TODO: show every product in the cart and the total amount
        // TODO: let the customer check out, remove a product, or go back
    }

    // Will's Additional Methods

    // 3. searchProducts()
    public static void searchProducts(ArrayList<Product> inventory, Scanner scanner) {

        String enteredChoice = ""; // Empty string to store updates from the suer

        // Switch case logic that traverses through the menu
        // Encapsulates the filter methods here
        while (!enteredChoice.equals("4")) { // Keeps the search menu running until 4 is entered
            // Menu that lets the user filter products by name, price, and department
            // Keeps looping until the user goes back
            System.out.println("*~*~*~*~*~* Search Products *~*~*~*~*~*");
            System.out.println("1. Search by name\n2. Search by price\n3. Search by department\n4. Go back\n\nChoose an option: ");
            enteredChoice = scanner.nextLine().trim();

            switch(enteredChoice) {
                case "1":
                    filterByName(inventory, scanner);
                    break;
                case "2":
                    filterByPrice(inventory, scanner);
                    break;
                case "3":
                    filterByDepartment(inventory, scanner);
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
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
        }

        // Handles the case if the department entered aren't there
        if (!found) {
            System.out.println("No matching products found in that department.");
        }
    }

    // 5. filterByPrice()
    public static void filterByPrice(ArrayList<Product> inventory, Scanner scanner) {

        // Use filterByPrice() to display results by price range

        // Ask the user for the minimum price of an item
        System.out.print("Enter the minimum price of an item: ");
        double minimumPrice = scanner.nextDouble();

        // Ask the user for the maximum price of an item
        System.out.println("Enter the maximum price of an item: ");
        double maximumPrice = scanner.nextDouble();

        // Tracks if the product was found
        boolean found = false;

        // Loop through the inventory using an enhanced for loop based on the price range given
        for (Product product: inventory) {
            // Checks for the price range of the items
            if (product.getPrice() >= minimumPrice && product.getPrice() <= maximumPrice) {
                System.out.println(product);
            }
        }

        // Handles the case if the department entered aren't there
        if (!found) {
            System.out.println("No matching products found in that department.");
        }
    }

    // 6. filterByDepartment()
    public static void filterByDepartment(ArrayList<Product> inventory, Scanner scanner) {

        // Use filterByDepartment() to display results by available departments

        // Ask the user for the specific item departments
        System.out.print("Enter the department: ");
        String searchDepartment = scanner.nextLine().trim(); // trim() removes trailing/leading spaces entered by user

        // Tracks if the product was found
        boolean found = false;

        // Loop through the inventory based on department
        for (Product product: inventory) {
            // Checks if the department is there
            if (product.getDepartment().equalsIgnoreCase(searchDepartment)) {
                System.out.println(product);
                found = true;
            }
        }

        // Handles the case if the department entered aren't there
        if (!found) {
            System.out.println("No matching products found in that department.");
        }
    }

    // 7. addProduct()
    public static void addProduct(ArrayList<Product> inventory, ArrayList<Product> cart, Scanner scanner) {

        // Ask the user to enter a product SKU

        // Loop through the inventory

        // Use if statement to compare SKU with product.getSku()

        // If a match is found, add that Product object to the cart

        // Print confirmation message

        // If no match is found, display an error message

    }

    // 8. createReceiptFile()
    public static void createReceiptFile() {

    }

    // 9. findProductBySku()
    public static Product findProductBySku(ArrayList<Product> inventory, String sku) {

        // Loop through the inventory
        for (Product product: inventory) {
            // Check if the SKU matches
            if (product.getSku().equalsIgnoreCase(sku)) {
                return product;
            }
        }

        // Return null if there is no product found
        return null;
    }

}