package nl.davefemi.webchess.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.webchess.data.dto.move.MoveRequestDTO;
import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.exception.MoveException;
import nl.davefemi.webchess.exception.SessionException;
import nl.davefemi.webchess.service.GameService;
import nl.davefemi.webchess.session.Player;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("app/games")
public class ProvisionaryGameController {
    private final GameService gameService;

    @PostMapping("/moves")
    public ResponseEntity<?> executeMove(HttpServletRequest request, @RequestBody MoveRequestDTO move)
            throws FileNotFoundException, MoveException, BoardException, GameException, SessionException {
        Player player = (Player) request.getAttribute("player");
        log.info("Received from sessionId={}, playerId={}: move request {}",
                player.getSessionId(), player.getId(), move.getMove());
        return ResponseEntity.ok(gameService.executeMove(player, move.getMove()));
    }

    @PostMapping("/surrender")
    public ResponseEntity<?> surrender(HttpServletRequest request)
            throws FileNotFoundException, BoardException, GameException, SessionException {
        Player player = (Player) request.getAttribute("player");
        log.info("Received from sessionId={}, playerId={}:  surrender request", player.getSessionId(), player.getId());
        return ResponseEntity.ok(gameService.surrender(player));
    }

}
