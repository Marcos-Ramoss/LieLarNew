package com.lielar.application.services;

import com.lielar.domain.dto.LoginRequestDTO;
import com.lielar.domain.dto.LoginResponseDTO;
import com.lielar.domain.entities.User;
import com.lielar.infrastructure.mappers.UserMapper;
import com.lielar.infrastructure.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para UserService
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private LoginRequestDTO loginRequest;
    private LoginResponseDTO loginResponse;

    @BeforeEach
    void setUp() {
        // Configurar usuário de teste
        User.Permissoes permissoes = User.Permissoes.builder()
                .produtos(true)
                .categorias(true)
                .usuarios(true)
                .orcamentos(true)
                .relatorios(true)
                .build();

        testUser = User.builder()
                .id("1")
                .nome("Teste User")
                .email("teste@lie-lar.com")
                .senha("encodedPassword")
                .tipo("admin")
                .cargo("Administrador")
                .departamento("administrativo")
                .ativo(true)
                .permissoes(permissoes)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Configurar DTOs de teste
        loginRequest = LoginRequestDTO.builder()
                .email("teste@lie-lar.com")
                .senha("password123")
                .build();

        loginResponse = LoginResponseDTO.builder()
                .id("1")
                .nome("Teste User")
                .email("teste@lie-lar.com")
                .token("test_token")
                .build();
    }

    @Test
    void authenticate_WithValidCredentials_ShouldReturnLoginResponse() {
        // Arrange
        when(userRepository.findByEmailAndAtivoTrue(anyString())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userMapper.toLoginResponseDTO(any(User.class), anyString())).thenReturn(loginResponse);

        // Act
        Optional<LoginResponseDTO> result = userService.authenticate(loginRequest);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(loginResponse, result.get());
        verify(userRepository).save(any(User.class));
        verify(userMapper).toLoginResponseDTO(any(User.class), anyString());
    }

    @Test
    void authenticate_WithInvalidEmail_ShouldReturnEmpty() {
        // Arrange
        when(userRepository.findByEmailAndAtivoTrue(anyString())).thenReturn(Optional.empty());

        // Act
        Optional<LoginResponseDTO> result = userService.authenticate(loginRequest);

        // Assert
        assertFalse(result.isPresent());
        verify(userRepository, never()).save(any(User.class));
        verify(userMapper, never()).toLoginResponseDTO(any(User.class), anyString());
    }

    @Test
    void authenticate_WithInvalidPassword_ShouldReturnEmpty() {
        // Arrange
        when(userRepository.findByEmailAndAtivoTrue(anyString())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // Act
        Optional<LoginResponseDTO> result = userService.authenticate(loginRequest);

        // Assert
        assertFalse(result.isPresent());
        verify(userRepository, never()).save(any(User.class));
        verify(userMapper, never()).toLoginResponseDTO(any(User.class), anyString());
    }

    @Test
    void createDefaultAdminUser_WhenAdminDoesNotExist_ShouldCreateAdmin() {
        // Arrange
        when(userRepository.existsByEmail("admin@lie-lar.com")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        userService.createDefaultAdminUser();

        // Assert
        verify(userRepository).existsByEmail("admin@lie-lar.com");
        verify(passwordEncoder).encode("admin123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createDefaultAdminUser_WhenAdminExists_ShouldNotCreateAdmin() {
        // Arrange
        when(userRepository.existsByEmail("admin@lie-lar.com")).thenReturn(true);

        // Act
        userService.createDefaultAdminUser();

        // Assert
        verify(userRepository).existsByEmail("admin@lie-lar.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void isEmailInUse_ShouldReturnTrue_WhenEmailExists() {
        // Arrange
        when(userRepository.existsByEmail("teste@lie-lar.com")).thenReturn(true);

        // Act
        boolean result = userService.isEmailInUse("teste@lie-lar.com");

        // Assert
        assertTrue(result);
        verify(userRepository).existsByEmail("teste@lie-lar.com");
    }

    @Test
    void isEmailInUse_ShouldReturnFalse_WhenEmailDoesNotExist() {
        // Arrange
        when(userRepository.existsByEmail("teste@lie-lar.com")).thenReturn(false);

        // Act
        boolean result = userService.isEmailInUse("teste@lie-lar.com");

        // Assert
        assertFalse(result);
        verify(userRepository).existsByEmail("teste@lie-lar.com");
    }

    @Test
    void findByEmail_ShouldReturnUser_WhenUserExists() {
        // Arrange
        when(userRepository.findByEmail("teste@lie-lar.com")).thenReturn(Optional.of(testUser));

        // Act
        Optional<User> result = userService.findByEmail("teste@lie-lar.com");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
        verify(userRepository).findByEmail("teste@lie-lar.com");
    }

    @Test
    void findByEmail_ShouldReturnEmpty_WhenUserDoesNotExist() {
        // Arrange
        when(userRepository.findByEmail("teste@lie-lar.com")).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.findByEmail("teste@lie-lar.com");

        // Assert
        assertFalse(result.isPresent());
        verify(userRepository).findByEmail("teste@lie-lar.com");
    }
}
