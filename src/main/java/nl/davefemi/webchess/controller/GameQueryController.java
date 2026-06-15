package nl.davefemi.webchess.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.exception.SessionException;
import nl.davefemi.webchess.service.GameQueryService;
import nl.davefemi.webchess.session.Player;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.FileNotFoundException;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameQueryController {
    private final GameQueryService queryService;

    @GetMapping("/positions")
    public ResponseEntity<?> getChessPositions(HttpServletRequest request)
            throws FileNotFoundException, BoardException, GameException, SessionException {
        Player player = (Player) request.getAttribute("player");
        return ResponseEntity.ok(queryService.getChessPositions(player));
    }

    @GetMapping("/get-player-turn")
    public ResponseEntity<?> getPlayerTurn(HttpServletRequest request)
            throws FileNotFoundException, GameException, BoardException, SessionException {
        Player player = (Player) request.getAttribute("player");
        return ResponseEntity.ok(queryService.getPlayerTurn(player));
    }

    @GetMapping("/status")
    public ResponseEntity<?> isCheck(HttpServletRequest request)
            throws FileNotFoundException, BoardException, SessionException {
        Player player = (Player) request.getAttribute("player");
        return ResponseEntity.ok(queryService.isCheck(player));
    }

    @GetMapping("/moves")
    public ResponseEntity<?> getAvailableMoves(HttpServletRequest request)
            throws FileNotFoundException, BoardException, SessionException, GameException {
        Player player = (Player) request.getAttribute("player");
        return ResponseEntity.ok(queryService.getAvailableMoves(player));
    }
}
