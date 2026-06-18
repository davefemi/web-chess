package nl.davefemi.webchess.controller;

import lombok.RequiredArgsConstructor;
import nl.davefemi.webchess.data.dto.move.MoveRequestDTO;
import nl.davefemi.webchess.service.GameMessageService;
import nl.davefemi.webchess.service.GamePlayService;
import nl.davefemi.webchess.session.PlayerPrincipal;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@MessageMapping("/games")
@RequiredArgsConstructor
public class GamePlayController {
    private final GameMessageService gameMessageService;
    private final GamePlayService gamePlayService;

    @MessageMapping("/moves")
    public void executeMove(@Payload MoveRequestDTO request, PlayerPrincipal principal) {
        try{
            gamePlayService.executeMove(principal.player(), request.getMove());

        } catch (Exception e) {
            gameMessageService.sendMessage(principal.player().getMessageId(), e.getMessage());
        }
    }
}
