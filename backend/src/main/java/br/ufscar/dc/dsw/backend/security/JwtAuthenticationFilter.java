package br.ufscar.dc.dsw.backend.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService; // Bean do Spring Security que busca usuários

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain

    ) throws IOException, ServletException {
        String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

        jwt =  authHeader.substring(7);

        try{
            username = jwtService.extractUsername(jwt);
        } catch (ExpiredJwtException e) {
            logger.warn("JWT Token has expired: " +e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Carrega os detalhes do usuário do banco de dados usando o nome de usuário
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // 5. Valida se o token pertence a este usuário e se não está expirado
            if (jwtService.isTokenValid(jwt, userDetails)) {

                // 6. Se o token for válido, cria um objeto de autenticação
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,         // O objeto principal do usuário (Principal)
                        null,                // Credenciais (senha), não precisamos aqui
                        userDetails.getAuthorities() // As permissões/roles do usuário
                );

                // Adiciona detalhes da requisição web (IP, etc.) ao objeto de autenticação
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 7. ATUALIZA O CONTEXTO DE SEGURANÇA. Este é o passo que "autentica" o usuário para esta requisição.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 8. Passa a requisição para o próximo filtro na cadeia.
        filterChain.doFilter(request, response);

    }

}
