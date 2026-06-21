package nl.davefemi.chess.web.restcontroller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.chess.exception.*;
import nl.davefemi.chess.web.dto.response.session.AcceptedSessionResponse;
import nl.davefemi.chess.web.dto.response.session.RequestedSessionResponse;
import nl.davefemi.chess.web.dto.response.session.SessionResponse;
import nl.davefemi.chess.session.service.GameSessionService;
import nl.davefemi.chess.session.model.Player;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/games")
public class GameSessionController {
    private final GameSessionService gameSessionService;

    @PostMapping("/invite")
    public ResponseEntity<RequestedSessionResponse> inviteToSession
            (@Nullable @RequestParam("color")String color, HttpServletRequest request)
            throws BoardException, InvalidTokenException, SessionException {
        log.info("Received from {}: session initiation request", request.getSession().getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(gameSessionService.startGameSession(color));
    }

    @PostMapping("/join")
    public ResponseEntity<AcceptedSessionResponse> joinSession
            (@RequestParam("token") String accessToken, HttpServletRequest request)
            throws SessionNotFoundException, BoardException, GameException, InvalidTokenException, SessionException {
        log.info("Received from {}: join request", request.getSession().getId());
        return ResponseEntity.ok(gameSessionService.joinGameSession(accessToken));
    }

    @PostMapping("/rematch/request")
    public ResponseEntity<SessionResponse> requestRematch(HttpServletRequest request)
            throws SessionNotFoundException, BoardException, SessionException {
        Player player = (Player) request.getAttribute("player");
        log.info("Received sessionId={}: invite request", player.getSessionId());
        return ResponseEntity.status(HttpStatus.CREATED).body(gameSessionService.offerRematch(player));
    }

    @PostMapping("/rematch/accept")
    public ResponseEntity<SessionResponse> acceptRematch(HttpServletRequest request)
            throws SessionNotFoundException, BoardException, GameException, SessionException {
        Player player = (Player) request.getAttribute("player");
        log.info("Received sessionId={}: join request", player.getSessionId());
        return ResponseEntity.ok(gameSessionService.acceptRematch(player));
    }

    @DeleteMapping("/rematch/decline")
    public ResponseEntity<SessionResponse> declineRematch(HttpServletRequest request)
            throws SessionNotFoundException, BoardException, SessionException {
        Player player = (Player) request.getAttribute("player");
        return ResponseEntity.ok(gameSessionService.declineRematch(player));
    }

    @PostMapping("/bot")
    public ResponseEntity<?> playAgainstBot(){
        return null;
    }


}
