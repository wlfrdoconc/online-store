package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

// UPDATES TO COMPLETE:
// input try to pay again if insufficient
// formatting

public class Store {
    public static void main(String[] args) {

        // Project Requirements
        // 1. Create a command line store
        // 2. This CLI store lets users browse products loaded from products.csv
        // 3. It should let users search, add or remove them from a cart
        // 4. Home -> Display Products ->  Display Cart

        // What we will need
        // 1. BufferedReader to load products, BufferedWriter for the receipt
        // 2. ArrayList for inventory, HashMap for cart quantities (bonus)
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

        // Initialize inventory as a list
        ArrayList<Product> inventory = loadInventory("products.csv");
//
//        // OLD APPROACH: Initialize cart as a list
//        ArrayList<Product> cart = new ArrayList<>();

        // Initialize cart quantities
        // Switched over from ArrayList<Product> cart for easier reusability
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
                    System.out.println("\nThank you for shopping at Bay Bytes! :)\n");
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

        for (Product product : inventory) {
            System.out.println(product);
        }

        String enteredChoice = ""; // Empty string to update the enteredChoice of the user

        // Wrap in a while loop to ensure program keeps running until the user enters 3

        while (!enteredChoice.equals("3")) {
            // Menu that lets users choose to search products, add products, or go back
            System.out.println("\n========= PRODUCTS =========\n");
            System.out.print("1) Search for products\n2) Add a product\n3) Go back\n\nChoose an option: ");
            enteredChoice = scanner.nextLine().trim();

            switch (enteredChoice) {
                case "1":
                    searchProducts(inventory, scanner); // Choosing 1 opens the searchProducts sub-menu
                    break;
                case "2":
                    // addProduct() placeholder
                    addProduct(inventory, quantities, scanner);
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");

            }
        }
    }

    public static void displayCart(ArrayList<Product> inventory, Map<String, Integer> quantities, Scanner scanner) {
        // Shows every product in the cart and the total amount

        System.out.println("\n========= YOUR SHOPPING CART =========\n");

        // Initialize the running total of products in the cart
        int productCount = 0;
        // Increment running total based on the quantities in the cart
        for (int quantity : quantities.values()) {
            productCount += quantity;
        }
        // Display the total amount of products in the cart
        System.out.println("There are " + productCount + " items in your cart.\n");

        // Header for products line
        System.out.printf("%-12s %-30s %-5s %s\n",
                "SKU:",
                "Item:",
                "Qty:",
                "Price:");

        // Initialize total cost variable
        double totalCostOfCart = 0;

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
            totalCostOfCart += productTotalPrice;

            // Display each product with its name, quantity, and total price
            System.out.printf("%-12s %-30s %-6s $%.2f\n",
                    sku,
                    productName,
                    productQuantity + "x",
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
        System.out.println("----------------------------------------------------------------");
        System.out.printf("%-50s $%.2f\n",
                "Total:",
                totalCostOfCart);

        // Display new menu: checkout, remove product, or return to home page
        System.out.println("\n1) Checkout");
        System.out.println("2) Remove product");
        System.out.println("3) Return to home");
        System.out.print("Choose an option: ");

        // Read and handle the user's selection.
        String userChoice = scanner.nextLine().trim();

        switch (userChoice) {
            case "1":
                checkOut(inventory, quantities, scanner, totalCostOfCart);
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
    public static void checkOut(ArrayList<Product> inventory, Map<String, Integer> quantities, Scanner scanner, double totalCostOfCart) {

        System.out.println("\n========= CHECKOUT =========\n");

        // Display the total sales amount
        System.out.printf("%s $%.2f\n",
                "Your total is: ",
                totalCostOfCart);

        // Initialize the customer's payment amount
        double customerPaymentAmount = 0;

        // Ask for payment until sufficient amount given
        while (customerPaymentAmount < totalCostOfCart) {
            try {
                // Read and handle customer's payment amount
                System.out.println("Please enter cash payment amount: \n");
                customerPaymentAmount = scanner.nextDouble();
                scanner.nextLine();

                // Verify customer's payment amount
                if (customerPaymentAmount < totalCostOfCart) {
                    System.out.println("\nDECLINED: Your payment is insufficient\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Invalid payment amount. Please enter a number.");;
            }
        }

        // Success message when payment sufficient
        System.out.println("\nPayment accepted!\n");

        // Calculate change
        double changeOwed = customerPaymentAmount - totalCostOfCart;

        // Display calculated change
        System.out.printf("\nYour change is: $%.2f\n", changeOwed);
        System.out.println("\nThank you for shopping at Bay Bytes! :)\n");

        // Build the receipt text
        String receiptText = buildReceiptText(inventory, quantities, totalCostOfCart, customerPaymentAmount, changeOwed);

        // Display sales receipt
        displayReceipt(receiptText);

        // Create a receipt file and save to csv
        String savedReceiptFile = createReceiptFile(receiptText);

        // Empty the cart after checkout and receipt saved
        if (savedReceiptFile != null) {
            quantities.clear();
        }
    }

    // displayReceipt()
    public static void displayReceipt(String receiptText) {

        // OLD APPROACH:
//        // Loop through products purchased
//        for (Product product : cart) {
//            String productName = product.getName();
//            double productPrice = product.getPrice();
//
//            // Display each product purchased (from cart)
//            System.out.printf("%-30s $%.2f\n" ,
//                    productName,
//                    productPrice);
//        }

        System.out.println(receiptText);
    }

    public static String buildReceiptText(ArrayList<Product> inventory, Map<String, Integer> quantities, double totalCostOfCart, double customerPaymentAmount, double changeOwed) {

        // Initialize a list for each line in the receipt
        ArrayList<String> receiptLines = new ArrayList<>();

        // Add main header to receipt
        receiptLines.add("\n========= BAY BYTES RECEIPT =========\n");

        // Get and format order date
        LocalDateTime orderDate = LocalDateTime.now();
        String formattedOrderDate = orderDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"));

        // Add order date to receipt
        receiptLines.add("Order Date: " + formattedOrderDate + "\n");

        // Add header for products line to receipt
        receiptLines.add(String.format("%-30s %-5s %s\n",
                "Item:",
                "Qty:",
                "Price:"));

        // Displaying each product, line by line ---->
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

            // Add each product with its name, quantity, and total price to receipt
            receiptLines.add(String.format("%-30s %-6s $%.2f\n",
                    productName,
                    productQuantity + "x",
                    productTotalPrice));
        }

        receiptLines.add("----------------------------------------------------------------\n");

        // Add the sales total
        receiptLines.add(String.format("%-37s $%.2f\n",
                "Total:",
                totalCostOfCart));

        // Add the amount paid in cash
        receiptLines.add(String.format("%-37s $%.2f\n",
                "Cash Paid:",
                customerPaymentAmount));

        // Add the change given
        receiptLines.add(String.format("%-37s $%.2f\n",
                "Change:",
                changeOwed));

        receiptLines.add("\nThank you for shopping at Bay Bytes! :)\n");

        // Converting the receipt list into a string
        // Initialize the string
        String receiptText = "";

        // Loop through each item (line of text) in the list
        for (String line : receiptLines) {
            receiptText += line; // add the line of text to the string
        }

        // Return the receipt as a single string
        return receiptText;
    }

    // removeProduct()
    public static void removeProduct(ArrayList<Product> inventory, Map<String, Integer> quantities, Scanner scanner) {

        System.out.println("\n========= REMOVE PRODUCT =========\n");

        // Read and handle customer's selected product
        System.out.println("Please enter the product's SKU to remove from cart: ");
        String customerSku = scanner.nextLine().trim();

        // Find the product object based on SKU input
        Product product = findProductBySku(inventory, customerSku);
        String productSku = product.getSku();

        // Get the quantity amount of the product using the [value] from quantities map
        int productQuantity = quantities.get(productSku);

        // Decrease the quantity by one
        productQuantity--;

        // Check if the quantity reaches 0 -> then remove the product's sku from the quantities map
        if (productQuantity == 0) {
            quantities.remove(productSku);
        } else {
            // Update the quantities map with newly reduced quantity for that product sku (changed the [value])
            quantities.put(productSku, productQuantity);
        }

        // Display successful removal message
        System.out.println("\nOne " + product.getName() + " has been successfully removed from your cart!\n");
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
            System.out.println("\n========= SEARCH PRODUCTS =========\n");
            System.out.println("1) Search by name\n2) Search by price\n3) Search by department\n4) Go back\n\nChoose an option: ");
            enteredChoice = scanner.nextLine().trim();

            switch (enteredChoice) {
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
        System.out.print("Please enter the product name: ");
        String searchName = scanner.nextLine().trim();

        // Tracks if the product was found
        boolean found = false;

        // Loop through the inventory using an enhanced for loop
        for (Product product : inventory) {
            // Checks if the product name matches the search
            if (product.getName().toLowerCase().contains(searchName.toLowerCase())) { // Checks what a user searches for regardless of capitalization
                // Display matching products
                System.out.println(product);
                found = true;
            }
        }

        // Handles the case if the product name isn't found
        if (!found) {
            System.out.println("\nNo matching products found by that name.\n");
        }
    }

    // 5. filterByPrice()
    public static void filterByPrice(ArrayList<Product> inventory, Scanner scanner) {

        // Try/Catch to catch invalid input from the user

        try {
            // Use filterByPrice() to display results by price range
            // Ask the user for the minimum price of an item
            System.out.print("Please enter the minimum price of an item: ");
            double minimumPrice = Double.parseDouble(scanner.nextLine().trim());

            // Ask the user for the maximum price of an item
            System.out.print("Please enter the maximum price of an item: ");
            double maximumPrice = Double.parseDouble(scanner.nextLine().trim());

            // Tracks if the product was found
            boolean found = false;

            // Loop through the inventory using an enhanced for loop based on the price range given
            for (Product product : inventory) {
                // Checks for the price range of the items
                if (product.getPrice() >= minimumPrice && product.getPrice() <= maximumPrice) {
                    System.out.println(product);
                    found = true;
                }
            }

            // Handles the case if are no items in that price range
            if (!found) {
                System.out.println("No matching products found in that price range.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }

    // 6. filterByDepartment()
    public static void filterByDepartment(ArrayList<Product> inventory, Scanner scanner) {

        // Use filterByDepartment() to display results by available departments

        // Ask the user for the specific item departments
        System.out.print("Please enter the department: ");
        String searchDepartment = scanner.nextLine().trim(); // trim() removes trailing/leading spaces entered by user

        // Tracks if the product was found
        boolean found = false;

        // Loop through the inventory based on department
        for (Product product : inventory) {
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
    public static void addProduct(ArrayList<Product> inventory, Map<String, Integer> quantities, Scanner scanner) {

        // Ask the user to enter a product SKU
        System.out.print("Please enter a product SKU to add: ");
        String sku = scanner.nextLine().trim();

        // Find the matching product
        Product product = findProductBySku(inventory, sku);

        // If a match is found, add product to the cart
        if (product != null) {

            // Get the product SKU
            String productSku = product.getSku();

            // Add product or increase existing quantity by 1
            quantities.put(productSku, quantities.getOrDefault(productSku, 0) + 1); // getOrDefault means to get existing quantity for SKU

            System.out.println("\nOne " + product.getName() + " has been successfully added to your cart!\n");
        } else {
            System.out.println("Product not found.");
        }

    }

    // 8. createReceiptFile()
    public static String createReceiptFile(String receiptText) {

        // Generate the receipt filename using the current date and time
        String fileName = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd___HH:mm:ss")) + ".txt";

        // Create the Receipts folder if it doesn't exist
        new File("Receipts").mkdirs();

        // Write the receipt to a text file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Receipts/" + fileName))) {

            writer.write(receiptText);

            System.out.println("Receipt saved successfully: " + fileName);
            return fileName;

        } catch (IOException e) {
            System.out.println("Could not save the receipt: " + e.getMessage());
            return null;
        }
    }

    // 9. findProductBySku()
    public static Product findProductBySku(ArrayList<Product> inventory, String sku) {

        // Loop through the inventory
        for (Product product : inventory) {
            // Check if the SKU matches
            if (product.getSku().equalsIgnoreCase(sku)) {
                return product;
            }
        }

        // Return null if there is no product found
        return null;
    }
}
