package com.spotcheck.spotcheck_server.socket;

import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class WebSocketHandler extends AbstractWebSocketHandler {
    Set<WebSocketSession> connnections = new HashSet<WebSocketSession>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        connnections.add(session);
    }

    public void afterConnectionClosed(WebSocketSession session) throws IOException {
        connnections.remove(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        System.out.println("New Text Message Received {}," + message);
        connnections.forEach(conn -> {
            try {
                conn.sendMessage(message);
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
