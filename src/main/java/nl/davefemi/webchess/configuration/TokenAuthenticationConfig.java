package nl.davefemi.webchess.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.webchess.service.TokenService;
import nl.davefemi.webchess.session.Player;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenAuthenticationConfig extends OncePerRequestFilter {
    private final TokenService tokenService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/games/invite")
                || path.startsWith("/games/join");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing bearer token");
            return;
        }
        String token = authorization.substring("Bearer ".length());
        try {
            Player player = tokenService.authenticatePlayerToken(token);
            request.setAttribute("player", player);
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid player token");
        }
    }
}
