package ru.otus.atmemulator;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * The class to run all ATM tests.
 */
@SelectPackages({
        "ru.otus.atmemulator.builder",
        "ru.otus.atmemulator.container",
        "ru.otus.atmemulator.service",
        "ru.otus.atmemulator.atm"
})
@Suite
@SuiteDisplayName("All ATM Tests")
public class AllAtmTest {
}
