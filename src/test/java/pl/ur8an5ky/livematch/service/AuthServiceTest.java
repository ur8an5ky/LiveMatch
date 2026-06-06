package pl.ur8an5ky.livematch.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.ur8an5ky.livematch.domain.Role;
import pl.ur8an5ky.livematch.domain.User;
import pl.ur8an5ky.livematch.dto.LoginRequest;
import pl.ur8an5ky.livematch.dto.LoginResponse;
import pl.ur8an5ky.livematch.repository.UserRepository;
import pl.ur8an5ky.livematch.security.JwtUtil;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtil jwtUtil;

    @InjectMocks private AuthService authService;

    @Test
    void shouldReturnTokenAndRole_whenCredentialsValid() {
        User user = User.builder()
                .username("admin").passwordHash("hashed").role(Role.ROLE_ADMIN)
                .build();
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "hashed")).thenReturn(true);
        when(jwtUtil.generate("admin", "ROLE_ADMIN")).thenReturn("eyJ.token.here");
        when(jwtUtil.getExpirationMs()).thenReturn(86400000L);

        LoginResponse response = authService.login(new LoginRequest("admin", "password"));

        assertThat(response.token()).isEqualTo("eyJ.token.here");
        assertThat(response.role()).isEqualTo(Role.ROLE_ADMIN);
        assertThat(response.username()).isEqualTo("admin");
    }

    @Test
    void shouldThrowBadCredentials_whenWrongPassword() {
        User user = User.builder()
                .username("admin").passwordHash("hashed").role(Role.ROLE_ADMIN).build();
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

        assertThatThrownBy(() ->
                authService.login(new LoginRequest("admin", "wrong")))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void shouldThrowBadCredentials_whenUserNotFound() {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                authService.login(new LoginRequest("ghost", "any")))
                .isInstanceOf(BadCredentialsException.class);
    }
}