import { Client } from "@stomp/stompjs";
import WebSocket from "ws";

const playerId ="M57Y9LAQu4k";
const token = "hN5zgMqAkG45dQ8BqIqZ5YjqM9knxqjPyXIFa81VO9g"
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
            destination: `/app/games/rematch/request`,
            headers: {
                "content-type": "application/json",
                "correlation-id": correlation,
            },
            body: JSON.stringify({
                move: {
                    move_type: "single",
                    from: "h4",
                    to: "f2",
                    rook_from: "h8",
                    rook_to: "f8",
                    new_piece_type:"knight"
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