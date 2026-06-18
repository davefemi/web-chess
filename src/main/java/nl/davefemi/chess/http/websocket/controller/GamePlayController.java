package nl.davefemi.chess.http.websocket.controller;

import lombok.RequiredArgsConstructor;
import nl.davefemi.chess.http.request.MoveRequest;
import nl.davefemi.chess.http.websocket.service.GameMessageService;
import nl.davefemi.chess.play.service.GamePlayService;
import nl.davefemi.chess.session.model.PlayerPrincipal;
import nl.davefemi.chess.session.service.GameSessionService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@MessageMapping("/games")
@RequiredArgsConstructor
public class GamePlayController {
    private final GameMessageService gameMessageService;
    private final GamePlayService gamePlayService;
    private final GameSessionService gameSessionService;

    @MessageMapping("/moves")
    public void executeMove(@Payload MoveRequest request, PlayerPrincipal principal) {
        try{
            gamePlayService.executeMove(principal.player(), request.getMove());

        } catch (Exception e) {
            gameMessageService.sendMessage(principal.player().getMessageId(), e.getMessage());
        }
    }

    @MessageMapping("/rematch/invite")
    public void invite(PlayerPrincipal principal) {
        try{
            gameSessionService.startRematch(principal.player());

        } catch (Exception e) {
            gameMessageService.sendMessage(principal.player().getMessageId(), e.getMessage());
        }
    }

    @MessageMapping("/rematch/join")
    public void joinInvite(PlayerPrincipal principal) {
        try{
            gameSessionService.acceptRematch(principal.player(), true);

        } catch (Exception e) {
            gameMessageService.sendMessage(principal.player().getMessageId(), e.getMessage());
        }
    }

    @MessageMapping("/rematch/decline")
    public void declineInvite(PlayerPrincipal principal) {
        try{
            gameSessionService.acceptRematch(principal.player(), false);
        } catch (Exception e) {
            gameMessageService.sendMessage(principal.player().getMessageId(), e.getMessage());
        }
    }
}
