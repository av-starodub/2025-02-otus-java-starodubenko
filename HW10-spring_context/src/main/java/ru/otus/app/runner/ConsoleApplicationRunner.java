package ru.otus.app.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.otus.app.model.Product;
import ru.otus.app.service.CartService;

import java.util.List;
import java.util.Scanner;

@Controller
public class ConsoleApplicationRunner implements ApplicationRunner {

    private final CartService cartService;

    @Autowired
    public ConsoleApplicationRunner(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        var cart = cartService.createNewCart();

        System.out.println("\nWelcome!");

        while (!exit) {
            System.out.print("""
                                        
                    0. Show available products
                    1. Add product to cart
                    2. Remove product from cart
                    3. Show cart
                    4. Log out
                                        
                    Select action and press enter:\040""");

            String choice = scanner.next();

            switch (choice) {
                case "0" -> {
                    System.out.println("All available products:");
                    var allProducts = cartService.getAllProducts();
                    if (allProducts.isEmpty()) {
                        System.out.println("There are no products available.");
                    } else {
                        allProducts.forEach(System.out::println);
                    }
                }
                case "1" -> {
                    System.out.print("Enter the product ID to add: ");
                    long addId = scanner.nextLong();
                    Product addedProduct = cartService.add(addId, cart);
                    if (addedProduct != null) {
                        System.out.printf("Product added: %s\n", addedProduct);
                    } else {
                        System.out.printf("Failed to add product with ID %d.\n", addId);
                    }
                }
                case "2" -> {
                    System.out.print("Enter the product ID to delete: ");
                    long removeId = scanner.nextLong();
                    boolean removed = cartService.remove(removeId, cart);
                    if (removed) {
                        System.out.println("Product removed.\n");
                    } else {
                        System.out.printf("There is no product with ID %d in the cart.\n", removeId);
                    }
                }
                case "3" -> {
                    List<Product> productsInCart = cartService.showCart(cart);
                    if (productsInCart.isEmpty()) {
                        System.out.println("The cart is empty.\n");
                    } else {
                        System.out.println("Products in cart:");
                        productsInCart.forEach(System.out::println);
                    }
                }
                case "4" -> {
                    exit = true;
                    System.out.println("Thank you for visiting and we look forward to seeing you again!");
                }

                default -> System.out.printf("Action '%s' does not exist, please try again.\n", choice);
            }
        }
    }
}
