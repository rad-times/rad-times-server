package com.radtimes.rad_times_server.socket;

import com.radtimes.rad_times_server.service.UserCheckInService;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class WebSocketHandler extends AbstractWebSocketHandler {
    Set<WebSocketSession> connections = new HashSet<WebSocketSession>();

    private final UserCheckInService userCheckInService;

    public WebSocketHandler(UserCheckInService userCheckInService) {
        this.userCheckInService = userCheckInService;
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        System.out.println("Session opened");
        connections.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus){
        System.out.println("Session closed");
        connections.remove(session);

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        connections.forEach(conn -> {
            try {
                if (!conn.getId().equals(session.getId())) {
                    conn.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws IOException {
        System.out.println("New Binary Message Received");
        session.sendMessage(message);
    }
}
