import { Client } from "@stomp/stompjs";
import WebSocket from "ws";

const sessionId = "afbff240-910a-4054-a979-950e86de2098";
const playerId = "44e1dce8-baa3-4b94-814f-7df70220df78"

const client = new Client({
    webSocketFactory: () => new WebSocket("ws://localhost:8080/ws"),

    debug: (msg) => console.log("[STOMP]", msg),

    onConnect: () => {
        console.log("Connected");

        client.subscribe(`/topic/games/${sessionId}`, (message) => {
            console.log("Received:", message.body);
        });

        client.publish({
            destination: `/app/games/${sessionId}/moves`,
            headers: {
                "content-type": "application/json",
            },
            body: JSON.stringify({
                player_id: playerId,
                move: {
                    move_type: "single",
                    from_file: 1,
                    from_rank: 4,
                    to_file: 1,
                    to_rank: 3,
                },
            }),
        });
    },

    onStompError: (frame) => {
        console.error("Broker error:", frame.headers["message"]);
        console.error(frame.body);
    },

    onWebSocketError: (error) => {
        console.error("WebSocket error:", error);
    },
});

client.activate();