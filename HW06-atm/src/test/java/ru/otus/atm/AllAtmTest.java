package ru.otus.atm;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * The class to run all ATM tests.
 */
@SelectPackages({
        "ru.otus.atm.builder",
        "ru.otus.atm.container"
})
@Suite
@SuiteDisplayName("All ATM Tests")
public class AllAtmTest {
}
