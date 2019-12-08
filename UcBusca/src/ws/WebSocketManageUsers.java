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
        System.out.println("CONSTRUTOR");
    }

    @OnOpen
    public void start(Session session) {
        users.add(this);
        this.session = session;
        System.out.println("START");
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
    	sendMessage(message);
        System.out.println("RECEBEU"+message);
    }
    
    @OnError
    public void handleError(Throwable t) {
    	t.printStackTrace();
    }

    private void sendMessage(String text) {
        System.out.println("ENVIOU "+text);
        for (WebSocketManageUsers user:this.users) {
            System.out.println(user);
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
