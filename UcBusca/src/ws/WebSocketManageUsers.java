package ws;

import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnError;
import javax.websocket.Session;

@ServerEndpoint(value = "/ws")
public class WebSocketManageUsers {
    private static final Set<WebSocketManageUsers> users =
            new CopyOnWriteArraySet<>();
    private static final AtomicInteger sequence = new AtomicInteger(1);
    private final String username;
    private Session session;

    public WebSocketManageUsers() {
        username = "User" + sequence.getAndIncrement();
    }

    @OnOpen
    public void start(Session session) {
        users.add(this);
        this.session = session;
        String message = "*" + username + "* connected.";
        sendMessage(message);
    }

    @OnClose
    public void end() {
    	// clean up once the WebSocket connection is closed
        users.remove(this);
    }

    @OnMessage
    public void receiveMessage(String message) {
		// one should never trust the client, and sensitive HTML
        // characters should be replaced with &lt; &gt; &quot; &amp;
    	String upperCaseMessage = message.toUpperCase();
    	sendMessage("[" + username + "] " + upperCaseMessage);
    }
    
    @OnError
    public void handleError(Throwable t) {
    	t.printStackTrace();
    }

    private void sendMessage(String text) {
    	// uses *this* object's session to call sendText()
        for (WebSocketManageUsers user:this.users) {

            try {
                user.session.getBasicRemote().sendText(text);
            } catch (IOException e) {
                // clean up once the WebSocket connection is closed
                try {
                    this.session.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

    }
}
