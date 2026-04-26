package nl.davefemi.controller;

import lombok.RequiredArgsConstructor;
import nl.davefemi.data.dto.move.MoveRequestDTO;
import nl.davefemi.exception.BoardException;
import nl.davefemi.exception.GameException;
import nl.davefemi.exception.MoveException;
import nl.davefemi.exception.SessionException;
import nl.davefemi.service.GameService;
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
