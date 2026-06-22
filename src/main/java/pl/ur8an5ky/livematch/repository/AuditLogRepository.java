package pl.ur8an5ky.livematch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ur8an5ky.livematch.domain.AuditLog;

/**
 * JPA repository for {@link pl.ur8an5ky.livematch.domain.AuditLog} entries
 * persisted by {@link pl.ur8an5ky.livematch.aop.AuditAspect}.
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}