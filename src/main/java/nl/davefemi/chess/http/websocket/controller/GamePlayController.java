package nl.davefemi.chess.http.websocket.controller;

import lombok.RequiredArgsConstructor;
import nl.davefemi.chess.http.request.MoveRequest;
import nl.davefemi.chess.http.response.game.GameStateDto;
import nl.davefemi.chess.http.response.session.SessionResponse;
import nl.davefemi.chess.http.websocket.event.EventType;
import nl.davefemi.chess.http.websocket.message.GameMessageType;
import nl.davefemi.chess.http.websocket.service.GameMessagingService;
import nl.davefemi.chess.play.service.GamePlayService;
import nl.davefemi.chess.session.model.PlayerPrincipal;
import nl.davefemi.chess.session.service.GameSessionService;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@MessageMapping("/games")
@RequiredArgsConstructor
public class GamePlayController {
    private final GameMessagingService gameMessageService;
    private final GamePlayService gamePlayService;
    private final GameSessionService gameSessionService;

    @MessageMapping("/moves")
    public void executeMove(@Header("correlation_id") String correlationId, @Payload MoveRequest request, PlayerPrincipal principal) {
        try{
            GameStateDto dto = gamePlayService.executeMove(principal.player(), request.getMove());
            gameMessageService.sendResponseMessage(
                    principal.player().getMessageEndpointId(),
                    correlationId,
                    GameMessageType.COMMAND_RESPONSE,
                    EventType.MOVE_ACCEPTED,
                    dto);

        } catch (Exception e) {
            gameMessageService.sendResponseMessage(
                    principal.player().getMessageEndpointId(),
                    correlationId,
                    GameMessageType.ERROR,
                    EventType.MOVE_REJECTED,
                    e.getMessage());
        }
    }

    @MessageMapping("/surrender")
    public void surrender(@Header("correlation_id") String correlationId, PlayerPrincipal principal) {
        try{
            GameStateDto dto = gamePlayService.surrender(principal.player());
            gameMessageService.sendResponseMessage(
                    principal.player().getMessageEndpointId(),
                    correlationId,
                    GameMessageType.COMMAND_RESPONSE,
                    EventType.PLAYER_SURRENDERED,
                    dto);

        } catch (Exception e) {
            gameMessageService.sendResponseMessage(
                    principal.player().getMessageEndpointId(),
                    correlationId,
                    GameMessageType.ERROR,
                    EventType.PLAYER_SURRENDERED,
                    e.getMessage());
        }
    }

    @MessageMapping("/rematch/request")
    public void requestRematch(@Header("correlation_id") String correlationId, PlayerPrincipal principal) {
        try{
            SessionResponse response = gameSessionService.offerRematch(principal.player());
            gameMessageService.sendResponseMessage(
                    principal.player().getMessageEndpointId(),
                    correlationId,
                    GameMessageType.COMMAND_RESPONSE,
                    EventType.REMATCH_REQUESTED,
                    response);
        } catch (Exception e) {
            gameMessageService.sendResponseMessage( principal.player().getMessageEndpointId(),
                    correlationId,
                    GameMessageType.ERROR,
                    EventType.REMATCH_REQUESTED,
                    e.getMessage());
        }
    }

    @MessageMapping("/rematch/accept")
    public void acceptRematch(@Header("correlation_id") String correlationId, PlayerPrincipal principal) {
        try{
            SessionResponse response = gameSessionService.acceptRematch(principal.player());
            gameMessageService.sendResponseMessage(
                    principal.player().getMessageEndpointId(),
                    correlationId,
                    GameMessageType.COMMAND_RESPONSE,
                    EventType.REMATCH_ACCEPTED,
                    response);
        } catch (Exception e) {
            gameMessageService.sendResponseMessage( principal.player().getMessageEndpointId(),
                    correlationId,
                    GameMessageType.ERROR,
                    EventType.REMATCH_REQUESTED,
                    e.getMessage());
        }
    }

    @MessageMapping("/rematch/decline")
    public void declineRematch(@Header("correlation_id") String correlationId, PlayerPrincipal principal) {
        try{
            SessionResponse response = gameSessionService.declineRematch(principal.player());
            gameMessageService.sendResponseMessage(
                    principal.player().getMessageEndpointId(),
                    correlationId,
                    GameMessageType.COMMAND_RESPONSE,
                    EventType.REMATCH_DECLINED,
                    response);
        } catch (Exception e) {
            gameMessageService.sendResponseMessage( principal.player().getMessageEndpointId(),
                    correlationId,
                    GameMessageType.ERROR,
                    EventType.REMATCH_DECLINED,
                    e.getMessage());
        }
    }
}
