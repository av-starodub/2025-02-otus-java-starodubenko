package ru.otus.simplejunit.cash;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.simplejunit.scenarios.LifeCycleTest;

@DisplayName("ResultContainerTest")
public class ResultsContainerTest {
    @Test
    @DisplayName("Simple name of test method as key in HashMap")
    public void checkInstantiateResultsContainer() {
        var mc = spy(new MethodsContainer(LifeCycleTest.class));
        var rc = new ResultsContainer(mc.getTest());
        assertTrue(rc.getResults().containsKey("firstTest"));
        assertTrue(rc.getResults().containsKey("secondTest"));
        assertEquals("TESTS - 2, PASSED - 0, FAILED - 0", rc.getStatistic());
    }
}
