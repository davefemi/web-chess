import { Client } from "@stomp/stompjs";
import WebSocket from "ws";

const playerId ="8y0oE13KEWk";
const token = "RQWvcN3wJUXt1nbcy6yD79Eq__qOORfKQuMOVf4wq1A"
const correlation = (Math.random() + 1).toString(36).substring(7)

const client = new Client({
    webSocketFactory: () => new WebSocket("ws://localhost:8080/NME2fTVJmEY"),
    connectHeaders: {
        Authorization: `Bearer ${token}`,

    },
    debug: (msg) => console.log("[STOMP]", msg),


    onConnect: () => {
        console.log("Connected");
        client.subscribe(`/user/${playerId}/queue/games/updates`, (message) => {
            console.log("Received:", message.body);
        });


        client.publish({
            destination: `/app/games/moves`,
            headers: {
                "content-type": "application/json",
                "correlation_id": correlation,
            },
            body: JSON.stringify({
                move: {
                    move_type: "single",
                    from: "g5",
                    to: "g4"
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