package pl.ur8an5ky.livematch.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as auditable. Methods annotated with @Auditable are intercepted
 * by AuditAspect, which records execution metadata (method name, arguments,
 * execution time, outcome) into the audit_logs table.
 * The audit entry is written in a separate transaction so that failed business
 * operations are still logged.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Auditable {
}