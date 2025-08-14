package ru.otus.simplejunitdemo;

import ru.otus.simplejunit.util.TestRunner;

public class MainSimpleJunitDemo {
    public static void main(String[] args) {
        TestRunner.run(ExampleTest.class);
    }
}
