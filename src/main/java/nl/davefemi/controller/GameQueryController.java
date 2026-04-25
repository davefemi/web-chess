package nl.davefemi.controller;

import lombok.RequiredArgsConstructor;
import nl.davefemi.exception.BoardException;
import nl.davefemi.exception.GameException;
import nl.davefemi.exception.SessionException;
import nl.davefemi.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.FileNotFoundException;

@RestController
@RequestMapping("/games/{id}")
@RequiredArgsConstructor
public class GameQueryController {
    private final GameService gameService;

    @GetMapping("/positions")
    public ResponseEntity<?> getChessPositions(@PathVariable("id") String sessionId) throws FileNotFoundException, BoardException, GameException, SessionException {
        return ResponseEntity.ok(gameService.getChessPositions(sessionId));
    }

    @GetMapping("/get-player-turn")
    public ResponseEntity<?> getPlayerTurn(@PathVariable("id") String sessionId) throws FileNotFoundException, GameException, BoardException, SessionException {
        return ResponseEntity.ok(gameService.getPlayerTurn(sessionId));
    }

    @GetMapping("/status")
    public ResponseEntity<?> isCheck(@PathVariable("id") String sessionId, @RequestParam("color") String color) throws FileNotFoundException, BoardException, SessionException {
        return ResponseEntity.ok(gameService.getStatus(sessionId, color));
    }

    @GetMapping("/moves")
    public ResponseEntity<?> getAvailableMoves(@PathVariable("id") String sessionId, @RequestParam("color") String color) throws FileNotFoundException, BoardException, SessionException {
        return ResponseEntity.ok(gameService.getAvailableMoves(sessionId, color));
    }
}
