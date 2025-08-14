package ru.otus.aop.proxy.handler;

import static org.assertj.core.api.Assertions.assertThat;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import ru.otus.aop.calculator.Calculator;
import ru.otus.aop.calculator.operations.Addition;
import ru.otus.aop.proxy.ioc.Ioc;

class LogMethodParametersHandlerTest {

    private Calculator calculator;

    private Logger handlerLogger;

    private ListAppender<ILoggingEvent> listAppender;

    @BeforeEach
    void setUp() {
        calculator = Ioc.createLoggedCalculator(new LogMethodParametersHandler<>(new Addition()));
        handlerLogger = (Logger) LoggerFactory.getLogger(LogMethodParametersHandler.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        handlerLogger.addAppender(listAppender);
    }

    @AfterEach
    void tearDown() {
        handlerLogger.detachAppender(listAppender);
        listAppender.stop();
    }

    @Test
    @DisplayName("Should not log method without @Log annotation")
    void checkNotLogAnnotatedMethod() {
        var result = calculator.calculate(1);
        assertThat(result).isEqualTo(1);

        assertThat(listAppender.list).isEmpty();
    }

    @Test
    @DisplayName("Should log method correctly when params types are same")
    void checkLoggingWithSameParamsTypes() {
        var result = calculator.calculate(1, 2);
        assertThat(result).isEqualTo(3);

        assertThat(listAppender.list).hasSize(1);

        var event = listAppender.list.getFirst();
        assertThat(event.getLevel()).isEqualTo(Level.INFO);
        assertThat(event.getFormattedMessage()).isEqualTo("executed method: calculate, param0=1 param1=2");
    }

    @Test
    @DisplayName("Should log method correctly when params types are different")
    void checkLoggingWithDifferentParamsTypes() {
        var result = calculator.calculate(2, 3, "km/h");
        assertThat(result).isEqualTo("5km/h");

        assertThat(listAppender.list).hasSize(1);

        var event = listAppender.list.getFirst();
        assertThat(event.getLevel()).isEqualTo(Level.INFO);
        assertThat(event.getFormattedMessage()).isEqualTo("executed method: calculate, param0=2 param1=3 param2=km/h");
    }
}
