package nl.davefemi.chess.http.restcontroller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.chess.exception.*;
import nl.davefemi.chess.http.request.MoveRequest;
import nl.davefemi.chess.play.service.GamePlayService;
import nl.davefemi.chess.session.model.Player;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("app/games")
public class ProvisionaryGameController {
    private final GamePlayService playService;

    @PostMapping("/moves")
    public ResponseEntity<?> executeMove(HttpServletRequest request, @RequestBody MoveRequest move)
            throws MoveException, BoardException, GameException, SessionNotFoundException, SessionException {
        Player player = (Player) request.getAttribute("player");
        log.info("Received from sessionId={}, playerId={}: move request {}",
                player.getSessionId(), player.getId(), move.getMove());
        return ResponseEntity.ok(playService.executeMove(player, move.getMove()));
    }

    @PostMapping("/surrender")
    public ResponseEntity<?> surrender(HttpServletRequest request)
            throws BoardException, GameException, SessionNotFoundException, SessionException {
        Player player = (Player) request.getAttribute("player");
        log.info("Received from sessionId={}, playerId={}:  surrender request", player.getSessionId(), player.getId());
        return ResponseEntity.ok(playService.surrender(player));
    }

}
