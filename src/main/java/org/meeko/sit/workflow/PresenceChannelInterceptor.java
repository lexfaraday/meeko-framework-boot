package org.meeko.sit.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;

@Scope("prototype")
public class PresenceChannelInterceptor extends ChannelInterceptorAdapter {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {

        StompHeaderAccessor sha = StompHeaderAccessor.wrap(message);

        // ignore non-STOMP messages like heartbeat messages
        if (sha.getCommand() == null) {
            return;
        }

        String sessionId = sha.getSessionId();

        switch (sha.getCommand()) {
            case CONNECT:
                System.out.println("Connect: " + sessionId);
                break;
            case CONNECTED:
                System.out.println("Connected: " + sessionId);
                break;
            case DISCONNECT:
                System.out.println("Disconnect: " + sessionId);
                break;
            default:
                break;
        }
    }
}