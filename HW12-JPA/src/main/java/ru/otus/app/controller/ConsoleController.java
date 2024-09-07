package ru.otus.app.controller;

import lombok.RequiredArgsConstructor;
import ru.otus.app.service.ClientService;
import ru.otus.app.service.ProductService;
import ru.otus.app.service.PurchaseService;

import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

@RequiredArgsConstructor
public class ConsoleController {
    private final ProductService productService;

    private final ClientService clientService;

    private final PurchaseService purchaseService;

    public void run() {
        var scanner = new Scanner(System.in);
        var exit = false;

        while (!exit) {
            System.out.print("""
                    1. Show all products
                    2. Show all clients
                    3. Show all purchases
                    4. Show all the goods that client bought
                    5. Show all clients who bought specific product
                    6. Delete product by ID
                    0. Log out
                    Select action and press enter:\040""");

            var choice = scanner.next();

            switch (choice) {
                case "1" -> {
                    System.out.println("All products:");
                    var products = productService.findAll();
                    if (products.isEmpty()) {
                        System.out.println("There are no products.");
                    } else {
                        products.forEach(System.out::println);
                    }
                }
                case "2" -> {
                    System.out.println("All clients:");
                    var clients = clientService.getAll();
                    if (clients.isEmpty()) {
                        System.out.println("There are no clients.");
                    } else {
                        clients.forEach(System.out::println);
                    }
                }
                case "3" -> {
                    System.out.println("All purchase:");
                    var purchases = purchaseService.getAll();
                    if (purchases.isEmpty()) {
                        System.out.println("There are no purchases.");
                    } else {
                        purchases.forEach(System.out::println);
                    }
                }
                case "4" -> {
                    System.out.print("Write client id and press enter:\040");
                    var optionalLong = getInputLong(scanner);
                    if (optionalLong.isEmpty()) {
                        scanner.next();
                        System.out.println("Invalid input. Try agan.");
                    } else {
                        var clientId = optionalLong.get();
                        var products = purchaseService.getPurchasedProductsByClientId(clientId);
                        if (products.isEmpty()) {
                            System.out.printf("Client with id=%s has no purchases", clientId);
                        } else {
                            System.out.printf("Client with id=%s purchases:%n", clientId);
                            products.forEach(System.out::println);
                        }
                    }
                }
                case "5" -> {
                    System.out.print("Write product id and press enter:\040");
                    var optionalLong = getInputLong(scanner);
                    if (optionalLong.isEmpty()) {
                        scanner.next();
                        System.out.println("Invalid input. Try agan.");
                    } else {
                        var productId = optionalLong.get();
                        var clients = purchaseService.getClientsByProductId(productId);
                        if (clients.isEmpty()) {
                            System.out.printf("Product with id-%s is not on the purchases%n", productId);
                        } else {
                            System.out.printf("Client who bought products with id=%s:%n", productId);
                            clients.forEach(System.out::println);
                        }
                    }
                }
                case "6" -> {
                    System.out.print("Write product id and press enter:\040");
                    var optionalId = getInputLong(scanner);
                    if (optionalId.isEmpty()) {
                        scanner.next();
                        System.out.println("Invalid input. Try agan.");
                    } else {
                        var productId = optionalId.get();
                        if (productService.deleteByIdIfExist(productId)) {
                            System.out.printf("Product with id=%s was successfully removed%n", productId);
                        } else {
                            System.out.printf("Product with id=%s does not exist or was deleted earlier.%n", productId);
                        }
                    }
                }
                case "0" -> exit = true;
                default -> System.out.printf("Action '%s' does not exist, please try again.%n", choice);
            }
        }
    }

    private static Optional<Long> getInputLong(Scanner scanner) {
        try {
            return Optional.of(scanner.nextLong());
        } catch (InputMismatchException e) {
            return Optional.empty();
        }
    }
}
