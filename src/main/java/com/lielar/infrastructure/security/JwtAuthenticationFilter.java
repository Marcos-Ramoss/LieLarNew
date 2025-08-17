package com.lielar.infrastructure.security;

import com.lielar.application.services.JwtService;
import com.lielar.application.services.UserService;
import com.lielar.domain.entities.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro JWT para autenticação automática
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        
        try {
            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            final String userEmail;
            
            log.debug("🔍 Processando requisição: {} {} - Authorization: {}", 
                    request.getMethod(), request.getRequestURI(), 
                    authHeader != null ? (authHeader.length() > 20 ? authHeader.substring(0, 20) + "..." : authHeader) : "null");

            // Verifica se o header Authorization existe e começa com "Bearer "
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            // Extrai o token JWT e remove espaços em branco
            jwt = authHeader.substring(7).trim();
            
            // Valida se o token não está vazio
            if (jwt.isEmpty()) {
                log.warn("Token JWT vazio após extração");
                filterChain.doFilter(request, response);
                return;
            }
            
            try {
                userEmail = jwtService.extractEmail(jwt);
            } catch (Exception e) {
                log.warn("Erro ao extrair email do JWT: {}", e.getMessage());
                filterChain.doFilter(request, response);
                return;
            }

            // Se o email foi extraído e não há autenticação no contexto
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                log.debug("Processando JWT para usuário: {}", userEmail);
                
                // Busca o usuário no banco
                var userOpt = userService.findByEmail(userEmail);
                
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    
                    // Valida o token especificamente para este usuário
                    if (jwtService.isTokenValid(jwt, user)) {
                        // Cria UserDetails para Spring Security
                        UserDetails userDetails = createUserDetails(user);
                        
                        // Cria token de autenticação
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                        
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        
                        log.info("✅ Usuário autenticado via JWT: {} com roles: {}", userEmail, userDetails.getAuthorities());
                    } else {
                        log.warn("❌ Token JWT inválido para usuário: {}", userEmail);
                    }
                } else {
                    log.warn("❌ Usuário não encontrado para email: {}", userEmail);
                }
            } else if (authHeader != null && authHeader.startsWith("Bearer ")) {
                log.debug("JWT já processado ou usuário já autenticado para: {}", userEmail);
            }
            
        } catch (Exception e) {
            log.warn("Erro durante processamento do JWT: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Cria UserDetails a partir da entidade User
     */
    private UserDetails createUserDetails(User user) {
        String role = "ROLE_" + user.getTipo().toUpperCase();
        log.debug("Criando UserDetails para usuário: {} com role: {} e status ativo: {}", 
                user.getEmail(), role, user.getAtivo());
        
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password("") // Senha não é necessária para JWT
                .authorities(role)
                .accountExpired(!user.getAtivo())
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!user.getAtivo())
                .build();
    }
}
