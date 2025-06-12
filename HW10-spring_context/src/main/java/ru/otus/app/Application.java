package ru.otus.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.otus.app.config.AppConfig;
import ru.otus.app.exception.ApplicationException;
import ru.otus.app.repository.Dao;
import ru.otus.app.runner.ApplicationRunner;

public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            var productRepository = context.getBean(Dao.class);
            productRepository.init();

            var runner = context.getBean(ApplicationRunner.class);
            runner.run();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ApplicationException(e.getMessage(), e);
        }
    }
}
