import { Client } from "@stomp/stompjs";
import WebSocket from "ws";

const gameId = "B3n8Z4tLHA8";
const playerId ="lRFUPVV4FDo";
const token = "jeIScrB-exfpCQQvQoJ6lb292vZ3o6ZehqrPqe2j9_M"

const client = new Client({
    webSocketFactory: () => new WebSocket("ws://localhost:8080/NME2fTVJmEY"),
    connectHeaders: {
        Authorization: `Bearer ${token}`,
    },
    debug: (msg) => console.log("[STOMP]", msg),


    onConnect: () => {
        console.log("Connected");
        client.subscribe(`/topic/games/${gameId}/state`, (message) => {
            console.log("Received:", message.body);
        });

        client.subscribe(`/topic/games/players/${playerId}`, (message) => {
            console.log("Received:", message.body);
        });

        client.publish({
            destination: `/app/games/moves`,
            headers: {
                "content-type": "application/json"
            },
            body: JSON.stringify({
                move: {
                    move_type: "single",
                    from: "g7",
                    to: "g6"
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