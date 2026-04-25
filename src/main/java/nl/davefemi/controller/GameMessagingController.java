package nl.davefemi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameMessagingController {
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/sessions/create")
    @SendTo("/topics/sessions/")
    public String createSession(@DestinationVariable String id, String message) throws InterruptedException {
        Thread.sleep(1000);
        return "YES";
    }
}
