package pl.ur8an5ky.livematch.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.ur8an5ky.livematch.domain.AuditLog;
import pl.ur8an5ky.livematch.repository.AuditLogRepository;

/**
 * Service responsible for persisting audit log entries.
 * Uses REQUIRES_NEW propagation so that audit records survive a rollback
 * of the calling business transaction (we want failed operations logged too).
 */
@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(AuditLog entry) {
        auditLogRepository.save(entry);
    }
}