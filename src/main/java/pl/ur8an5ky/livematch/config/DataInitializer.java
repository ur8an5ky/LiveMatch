package pl.ur8an5ky.livematch.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.ur8an5ky.livematch.domain.Role;
import pl.ur8an5ky.livematch.domain.User;
import pl.ur8an5ky.livematch.repository.UserRepository;

/**
 * Creates a default admin account on first application startup.
 * Useful for development; in production an admin would be provisioned manually.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .passwordHash(passwordEncoder.encode("admin123"))
                    .role(Role.ROLE_ADMIN)
                    .build();
            userRepository.save(admin);
            log.info("Default admin user created (username=admin, password=admin123)");
        }
    }
}