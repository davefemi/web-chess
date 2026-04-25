import { Client } from '@stomp/stompjs';
import WebSocket from 'ws';

const client = new Client({
    webSocketFactory: () => new WebSocket('ws://localhost:8080/ws'),

    onConnect: () => {
        console.log('Connected');

        client.subscribe('/topic/sessions/123', message => {
            console.log('Received:', message.body);
        });

        client.publish({
            destination: '/app/sessions/create',
            body: JSON.stringify({ player: 'client1' })
        });
    }
});

client.activate();