package com.lielar.infrastructure.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lielar.application.services.UserService;
import com.lielar.domain.dto.LoginRequestDTO;
import com.lielar.domain.dto.LoginResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes unitários para AuthController
 */
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void login_WithValidCredentials_ShouldReturnLoginResponse() throws Exception {
        // Arrange
        LoginRequestDTO loginRequest = LoginRequestDTO.builder()
                .email("teste@lie-lar.com")
                .senha("password123")
                .build();

        LoginResponseDTO loginResponse = LoginResponseDTO.builder()
                .id("1")
                .nome("Teste User")
                .email("teste@lie-lar.com")
                .token("test_token")
                .build();

        when(userService.authenticate(any(LoginRequestDTO.class)))
                .thenReturn(Optional.of(loginResponse));

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.email").value("teste@lie-lar.com"))
                .andExpect(jsonPath("$.token").value("test_token"));
    }

    @Test
    void login_WithInvalidCredentials_ShouldReturnUnauthorized() throws Exception {
        // Arrange
        LoginRequestDTO loginRequest = LoginRequestDTO.builder()
                .email("teste@lie-lar.com")
                .senha("wrongpassword")
                .build();

        when(userService.authenticate(any(LoginRequestDTO.class)))
                .thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Email ou senha inválidos"))
                .andExpect(jsonPath("$.error").value("UNAUTHORIZED"));
    }

    @Test
    void login_WithInvalidEmail_ShouldReturnBadRequest() throws Exception {
        // Arrange
        LoginRequestDTO loginRequest = LoginRequestDTO.builder()
                .email("invalid-email")
                .senha("password123")
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_WithEmptyPassword_ShouldReturnBadRequest() throws Exception {
        // Arrange
        LoginRequestDTO loginRequest = LoginRequestDTO.builder()
                .email("teste@lie-lar.com")
                .senha("")
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_WithShortPassword_ShouldReturnBadRequest() throws Exception {
        // Arrange
        LoginRequestDTO loginRequest = LoginRequestDTO.builder()
                .email("teste@lie-lar.com")
                .senha("123")
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void healthCheck_ShouldReturnOk() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/auth/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("Authentication Service"))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
