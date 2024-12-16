package mediHub_be.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.security.service.TokenService;
import mediHub_be.security.util.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = resolveToken(request);

        if (jwt != null) {
            log.info("Received JWT token: {}", jwt);
            if (isTokenBlacklisted(jwt, response)) {
                log.warn("Token is blacklisted: {}", jwt);
                return;
            }

            if (jwtUtil.validateToken(jwt)) {
                Authentication authentication = jwtUtil.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("Authentication successful for user: {}", authentication.getName());
            } else {
                log.warn("Invalid JWT token detected.");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }
        } else {
            log.info("No JWT token provided in request.");
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        log.debug("Authorization header: {}", bearerToken);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private boolean isTokenBlacklisted(String jwt, HttpServletResponse response) throws IOException {
        if (tokenService.isTokenBlacklisted(jwt)) {
            log.error("Blacklisted token detected: {}", jwt);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("INVALID_TOKEN");
            return true;
        }
        return false;
    }
}
