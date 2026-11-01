package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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

        ArrayList<Product> inventory = loadInventory("products.csv");
        ArrayList<Product> cart = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        String choice = "";
        while (!choice.equals("3")) {
            System.out.println();
            System.out.println("=== Welcome to the Online Store ===");
            System.out.println("1) Display Products");
            System.out.println("2) Display Cart");
            System.out.println("3) Exit");
            System.out.print("Choose an option: ");
            choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    displayProducts(inventory, cart, scanner);
                    break;
                case "2":
                    displayCart(cart, scanner);
                    break;
                case "3":
                    System.out.println("Thank you for shopping with us!");
                    break;
                default:
                    System.out.println("Please enter 1, 2 or 3.");
            }
        }
    }

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

    public static void showProductQuantityInCart(HashMap<String, Integer> quantities, ArrayList<Product> inventory)// prints the cart'total
    {
        for (String sku : quantities.keySet()) {
            Product eachProduct = findProductBySku(inventory, sku);
            // uses the method above to get full product info
            int quantityOfItemsAddedToTheCart = quantities.get(sku);
            String productName=eachProduct.getName();
            //gets how many of sku are in the cart
            System.out.println("quantiyOfItemsAddedToTheCart " + productName  + "is" + quantityOfItemsAddedToTheCart);
            //prints the name quantity and

        }


    }
}
