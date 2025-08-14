package ru.otus.simplejunit.demo;

import ru.otus.simplejunit.util.TestRunner;
import ru.otus.simplejunitdemo.ExampleTest;

public class MainSimpleJunitDemo {
    public static void main(String[] args) {
        TestRunner.run(ExampleTest.class);
    }
}
