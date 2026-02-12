package simple.automation;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

/**
 * Records audit events with a minimal, structured format.
 */
public class AuditLogService1 {
    private final Logger1 logger;
    private final Clock clock;

    public AuditLogService1(Logger1 logger) {
        this(logger, Clock.systemUTC());
    }

    public AuditLogService1(Logger1 logger, Clock clock) {
        this.logger = Objects.requireNonNull(logger, "logger");
        this.clock = Objects.requireNonNull(clock, "clock");
    }

    public void recordEvent(String actorId, String action, String target, String metadata) {
        String event = formatEvent(actorId, action, target, metadata);
        logger.info(event);
    }

    public void recordEvent(String actorId, String action, String target) {
        recordEvent(actorId, action, target, "");
    }

    private String formatEvent(String actorId, String action, String target, String metadata) {
        String safeActor = sanitize(actorId);
        String safeAction = sanitize(action);
        String safeTarget = sanitize(target);
        String safeMetadata = sanitize(metadata);

        Instant now = Instant.now(clock);
        return String.format("eventTime=%s actor=%s action=%s target=%s meta=%s",
                now, safeActor, safeAction, safeTarget, safeMetadata);
    }

    private String sanitize(String value) {
        if (value == null || value.isBlank()) {
            return "-";
        }
        return value.replace('\n', ' ').replace('\r', ' ').trim();
    }
}
