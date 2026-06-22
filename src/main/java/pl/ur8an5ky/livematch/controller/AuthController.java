package pl.ur8an5ky.livematch.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.ur8an5ky.livematch.dto.LoginRequest;
import pl.ur8an5ky.livematch.dto.LoginResponse;
import pl.ur8an5ky.livematch.service.AuthService;

/**
 * REST controller for authentication endpoints.
 * Exposes the public login endpoint used by the SPA client to obtain a JWT token.
 * All other authentication-related concerns (token validation, role checks) are
 * handled transparently by the security filter chain.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Authenticates a user with username and password.
     * On success, returns a signed JWT token along with user metadata
     * (username, role, token expiration in milliseconds).
     *
     * @param request login credentials (validated)
     * @return JWT token and user metadata
     * @throws org.springframework.security.authentication.BadCredentialsException
     *         if the username or password is invalid
     */
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}