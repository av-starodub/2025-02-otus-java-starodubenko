package ru.otus.simplejunit.cash;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.simplejunit.scenarios.LifeCycleTest;

@DisplayName("MethodsContainerTest")
public class MethodsContainerTest {
    @Test
    @DisplayName("Must correctly extract the annotated methods of the class passed to the constructor")
    public void checkExtractingAllAnnotatedMethods() {
        var mc = new MethodsContainer(LifeCycleTest.class);
        var expectedTestAnnotated = new String[] {"firstTest", "secondTest"};
        var actualTestAnnotated = new ArrayList<>() {
            {
                mc.getTest().forEach(method -> add(method.getName()));
            }
        };
        assertEquals(Arrays.toString(expectedTestAnnotated), actualTestAnnotated.toString());
        assertEquals("setUp", mc.getBefore().get(0).getName());
        assertEquals("tearDown", mc.getAfter().get(0).getName());
    }
}
