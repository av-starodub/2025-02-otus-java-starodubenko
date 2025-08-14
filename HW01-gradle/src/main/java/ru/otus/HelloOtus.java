package ru.otus;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloOtus {

    private static final Logger logger = LoggerFactory.getLogger(HelloOtus.class);

    public static void main(String[] args) {
        var timer = Stopwatch.createStarted();

        var greetings = new ArrayList<String>();
        for (int i = 0; i < 100_000_000; i++) {
            greetings.add("Hello Otus!");
        }

        Lists.reverse(greetings);

        timer.stop();

        logger.info("Running time: {}ms.", timer.elapsed(TimeUnit.MILLISECONDS));
    }
}
