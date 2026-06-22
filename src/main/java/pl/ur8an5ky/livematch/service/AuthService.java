package pl.ur8an5ky.livematch.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.ur8an5ky.livematch.domain.User;
import pl.ur8an5ky.livematch.dto.LoginRequest;
import pl.ur8an5ky.livematch.dto.LoginResponse;
import pl.ur8an5ky.livematch.repository.UserRepository;
import pl.ur8an5ky.livematch.security.JwtUtil;

/**
 * Handles user authentication: verifies credentials against the database
 * and issues a signed JWT token on success.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * Verifies the username and password, then returns a fresh JWT token
     * along with user metadata. Throws {@link BadCredentialsException} in both
     * "user not found" and "wrong password" cases to avoid leaking which one
     * actually failed.
     *
     * @param request login credentials
     * @return JWT token, username, role, and token lifetime
     * @throws BadCredentialsException if username does not exist or password does not match
     */
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String token = jwtUtil.generate(user.getUsername(), user.getRole().name());
        return new LoginResponse(token, user.getUsername(), user.getRole(), jwtUtil.getExpirationMs());
    }
}