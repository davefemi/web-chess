package nl.davidfemi.controller;

import lombok.RequiredArgsConstructor;
import nl.davidfemi.data.dto.*;
import nl.davidfemi.service.GameService;
import org.springframework.web.bind.annotation.*;
import java.io.FileNotFoundException;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;

    @PostMapping
    public GameReponseDTO getNewGame(){
        return gameService.getNewGame();
    }

    @GetMapping("/{id}/positions")
    public BoardDTO getPositions(@PathVariable("id") String gameId) throws FileNotFoundException {
        return gameService.getPositions(gameId);
    }

    @GetMapping("/{id}/get-player-turn")
    public GameReponseDTO getPlayerTurn(@PathVariable("id") String gameId) throws FileNotFoundException {
        return gameService.getPlayerTurn(gameId);
    }

    @GetMapping("/{id}/is-check")
    public GameReponseDTO isCheck(@PathVariable("id") String gameId, @RequestParam("color") String color) throws FileNotFoundException {
        return gameService.isCheck(gameId, color);
    }

    @GetMapping("/{id}/is-check-mate")
    public GameReponseDTO isCheckMate(@PathVariable("id") String gameId, @RequestParam("color") String color) throws FileNotFoundException {
        return gameService.isCheckMate(gameId, color);
    }

    @GetMapping("/{id}/moves")
    public GameReponseDTO getAvailableMoves(@PathVariable("id") String gameId, @RequestParam("color") String color) throws FileNotFoundException {
        return gameService.getAvailableMoves(gameId, color);
    }

    @PostMapping("/{id}/moves")
    public GameReponseDTO executeMove(@PathVariable("id") String gameId, @RequestParam("color") String color, @RequestBody MoveDTO move) throws FileNotFoundException {
        return gameService.executeMove(gameId, color, move);
    }

    @PostMapping("/{id}/moves/castling")
    public GameReponseDTO executeCastling(@PathVariable("id") String gameId, @RequestParam("color") String color, @RequestBody CastlingMoveDTO move) throws FileNotFoundException {
        return gameService.executeCastling(gameId, color, move);
    }

    @PostMapping("{id}/actions/promote-pawn")
    public GameReponseDTO promotePawn(@PathVariable("id") String gameId, @RequestParam("color") String color, @RequestBody PositionDTO position, @RequestParam("type") String pieceType) throws FileNotFoundException {
        return gameService.promotePawn(gameId, color, position, pieceType);
    }
}
