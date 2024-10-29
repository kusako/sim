package ms.braindump.sim.websocket;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkus.websockets.next.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebSocket(path = "/messaging")
public class SimWebSocket {

    public enum CommandType {
        STATUS
    }

    public enum ResponseType {
        STATUS
    }

    @RegisterForReflection
    public record SimCommand(CommandType type, String payload) {
    }

    @RegisterForReflection
    public record SimResponse(ResponseType type, String payload) {
    }

    private static final Logger log = LoggerFactory.getLogger(SimWebSocket.class);

    private final WebSocketConnection connection;

    public SimWebSocket(final WebSocketConnection connection) {
        this.connection = connection;
    }

    @OnOpen
    public void onOpen() {
        log.debug("Connection Opened.");
    }

    @OnClose
    public void onClose() {
        log.debug("Connection closed.");
    }

    @OnError
    public void onError(final Throwable error) {
        log.debug("Error occured.", error);
    }

    @OnTextMessage
    public SimResponse onTextMessage(final SimCommand command) {
        final SimResponse response;
        switch (command.type) {
            case STATUS -> {
                log.debug("Status type received with payload {}", command.payload);
                response = new SimResponse(ResponseType.STATUS, command.payload);
            }
            default -> {
                log.error("Unkown type received.");
                response = new SimResponse(ResponseType.STATUS, "Error");
            }
        }

        return response;
    }
}
