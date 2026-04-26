package nl.davefemi.controller;

import lombok.RequiredArgsConstructor;
import nl.davefemi.data.dto.GameStateDTO;
import nl.davefemi.data.dto.session.SessionRequestDTO;
import nl.davefemi.data.dto.move.MoveRequestDTO;
import nl.davefemi.exception.BoardException;
import nl.davefemi.exception.GameException;
import nl.davefemi.exception.MoveException;
import nl.davefemi.exception.SessionException;
import nl.davefemi.service.GameService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.io.FileNotFoundException;

@Controller
@MessageMapping("/games/{id}")
@RequiredArgsConstructor
public class GameMessagingController {
    private final SimpMessagingTemplate messagingTemplate;
    private final GameService gameService;

    @MessageMapping("/moves")
    public void executeMove(@DestinationVariable("id") String sessionId, @Payload MoveRequestDTO request) throws FileNotFoundException, MoveException, BoardException, GameException, SessionException {
        GameStateDTO dto;
        try{
            messagingTemplate.convertAndSend("/topic/games/" + sessionId, gameService.executeMove( request.getPlayerId(), sessionId, request.getMove()));
        } catch (Exception e) {
            messagingTemplate.convertAndSend("/topic/games/" + sessionId, e.getMessage());
        }
    }

    @MessageMapping("/end")
    public void endSession(@DestinationVariable("id") String sessionId, @Payload SessionRequestDTO request){

    }
}
