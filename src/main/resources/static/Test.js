import { Client } from "@stomp/stompjs";
import WebSocket from "ws";

const sessionId = "dec8a8b7-4b84-4ab0-8794-d8cf16f4192c";
const playerId = "76af85df-a4e6-4dce-87b8-09b19f53a9a6"
// const playerId = "bb124abe-d0bb-4b75-81d0-b9ea7761d40d"

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
                    from: "h5",
                    to: "f7"
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