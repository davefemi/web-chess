package nl.davefemi.controller;

import lombok.RequiredArgsConstructor;
import nl.davefemi.exception.BoardException;
import nl.davefemi.exception.GameException;
import nl.davefemi.exception.SessionException;
import nl.davefemi.service.GameSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/games")
public class GameLobbyController {
    private final GameSessionService gameSessionService;

    @PostMapping
    public ResponseEntity<?> startSession(@RequestParam("color")String color) throws GameException, SessionException {
        return ResponseEntity.ok(gameSessionService.startGameSession(color));
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> startNewGame(@PathVariable("id")String sessionId) throws FileNotFoundException, SessionException, BoardException, GameException {
        return ResponseEntity.ok(gameSessionService.startNewGameInCurrentSession(sessionId));
    }

    @PostMapping("/invite")
    public ResponseEntity<?> invitePlayer() throws GameException, SessionException {
        return ResponseEntity.ok(gameSessionService.startGameSession(null));
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<?> joinGame(@PathVariable("id") String sessionId, @RequestParam("color") String color) throws FileNotFoundException, SessionException, BoardException, GameException {
        return ResponseEntity.ok(gameSessionService.joinGameSession(sessionId, color));
    }

    @PostMapping("/bot")
    public ResponseEntity<?> playAgainstBot(){
        return null;
    }


}
