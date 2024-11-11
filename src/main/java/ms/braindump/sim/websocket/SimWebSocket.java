package ms.braindump.sim.websocket;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkus.vertx.ConsumeEvent;
import io.quarkus.websockets.next.*;
import io.smallrye.common.annotation.Blocking;
import jakarta.annotation.Nonnull;
import ms.braindump.sim.game.GameServer;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebSocket(path = "/messaging")
public class SimWebSocket {

    @ConfigProperty(name = "quarkus.websockets-next.server.max-message-size")
    String messageSize;
    private final OpenConnections connections;

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

    public SimWebSocket(@Nonnull final GameServer gameServer, @Nonnull final OpenConnections connections) {
        this.gameServer = gameServer;
        this.connections = connections;
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

    @ConsumeEvent("tick")
    @Blocking
    void ticked(final String message) {
        //final var response = new SimResponse(ResponseType.STATUS, gameServer.getNodes());
        //connections.forEach(c -> c.sendTextAndAwait(response));
    }

    @OnTextMessage
    public SimResponse onTextMessage(final SimCommand command) {
        log.debug("Max Message Size: {}", messageSize);
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
