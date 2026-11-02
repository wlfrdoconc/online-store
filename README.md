# Bay Bytes Online Store

A command-line store built in Java. Browse products loaded from a CSV file, search them, add them to a cart, and check out with a cash payment.

## Features

- Load products from `products.csv`
- Browse the full product list
- Search by name, price range, or department
- Add products to the cart by SKU (adding the same SKU again increases its quantity)
- View the cart with quantities and the total
- Remove one item at a time from the cart
- Check out with cash, get change back, and see a receipt

## Built With

- Java
- IntelliJ IDEA
- Git and GitHub

## Getting Started

1. Clone the repo:
   ```
   git clone https://github.com/wlfrdoconc/online-store
   ```
2. Open the project in IntelliJ.
3. Make sure `products.csv` is in the project root.
4. Run `Store.java`.

### `products.csv` format

Pipe-separated, with a header line first:

```
sku|name|price|department
```

## Usage

```
*~*~*~*~*~* WELCOME TO BAY BYTES *~*~*~*~*~*
1) Display Products
2) Display Cart
3) Exit
```

- **Display Products:** view the inventory, search it, or add a product to the cart by SKU.
- **Display Cart:** view your items and total, check out, or remove a product.




## Project Structure

```
src/main/java/com/pluralsight/
  Store.java     Menus, search, cart, and checkout logic
  Product.java   Product model (sku, name, price, department)
products.csv     Inventory data
```

