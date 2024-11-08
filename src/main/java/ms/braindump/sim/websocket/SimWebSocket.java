package ms.braindump.sim.websocket;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkus.vertx.ConsumeEvent;
import io.quarkus.websockets.next.*;
import jakarta.annotation.Nullable;
import jakarta.enterprise.context.ApplicationScoped;
import ms.braindump.sim.game.GameServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebSocket(path = "/messaging")
@ApplicationScoped
public class SimWebSocket {

    public enum CommandType {
        STATUS,
        START,
        STOP
    }

    public enum ResponseType {
        STATUS
    }

    @RegisterForReflection
    public record SimCommand(CommandType type, String payload) {
    }

    @RegisterForReflection
    public record SimResponse(ResponseType type, Object payload) {
    }

    private static final Logger log = LoggerFactory.getLogger(SimWebSocket.class);

    private final GameServer gameServer;

    public SimWebSocket(final GameServer gameServer) {
        this.gameServer = gameServer;
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
                response = new SimResponse(ResponseType.STATUS, gameServer.getNodes());
            }
            case START -> {
                log.debug("Start Sim command received");
                gameServer.start();
                response = new SimResponse(ResponseType.STATUS, gameServer.getNodes());
            }
            case STOP -> {
                log.debug("Stop Sim command received");
                gameServer.stop();
                response = new SimResponse(ResponseType.STATUS, gameServer.getNodes());
            }
            default -> {
                log.error("Unknown type received.");
                response = new SimResponse(ResponseType.STATUS, "Error");
            }
        }

        return response;
    }
}
