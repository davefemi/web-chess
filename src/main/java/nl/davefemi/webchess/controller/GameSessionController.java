package nl.davefemi.webchess.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.webchess.data.dto.session.SessionInitiationDTO;
import nl.davefemi.webchess.data.dto.session.SessionResponseDTO;
import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.exception.SessionException;
import nl.davefemi.webchess.service.GameSessionService;
import nl.davefemi.webchess.session.Player;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.FileNotFoundException;

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
    public ResponseEntity<SessionInitiationDTO> invitePlayer
            (@RequestParam("color")String color, HttpServletRequest request)
            throws SessionException, BoardException {
        log.info("Received from {}: session initiation request", request.getSession().getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(gameSessionService.startGameSession(color));
    }

    @PostMapping("/invite1")
    public ResponseEntity<SessionResponseDTO> invitePlayer(HttpServletRequest request)
            throws FileNotFoundException, SessionException, BoardException, GameException {
        Player player = (Player) request.getAttribute("player");
        log.info("Received sessionId={}: invite request", player.getSessionId());
        return ResponseEntity.status(HttpStatus.CREATED).body(gameSessionService.startRematch(player));
    }

    @PostMapping("/join")
    public ResponseEntity<SessionInitiationDTO> joinGame
            (@RequestParam("token") String accessToken, HttpServletRequest request)
            throws FileNotFoundException, SessionException, BoardException, GameException {
        log.info("Received from {}: join request", request.getSession().getId());
        return ResponseEntity.ok(gameSessionService.joinGameSession(accessToken));
    }

    @PostMapping("/join2")
    public ResponseEntity<SessionResponseDTO> joinGame(HttpServletRequest request)
            throws FileNotFoundException, SessionException, BoardException, GameException {
        Player player = (Player) request.getAttribute("player");
        log.info("Received sessionId={}: join request", player.getSessionId());
        return ResponseEntity.ok(gameSessionService.acceptRematch(player));
    }

    @PostMapping("/bot")
    public ResponseEntity<?> playAgainstBot(){
        return null;
    }


}
