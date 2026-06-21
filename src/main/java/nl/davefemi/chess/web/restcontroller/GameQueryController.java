package nl.davefemi.chess.web.restcontroller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import nl.davefemi.chess.exception.BoardException;
import nl.davefemi.chess.exception.GameException;
import nl.davefemi.chess.exception.SessionException;
import nl.davefemi.chess.exception.SessionNotFoundException;
import nl.davefemi.chess.gameplay.service.GameQueryService;
import nl.davefemi.chess.session.model.Player;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameQueryController {
    private final GameQueryService queryService;

    @GetMapping("/positions")
    public ResponseEntity<?> getChessPositions(HttpServletRequest request)
            throws BoardException, SessionNotFoundException, SessionException {
        Player player = (Player) request.getAttribute("player");
        return ResponseEntity.ok(queryService.getChessPositions(player));
    }

    @GetMapping("/get-player-turn")
    public ResponseEntity<?> getPlayerTurn(HttpServletRequest request)
            throws BoardException, SessionNotFoundException, SessionException {
        Player player = (Player) request.getAttribute("player");
        return ResponseEntity.ok(queryService.getPlayerTurn(player));
    }

    @GetMapping("/status")
    public ResponseEntity<?> isCheck(HttpServletRequest request)
            throws BoardException, SessionNotFoundException, SessionException {
        Player player = (Player) request.getAttribute("player");
        return ResponseEntity.ok(queryService.isCheck(player));
    }

    @GetMapping("/moves")
    public ResponseEntity<?> getAvailableMoves(HttpServletRequest request)
            throws BoardException, SessionNotFoundException, GameException, SessionException {
        Player player = (Player) request.getAttribute("player");
        return ResponseEntity.ok(queryService.getAvailableMoves(player));
    }
}
