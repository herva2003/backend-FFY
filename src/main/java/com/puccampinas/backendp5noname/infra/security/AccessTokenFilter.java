package com.puccampinas.backendp5noname.infra.security;

import com.puccampinas.backendp5noname.domain.User;
import com.puccampinas.backendp5noname.services.UserService;
import com.puccampinas.backendp5noname.services.auth.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Log4j2
public class AccessTokenFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService jwtHelper;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Optional<String> accessToken = parseAccessToken(request);
            if (accessToken.isPresent() && jwtHelper.validateAccessToken(accessToken.get())) {
                String userId = jwtHelper.getUserIdFromAccessToken(accessToken.get());
                log.info("Token válido. userId extraído: {}", userId);
                User user = userService.findById(userId);
                if (user != null) {
                    log.info("Usuário encontrado: {}", user.getUsername());
                    UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(upat);
                } else {
                    log.warn("Usuário não encontrado para userId: {}", userId);
                }
            } else {
                log.warn("Token inválido ou ausente");
            }
        } catch (Exception e) {
            log.error("Não foi possível configurar a autenticação", e);
        }

        filterChain.doFilter(request, response);
    }

    private Optional<String> parseAccessToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return Optional.of(authHeader.replace("Bearer ", ""));
        }
        return Optional.empty();
    }
}