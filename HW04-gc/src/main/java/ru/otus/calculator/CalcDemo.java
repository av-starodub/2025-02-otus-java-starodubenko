package ru.otus.calculator;

/*
-Xms256m
-Xmx256m
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=./logs/heapdump.hprof
-XX:+UseG1GC
-Xlog:gc=debug:file=./logs/gc-%p-%t.log:tags,uptime,time,level:filecount=5,filesize=10m
*/

import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalcDemo {

    public static final Logger logger = LoggerFactory.getLogger(CalcDemo.class);

    public static void main(String[] args) {
        long counter = 100_000_000;
        var summator = new Summator();
        var startTime = System.currentTimeMillis();

        for (var idx = 0; idx < counter; idx++) {
            summator.calc(new Data(idx));

            if (idx % 10_000_000 == 0) {
                logger.info("{} current idx:{}", LocalDateTime.now(), idx);
            }
        }

        var durationInMillis = System.currentTimeMillis() - startTime;

        logger.info("prevValue: {}", summator.getPrevValue());
        logger.info("prevPrevValue: {}", summator.getPrevPrevValue());
        logger.info("sumLastThreeValues: {}", summator.getSumLastThreeValues());
        logger.info("someValue: {}", summator.getSomeValue());
        logger.info("sum: {}", summator.getSum());
        logger.info("spend msec: {}", durationInMillis);
        logger.info("spend sec: {}", (durationInMillis / 1000));
    }
}
