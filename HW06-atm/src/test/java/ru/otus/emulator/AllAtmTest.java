package ru.otus.emulator;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * The class to run all ATM tests.
 */
@SelectPackages({
        "ru.otus.aemulator.builder",
        "ru.otus.emulator.container",
        "ru.otus.emulator.service",
        "ru.otus.emulator.atm"
})
@Suite
@SuiteDisplayName("All ATM Tests")
public class AllAtmTest {
}
