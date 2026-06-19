package nl.davefemi.chess.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.chess.exception.InvalidTokenException;
import nl.davefemi.chess.session.service.CredentialService;
import nl.davefemi.chess.session.model.Player;
import nl.davefemi.chess.session.model.PlayerPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestInterceptor extends OncePerRequestFilter implements ChannelInterceptor {
    private final CredentialService credentialService;
    @Value("${external.websocket_id}")
    private String stompEndpoint;

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.endsWith("/games/invite")
                || path.endsWith("/games/join")
                || path.endsWith(stompEndpoint);
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing bearer token");
            return;
        }
        String token = authorization.substring("Bearer ".length());
        try {
            Player player = credentialService.authenticatePlayerToken(token);
            request.setAttribute("player", player);
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid player token");
        }
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) {
            return message;
        }
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authorization = accessor.getFirstNativeHeader("Authorization");
            String correlationId = accessor.getFirstNativeHeader("Correlation_id");
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                throw new IllegalArgumentException("Missing player token");
            }
            String token = authorization.substring("Bearer ".length());
            try {
                Player player = credentialService.authenticatePlayerToken(token);
                accessor.setUser(new PlayerPrincipal(player, correlationId));
            } catch (InvalidTokenException e) {
                throw new RuntimeException(e);
            }
        }
        return message;
    }
}
