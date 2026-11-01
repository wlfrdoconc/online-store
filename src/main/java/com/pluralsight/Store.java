package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

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
        // 9. createReceipt()

        // Initialize inventory as a list
        ArrayList<Product> inventory = loadInventory("products.csv");
//
//        // OLD APPROACH: Initialize cart as a list
//        ArrayList<Product> cart = new ArrayList<>();

        // Initialize product quantities in cart as a hashmap (key: SKU, value: quantity)
        Map<String, Integer> quantities = new HashMap<>();

        // Initialize scanner
        Scanner scanner = new Scanner(System.in);

        // Display main menu
        homeScreen(inventory, quantities, scanner);
    }

    // homeScreen() Method: display menu, read user selection, call the right method
    public static void homeScreen(ArrayList<Product> inventory, Map<String, Integer> quantities, Scanner scanner) {

        // Initialize user selection.
        String userChoice = "";

        // Display menu options until the user selects Exit.
        while (!userChoice.equals("3")) {
            System.out.println();
            System.out.println("*~*~*~*~*~* WELCOME TO BAY BYTES *~*~*~*~*~*");
            System.out.println("1) Display Products");
            System.out.println("2) Display Cart");
            System.out.println("3) Exit");
            System.out.print("Choose an option: ");

            // Read the user's selection.
            userChoice = scanner.nextLine().trim();

            // Call the correct method based on user's menu selection.
            switch (userChoice) {
                case "1":
                    displayProducts(inventory, quantities, scanner);
                    break;
                case "2":
                    displayCart(inventory, quantities, scanner);
                    break;
                case "3":
                    System.out.println("Thank you for shopping at Bay Bytes! :)");
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
    public static void displayProducts(ArrayList<Product> inventory, Map<String, Integer> quantities, Scanner scanner) {
        System.out.println();
        System.out.println("=== Products ===");
        for (Product product : inventory) {
            System.out.println(product);
        }
        // TODO: let the customer search or filter the list
        // TODO: let the customer add a product to the cart (by SKU)
        // TODO: let the customer go back to the home screen
    }

    public static void displayCart(ArrayList<Product> inventory, Map<String, Integer> quantities, Scanner scanner) {
        // Shows every product in the cart and the total amount
        // Lets the customer check out, remove a product, or go back

        System.out.println("\n========= YOUR SHOPPING CART =========\n");

        // Initialize the running total of products in the cart
        int productCount = 0;
        // Increment running total based on the quantities in the cart
        for (int quantity : quantities.values()) {
            productCount += quantity;
        }
        // Display the total amount of products in the cart
        System.out.println("There are " + productCount + " items in your cart.\n");

        // Initialize total cost variable
        double totalCostofCart = 0;

        // Loop through each SKU in the quantities map
        for (String sku : quantities.keySet()) {
            // Get the product object from inventory based on the SKU
            Product product = findProductBySku(inventory, sku);

            // Get the product name, price, and quantity
            String productName = product.getName();
            double productPrice = product.getPrice();
            int productQuantity = quantities.get(sku); // Quantity is the [value] of the SKU [key]

            // Calculate the product's total price based on its quantity amount
            double productTotalPrice = productPrice * productQuantity;

            // Increment the total cost of ALL the products in the cart
            totalCostofCart += productTotalPrice;

            // Display each product with its name, quantity, and total price
            System.out.printf("%-30s %x-3d $%.2f\n",
                    productName,
                    productQuantity,
                    productTotalPrice);
        }

        // OLD APPROACH:
//        // Loop through each product in the cart
//        for (Product product : cart) {
//            String productName = product.getName();
//            double productPrice = product.getPrice();
//
//            // Update total cost of ALL the products in the cart
//            totalCost += productPrice;
//
//            // Display all products in the cart
//            System.out.printf("%-30s $%.2f\n" ,
//                    productName,
//                    productPrice);
//        }

        // Display the total cost of the products in the cart
        System.out.println("------------------------------------");
        System.out.printf("%-30s $%.2f\n",
                "Total:",
                totalCostofCart);

        // Display new menu: checkout, remove product, or return to home page
        System.out.println("1) Checkout");
        System.out.println("2) Remove product");
        System.out.println("3) Return to home");
        System.out.print("Choose an option: ");

        // Read and handle the user's selection.
        String userChoice = scanner.nextLine().trim();

        switch (userChoice) {
            case "1":
                checkOut(inventory, quantities, scanner, totalCostofCart);
                break;
            case "2":
                removeProduct(inventory, quantities, scanner);
                break;
            case "3":
                break;
            default:
                System.out.println("Please enter number 1, 2 or 3.");
        }
    }


    // Sama's Additional Methods

    // checkOut()
    public static void checkOut(ArrayList<Product> inventory, Map<String, Integer> quantities, Scanner scanner, double totalCost) {

        System.out.println("\n========= CHECKOUT =========\n");

        // Display the total sales amount
        System.out.printf("%-30s $%.2f\n",
                "Your total is: ",
                totalCost);

        // Read and handle customer's payment amount
        System.out.println("Enter cash payment amount: \n");
        double customerPaymentAmount = scanner.nextDouble();
        scanner.nextLine();

        // Verify customer's payment amount
        if (customerPaymentAmount >= totalCost) {
            System.out.println("Payment accepted!");
            // Calculate change
            double changeOwed = customerPaymentAmount - totalCost;
            // Display calculated change
            System.out.printf("Your change is: $%.2f\n", changeOwed);
            System.out.println("Thank you for shopping at Bay Bytes! :)");
            // Display sales receipt
            displayReceipt(inventory, totalCost, customerPaymentAmount, changeOwed);
//            cart.clear(); ?????
        }
        else {
            System.out.println("DECLINED: Your payment is insufficient");
        }
    }

    // displayReceipt()
    public static void displayReceipt(Map<String, Integer> quantities, double totalCost, double customerPaymentAmount, double changeOwed ) {

        // Retrieve and display order date
        LocalDateTime orderDate = LocalDateTime.now();
        String formattedOrderDate = orderDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm"));
        System.out.printf("Order Date: %s\n", formattedOrderDate);

        // Display all line items purchased

        // Header for line items
        System.out.printf("%-30s %s\n",
                "Item:",
                "Price:");

        // Loop through products purchased (from cart)
        for (Product product : cart) {
            String productName = product.getName();
            double productPrice = product.getPrice();

            // Display each product purchased (from cart)
            System.out.printf("%-30s $%.2f\n" ,
                    productName,
                    productPrice);
        }

        System.out.println("---------------------------------");

        // Display the sales total
        System.out.printf("%-30s $%.2f\n",
                "Total:",
                totalCost);

        // Display the amount paid in cash
        System.out.printf("%-30s $%.2f\n",
                "Cash Paid:",
                customerPaymentAmount);

        // Display the change given
        System.out.printf("%-30s $%.2f\n",
                "Change:",
                changeOwed);
    }

    // removeProduct()
    public static void removeProduct(Map<String, Integer> quantities, Scanner scanner) {

        System.out.println("Please enter the product's SKU to remove from cart: ");

    }

    // FANA !!!!!!! ----======================================
    // REMOVE EVENTUALLY
    public static Product findProductBySku(ArrayList<Product> inventory, String sku)
    // takes all the products and give us one product
    {
        for (Product p : inventory)// to go through every product in the inventory
        {
            if (p.getSku().equalsIgnoreCase(sku))// to check the product sku matches with what we are looking for
            {
                return p;// to show that match is found by the sku
            }
        }
        return null; // only if the sku doesnt exist
    }

    // Will's Additional Methods

    // 3. searchProducts()
    public static void searchProducts() {

    }

    // 4. filterByName()
    public static void filterByName() {

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