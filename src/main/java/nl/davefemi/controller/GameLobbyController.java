package nl.davefemi.controller;

import lombok.RequiredArgsConstructor;
import nl.davefemi.exception.BoardException;
import nl.davefemi.exception.GameException;
import nl.davefemi.exception.SessionException;
import nl.davefemi.service.GameSessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/games")
public class GameLobbyController {
    private final GameSessionService gameSessionService;

    @PostMapping
    public ResponseEntity<?> enterGameRoom() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not welcome here");
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> startNewGame(@PathVariable("id")String sessionId) throws FileNotFoundException, SessionException, BoardException, GameException {
        return ResponseEntity.status(HttpStatus.CREATED).body(gameSessionService.startNewGameInCurrentSession(sessionId));
    }

    @PostMapping("/invite")
    public ResponseEntity<?> invitePlayer(@RequestParam("color")String color) throws SessionException {
        return ResponseEntity.status(HttpStatus.CREATED).body(gameSessionService.startGameSession(color));
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinGame(@RequestParam("code") String accessCode) throws FileNotFoundException, SessionException, BoardException {
        return ResponseEntity.ok(gameSessionService.joinGameSession(accessCode));
    }

    @PostMapping("/bot")
    public ResponseEntity<?> playAgainstBot(){
        return null;
    }


}
