package pl.ur8an5ky.livematch.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import pl.ur8an5ky.livematch.domain.AuditLog;
import pl.ur8an5ky.livematch.service.AuditLogService;

import java.util.Arrays;

/**
 * Aspect that intercepts every method annotated with @Auditable
 * and persists an entry to the audit_logs table. Captures:
 *   - fully qualified method name (Class.method)
 *   - serialized arguments
 *   - execution time in milliseconds
 *   - success/failure flag
 * On exception the audit entry is still written, then the exception is rethrown.
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    private static final int MAX_ARGS_LENGTH = 1000;

    private final AuditLogService auditLogService;

    /**
     * Around advice that wraps every {@code @Auditable} method invocation.
     * Measures execution time, captures arguments, and writes an audit entry
     * regardless of whether the target method succeeded or threw an exception.
     * Any failure to persist the audit entry is logged but swallowed,
     * so business logic is never affected by audit infrastructure errors.
     *
     * @param joinPoint AspectJ join point representing the intercepted method call
     * @return the original method's return value
     * @throws Throwable rethrows whatever the target method threw
     */
    @Around("@annotation(pl.ur8an5ky.livematch.aop.Auditable)")
    public Object audit(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = buildMethodName(joinPoint);
        String methodArgs = truncate(Arrays.toString(joinPoint.getArgs()));

        long startNs = System.nanoTime();
        boolean success = false;
        Object result = null;

        try {
            result = joinPoint.proceed();
            success = true;
            return result;
        } finally {
            long elapsedMs = (System.nanoTime() - startNs) / 1_000_000;
            try {
                AuditLog entry = AuditLog.builder()
                        .methodName(methodName)
                        .methodArgs(methodArgs)
                        .executionTimeMs(elapsedMs)
                        .success(success)
                        .build();
                auditLogService.save(entry);
            } catch (Exception ex) {
                // Never let an audit failure break business flow.
                log.error("Failed to persist audit log for {}", methodName, ex);
            }
        }
    }

    /**
     * Builds a short identifier of the intercepted method in the form {@code ClassName.methodName}.
     */
    private String buildMethodName(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getDeclaringType().getSimpleName()
                + "."
                + signature.getName();
    }

    /**
     * Truncates the given text to {@value #MAX_ARGS_LENGTH} characters
     * to prevent overly large method argument strings from bloating the audit table.
     */
    private String truncate(String text) {
        if (text == null) return null;
        return text.length() > MAX_ARGS_LENGTH
                ? text.substring(0, MAX_ARGS_LENGTH) + "...(truncated)"
                : text;
    }
}