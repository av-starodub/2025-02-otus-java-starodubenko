package ru.otus.web_service.config;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DataBaseCleaner implements ApplicationListener<ContextClosedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataBaseCleaner.class);

    private final Flyway flyway;

    @Override
    public void onApplicationEvent(@NonNull ContextClosedEvent event) {
        try {
            LOGGER.info("Flyway clean start...");
            flyway.clean();
            LOGGER.info("Flyway clean data base completed successfully.");
        } catch (Exception e) {
            LOGGER.error("Flyway clean error", e);
        }
    }
}
