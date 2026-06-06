package nl.davefemi.webchess.controller;

import lombok.RequiredArgsConstructor;
import nl.davefemi.webchess.data.dto.session.PlayerDTO;
import nl.davefemi.webchess.data.dto.session.SessionInvitationDTO;
import nl.davefemi.webchess.data.dto.session.SessionResponseDTO;
import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.exception.SessionException;
import nl.davefemi.webchess.service.GameSessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

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
    public ResponseEntity<SessionInvitationDTO> invitePlayer(@RequestParam("color")String color) throws SessionException, BoardException {
        return ResponseEntity.status(HttpStatus.CREATED).body(gameSessionService.startGameSession(color));
    }

    @PostMapping("invite/{id}")
    public ResponseEntity<SessionResponseDTO> invitePlayer(@PathVariable("id")String sessionId, @RequestBody PlayerDTO player) throws FileNotFoundException, SessionException, BoardException, GameException {
        return ResponseEntity.status(HttpStatus.CREATED).body(gameSessionService.startRematch(sessionId, player));
    }

    @PostMapping("/join")
    public ResponseEntity<SessionResponseDTO> joinGame(@RequestParam("code") String accessCode) throws FileNotFoundException, SessionException, BoardException, GameException {
        return ResponseEntity.ok(gameSessionService.joinGameSession(accessCode));
    }

    @PostMapping("/join/{id}")
    public ResponseEntity<SessionResponseDTO> joinGame(@PathVariable("id") String sessionId, @RequestBody PlayerDTO player) throws FileNotFoundException, SessionException, BoardException, GameException {
        return ResponseEntity.ok(gameSessionService.acceptRematch(sessionId, player));
    }

    @PostMapping("/bot")
    public ResponseEntity<?> playAgainstBot(){
        return null;
    }


}
