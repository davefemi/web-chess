package nl.davefemi.controller;

import lombok.RequiredArgsConstructor;
import nl.davefemi.data.dto.*;
import nl.davefemi.data.dto.move.MoveRequestDTO;
import nl.davefemi.data.dto.move.PromotionMoveDTO;
import nl.davefemi.data.dto.move.SingleMoveDTO;
import nl.davefemi.data.dto.move.CastlingMoveDTO;
import nl.davefemi.exception.BoardException;
import nl.davefemi.exception.GameException;
import nl.davefemi.exception.MoveException;
import nl.davefemi.service.GameService;
import nl.davefemi.service.GameSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;
    private final GameSessionService sessionService;

    @PostMapping
    public ResponseEntity<?> getNewGame() throws GameException {
        return ResponseEntity.ok(sessionService.getNewGame());
    }

    @GetMapping("/{id}/positions")
    public BoardDTO getPositions(@PathVariable("id") String gameId) throws FileNotFoundException {
        return gameService.getPositions(gameId);
    }

    @GetMapping("/{id}/get-player-turn")
    public GameReponseDTO getPlayerTurn(@PathVariable("id") String gameId) throws FileNotFoundException, GameException {
        return gameService.getPlayerTurn(gameId);
    }

    @GetMapping("/{id}/is-check")
    public ResponseEntity<?> isCheck(@PathVariable("id") String gameId, @RequestBody GameRequestDTO request) throws FileNotFoundException, BoardException {
        return ResponseEntity.ok(gameService.isCheck(gameId, request.getColor()));
    }

    @GetMapping("/{id}/is-check-mate")
    public ResponseEntity<?> isCheckMate(@PathVariable("id") String gameId, @RequestBody GameRequestDTO request) throws FileNotFoundException, BoardException {
        return ResponseEntity.ok(gameService.isCheckMate(gameId, request.getColor()));
    }

    @GetMapping("/{id}/moves")
    public ResponseEntity<?> getAvailableMoves(@PathVariable("id") String gameId, @RequestBody GameRequestDTO request) throws FileNotFoundException, BoardException {
        return ResponseEntity.ok(gameService.getAvailableMoves(gameId, request.getColor()));
    }

    @PostMapping("/{id}/moves/single")
    public ResponseEntity<?> executeMove(@PathVariable("id") String gameId, @RequestBody MoveRequestDTO<SingleMoveDTO> request) throws FileNotFoundException, MoveException, BoardException, GameException {
        return ResponseEntity.ok(gameService.executeMove(gameId, request.getColor(), request.getMove()));
    }

    @PostMapping("/{id}/moves/castling")
    public ResponseEntity<?> executeCastling(@PathVariable("id") String gameId, @RequestBody MoveRequestDTO<CastlingMoveDTO> request) throws FileNotFoundException, MoveException, BoardException, GameException {
        return ResponseEntity.ok(gameService.executeMove(gameId, request.getColor(), request.getMove()));
    }

    @PostMapping("{id}/moves/promote")
    public ResponseEntity<?> promotePawn(@PathVariable("id") String gameId, @RequestBody MoveRequestDTO<PromotionMoveDTO> request) throws FileNotFoundException, MoveException, BoardException, GameException {
        return ResponseEntity.ok(gameService.executeMove(gameId, request.getColor(), request.getMove()));
    }
}
