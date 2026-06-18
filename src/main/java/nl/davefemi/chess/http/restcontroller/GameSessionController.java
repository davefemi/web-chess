package nl.davefemi.chess.http.restcontroller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.chess.http.response.session.AcceptedSessionResponse;
import nl.davefemi.chess.http.response.game.RequestedSessionResponse;
import nl.davefemi.chess.http.response.session.SessionResponse;
import nl.davefemi.chess.exception.BoardException;
import nl.davefemi.chess.exception.GameException;
import nl.davefemi.chess.exception.InvalidTokenException;
import nl.davefemi.chess.exception.SessionNotFoundException;
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

    @PostMapping
    public ResponseEntity<?> enterGameRoom() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not welcome here");
    }

    @PostMapping("/invite")
    public ResponseEntity<RequestedSessionResponse> invitePlayer
            (@Nullable @RequestParam("color")String color, HttpServletRequest request)
            throws SessionNotFoundException, BoardException, InvalidTokenException {
        log.info("Received from {}: session initiation request", request.getSession().getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(gameSessionService.startGameSession(color));
    }

    @PostMapping("/join")
    public ResponseEntity<AcceptedSessionResponse> joinGame
            (@RequestParam("token") String accessToken, HttpServletRequest request)
            throws SessionNotFoundException, BoardException, GameException, InvalidTokenException {
        log.info("Received from {}: join request", request.getSession().getId());
        return ResponseEntity.ok(gameSessionService.joinGameSession(accessToken));
    }

    @PostMapping("/rematch/invite")
    public ResponseEntity<SessionResponse> invitePlayer(HttpServletRequest request)
            throws SessionNotFoundException, BoardException {
        Player player = (Player) request.getAttribute("player");
        log.info("Received sessionId={}: invite request", player.getSessionId());
        return ResponseEntity.status(HttpStatus.CREATED).body(gameSessionService.startRematch(player));
    }

    @PostMapping("/rematch/join")
    public ResponseEntity<SessionResponse> joinGame(HttpServletRequest request)
            throws SessionNotFoundException, BoardException, GameException {
        Player player = (Player) request.getAttribute("player");
        log.info("Received sessionId={}: join request", player.getSessionId());
        return ResponseEntity.ok(gameSessionService.acceptRematch(player, true));
    }

    @DeleteMapping("/rematch/decline")
    public ResponseEntity<SessionResponse> declineGame(HttpServletRequest request)
            throws SessionNotFoundException, BoardException, GameException {
        Player player = (Player) request.getAttribute("player");
        return ResponseEntity.ok(gameSessionService.acceptRematch(player,false));
    }

    @PostMapping("/bot")
    public ResponseEntity<?> playAgainstBot(){
        return null;
    }


}
