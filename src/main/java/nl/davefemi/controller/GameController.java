package nl.davefemi.controller;

import lombok.RequiredArgsConstructor;
import nl.davefemi.data.dto.*;
import nl.davefemi.service.GameService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> isCheck(@PathVariable("id") String gameId, @RequestBody GameRequestDTO request) throws FileNotFoundException {
        return ResponseEntity.ok(gameService.isCheck(gameId, request.getColor()));
    }

    @GetMapping("/{id}/is-check-mate")
    public ResponseEntity<?> isCheckMate(@PathVariable("id") String gameId, @RequestBody GameRequestDTO request) throws FileNotFoundException {
        return ResponseEntity.ok(gameService.isCheckMate(gameId, request.getColor()));
    }

    @GetMapping("/{id}/moves")
    public ResponseEntity<?> getAvailableMoves(@PathVariable("id") String gameId, @RequestBody GameRequestDTO request) throws FileNotFoundException {
        return ResponseEntity.ok(gameService.getAvailableMoves(gameId, request.getColor()));
    }

    @PostMapping("/{id}/moves/single")
    public ResponseEntity<?> executeMove(@PathVariable("id") String gameId, @RequestBody MoveRequestDTO<SingleMoveDTO> request) throws FileNotFoundException {
        return ResponseEntity.ok(gameService.executeMove(gameId, request.getColor(), request.getMove()));
    }

    @PostMapping("/{id}/moves/castling")
    public ResponseEntity<?> executeCastling(@PathVariable("id") String gameId, @RequestBody MoveRequestDTO<CastlingMoveDTO> request) throws FileNotFoundException {
        return ResponseEntity.ok(gameService.executeMove(gameId, request.getColor(), request.getMove()));
    }

    @PostMapping("{id}/moves/promote")
    public ResponseEntity<?> promotePawn(@PathVariable("id") String gameId, @RequestBody MoveRequestDTO<PromotionMoveDTO> request) throws FileNotFoundException {
        return ResponseEntity.ok(gameService.executeMove(gameId, request.getColor(), request.getMove()));
    }
}
