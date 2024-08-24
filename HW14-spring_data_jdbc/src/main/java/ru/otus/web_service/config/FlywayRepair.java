package ru.otus.web_service.config;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FlywayRepair implements ApplicationListener<ContextClosedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(FlywayRepair.class);
    private final Flyway flyway;

    @Override
    public void onApplicationEvent(@NonNull ContextClosedEvent event) {
        try {
            logger.info("Flyway repairing start...");
            flyway.repair();
            logger.info("Flyway repairing completed successfully.");
        } catch (Exception e) {
            logger.error("Flyway repairing error", e);
        }
    }
}
