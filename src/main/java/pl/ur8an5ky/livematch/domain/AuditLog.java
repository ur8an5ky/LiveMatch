package pl.ur8an5ky.livematch.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Audit log entry — automatically recorded by the AOP aspect
 * whenever a service method that adds or modifies data is called.
 * Implements the design requirement ‘event handling via aspect-oriented programming’.
 */
@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Full method name, e.g. ‘MatchEventService.addEvent’.
     */
    @Column(name = "method_name", nullable = false, length = 200)
    private String methodName;

    /**
     * The method's input arguments as text (toString).
     */
    @Column(name = "method_args", columnDefinition = "TEXT")
    private String methodArgs;

    /**
     * Execution time in milliseconds (for performance statistics).
     */
    @Column(name = "execution_time_ms")
    private Long executionTimeMs;

    /**
     * Whether the call was successful or resulted in an exception.
     */
    @Column(name = "success", nullable = false)
    private Boolean success;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @PrePersist
    public void prePersist() {
        if (timestamp == null) timestamp = LocalDateTime.now();
    }
}