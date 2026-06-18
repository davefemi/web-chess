package nl.davefemi.chess.http.websocket.controller;

import lombok.RequiredArgsConstructor;
import nl.davefemi.chess.data.dto.move.MoveRequestDTO;
import nl.davefemi.chess.http.websocket.service.GameMessageService;
import nl.davefemi.chess.play.service.GamePlayService;
import nl.davefemi.chess.session.model.PlayerPrincipal;
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

    @MessageMapping("/rematch/invite")
    public void invite(@Payload MoveRequestDTO request, PlayerPrincipal principal) {
        try{
            gamePlayService.executeMove(principal.player(), request.getMove());

        } catch (Exception e) {
            gameMessageService.sendMessage(principal.player().getMessageId(), e.getMessage());
        }
    }

    @MessageMapping("/rematch/join")
    public void joinInvite(@Payload MoveRequestDTO request, PlayerPrincipal principal) {
        try{
            gamePlayService.executeMove(principal.player(), request.getMove());

        } catch (Exception e) {
            gameMessageService.sendMessage(principal.player().getMessageId(), e.getMessage());
        }
    }

    @MessageMapping("/rematch/decline")
    public void declineInvite(@Payload MoveRequestDTO request, PlayerPrincipal principal) {
        try{
            gamePlayService.executeMove(principal.player(), request.getMove());

        } catch (Exception e) {
            gameMessageService.sendMessage(principal.player().getMessageId(), e.getMessage());
        }
    }
}
