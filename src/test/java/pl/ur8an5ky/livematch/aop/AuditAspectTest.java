package pl.ur8an5ky.livematch.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.ur8an5ky.livematch.domain.AuditLog;
import pl.ur8an5ky.livematch.service.AuditLogService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditAspectTest {

    @Mock private AuditLogService auditLogService;
    @Mock private ProceedingJoinPoint joinPoint;
    @Mock private MethodSignature signature;

    @InjectMocks private AuditAspect auditAspect;

    @Test
    void shouldLogSuccess_whenMethodReturnsNormally() throws Throwable {
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getDeclaringType()).thenReturn(String.class);
        when(signature.getName()).thenReturn("someMethod");
        when(joinPoint.getArgs()).thenReturn(new Object[]{"arg1"});
        when(joinPoint.proceed()).thenReturn("result");

        Object result = auditAspect.audit(joinPoint);

        assertThat(result).isEqualTo("result");

        ArgumentCaptor<AuditLog> captor = ArgumentCaptor.forClass(AuditLog.class);
        verify(auditLogService).save(captor.capture());
        AuditLog logged = captor.getValue();
        assertThat(logged.getSuccess()).isTrue();
        assertThat(logged.getMethodName()).isEqualTo("String.someMethod");
        assertThat(logged.getExecutionTimeMs()).isNotNull();
    }

    @Test
    void shouldLogFailure_andRethrow_whenMethodThrows() throws Throwable {
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getDeclaringType()).thenReturn(String.class);
        when(signature.getName()).thenReturn("badMethod");
        when(joinPoint.getArgs()).thenReturn(new Object[]{});
        when(joinPoint.proceed()).thenThrow(new RuntimeException("boom"));

        assertThatThrownBy(() -> auditAspect.audit(joinPoint))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("boom");

        ArgumentCaptor<AuditLog> captor = ArgumentCaptor.forClass(AuditLog.class);
        verify(auditLogService).save(captor.capture());
        assertThat(captor.getValue().getSuccess()).isFalse();
    }
}