package nl.davefemi.webchess.controller;

import lombok.RequiredArgsConstructor;
import nl.davefemi.webchess.data.dto.move.MoveRequestDTO;
import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.exception.MoveException;
import nl.davefemi.webchess.exception.SessionException;
import nl.davefemi.webchess.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@RestController
@RequiredArgsConstructor
@RequestMapping("app/games")
public class ProvisionaryGameController {
    private final GameService gameService;

    @PostMapping("/{id}/moves")
    public ResponseEntity<?> executeMove(@PathVariable("id") String sessionId, @RequestBody MoveRequestDTO request) throws FileNotFoundException, MoveException, BoardException, GameException, SessionException {
        return ResponseEntity.ok(gameService.executeMove(request.getPlayerId(), sessionId, request.getMove()));
    }
}
