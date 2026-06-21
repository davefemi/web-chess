package nl.davefemi.chess.web.websocket.stompcontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.chess.exception.BoardException;
import nl.davefemi.chess.exception.SessionException;
import nl.davefemi.chess.exception.SessionNotFoundException;
import nl.davefemi.chess.web.dictionary.StompHeaders;
import nl.davefemi.chess.web.dto.request.MoveRequest;
import nl.davefemi.chess.web.websocket.service.GameMessagingService;
import nl.davefemi.chess.gameplay.service.GamePlayService;
import nl.davefemi.chess.session.model.Player;
import nl.davefemi.chess.session.model.PlayerPrincipal;
import nl.davefemi.chess.session.service.GameSessionService;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Slf4j
@Controller
@MessageMapping("/games")
@RequiredArgsConstructor
public class GamePlayController {
    private final GameMessagingService gameMessageService;
    private final GamePlayService gamePlayService;
    private final GameSessionService gameSessionService;

    @MessageMapping("/moves")
    public void executeMove(@Header(StompHeaders.CORRELATION_ID) String correlationId,
                            @Payload MoveRequest request, PlayerPrincipal principal) throws SessionException, BoardException, SessionNotFoundException {
        Player player = principal.player();
        log(player.getSessionId(), player.getId(), "/moves");
        try{
            gameMessageService.sendAcceptedMoveResponse(
                    player.getSessionId(),
                    player.getChannelId(),
                    correlationId,
                    gamePlayService.executeMove(player, request.getMove()));

        } catch (Exception e) {
            gameMessageService.sendErrorResponse(
                    player.getSessionId(),
                    player.getChannelId(),
                    correlationId,
                    e.getMessage());
        }
    }

    @MessageMapping("/surrender")
    public void surrender(@Header(StompHeaders.CORRELATION_ID) String correlationId,
                          PlayerPrincipal principal) throws SessionException, BoardException, SessionNotFoundException {
        Player player = principal.player();
        log(player.getSessionId(), player.getId(), "/surrender");
        try{
            gameMessageService.sendSurrenderResponse(
                    player.getSessionId(),
                    player.getChannelId(),
                    correlationId,
                    gamePlayService.surrender(player));
        } catch (Exception e) {
            gameMessageService.sendErrorResponse(
                    player.getSessionId(),
                    player.getChannelId(),
                    correlationId,
                    e.getMessage());
        }
    }

    @MessageMapping("/rematch/request")
    public void requestRematch(@Header(StompHeaders.CORRELATION_ID) String correlationId,
                               PlayerPrincipal principal) throws SessionException, BoardException, SessionNotFoundException {
        Player player = principal.player();
        log(player.getSessionId(), player.getId(), "/rematch/request");
        try{
            gameMessageService.sendRequestedRematchResponse(
                    player.getSessionId(),
                    player.getChannelId(),
                    correlationId,
                    gameSessionService.offerRematch(player));
        } catch (Exception e) {
            gameMessageService.sendErrorResponse(
                    player.getSessionId(),
                    player.getChannelId(),
                    correlationId,
                    e.getMessage());
        }
    }

    @MessageMapping("/rematch/accept")
    public void acceptRematch(@Header(StompHeaders.CORRELATION_ID) String correlationId,
                              PlayerPrincipal principal) throws SessionException, BoardException, SessionNotFoundException {
        Player player = principal.player();
        log(player.getSessionId(), player.getId(), "/rematch/accept");
        try{
            gameMessageService.sendAcceptedRematchResponse(
                    player.getSessionId(),
                    player.getChannelId(),
                    correlationId,
                    gameSessionService.acceptRematch(player));
        } catch (Exception e) {
            gameMessageService.sendErrorResponse(
                    player.getSessionId(),
                    player.getChannelId(),
                    correlationId,
                    e.getMessage());
        }
    }

    @MessageMapping("/rematch/decline")
    public void declineRematch(@Header(StompHeaders.CORRELATION_ID) String correlationId,
                               PlayerPrincipal principal) throws SessionException, BoardException, SessionNotFoundException {
        Player player = principal.player();
        log(player.getSessionId(), player.getId(), "/rematch/decline");
        try{
            gameMessageService.sendDeclinedRematchResponse(
                    player.getSessionId(),
                    player.getChannelId(),
                    correlationId,
                    gameSessionService.declineRematch(player));
        } catch (Exception e) {
            gameMessageService.sendErrorResponse(
                    player.getSessionId(),
                    player.getChannelId(),
                    correlationId,
                    e.getMessage());
        }
    }

    @MessageMapping("/end-session")
    public void endSession(@Header(StompHeaders.CORRELATION_ID) String correlationId,
        PlayerPrincipal principal) throws SessionException, BoardException, SessionNotFoundException {
        Player player = principal.player();
        log(player.getSessionId(), player.getId(), "/end-session");
        try{
            gameMessageService.sendEndSessionResponse(
                    player.getSessionId(),
                    player.getChannelId(),
                    correlationId,
                    gameSessionService.endSession(player));
        } catch (Exception e) {
            {
                gameMessageService.sendErrorResponse(
                        player.getSessionId(),
                        player.getChannelId(),
                        correlationId,
                        e.getMessage());
            }
        }
    }

    private void log(UUID sessionId, UUID playerId, String request){
        log.info("Request received from sessionId={}, playerId={}: {}", sessionId, playerId, request);
    }
}
