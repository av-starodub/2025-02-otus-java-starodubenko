package ru.otus.app.controller;

import lombok.RequiredArgsConstructor;
import ru.otus.app.service.ProductService;

import java.util.Scanner;

@RequiredArgsConstructor
public class ConsoleController {
    private final ProductService productService;

    public void run() {
        var scanner = new Scanner(System.in);
        var exit = false;

        while (!exit) {
            System.out.print("""
                    0. Show available products
                    7. Log out
                    Select action and press enter:\040""");

            var choice = scanner.next();

            switch (choice) {
                case "0" -> {
                    System.out.println("All products:");
                    var products = productService.findAll();
                    if (products.isEmpty()) {
                        System.out.println("There are no products available.");
                    } else {
                        products.forEach(System.out::println);
                    }
                }
                case "7" -> exit = true;

                default -> System.out.printf("Action '%s' does not exist, please try again.\n", choice);
            }
        }
    }
}
