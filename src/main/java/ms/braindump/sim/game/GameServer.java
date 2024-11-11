package ms.braindump.sim.game;

import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import ms.braindump.sim.webservice.RemoteNode;
import ms.braindump.sim.webservice.TestNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@ApplicationScoped
public class GameServer {

    private static final Logger log = LoggerFactory.getLogger(GameServer.class);
    private final EventBus eventBus;

    private List<RemoteNode> nodes;
    private boolean isRunning;

    public GameServer(@Nonnull final EventBus eventBus) {
        this.eventBus = eventBus;
        log.debug("Construct GameServer.");
    }

    public synchronized void start() {
        log.debug("Starting Game Server Main Loop");
        if (!isRunning) {
            Thread.ofVirtual().start(new MainLoop());
            this.isRunning = true;
        }
        log.debug("Started Game Server Main Loop");
    }

    public synchronized void stop() {
        log.debug("Stopping Game Server Main Loop");
        if (isRunning) {
            this.isRunning = false;
        }
    }

    @PostConstruct
    void init() {
        log.debug("Init GameServer");

        this.nodes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            final var testNode = new TestNode(
                    UUID.randomUUID().toString(),
                    "Agent",
                    new RemoteNode.Position(random(0, 999), random(0, 999)),
                    new RemoteNode.Velocity(random(-20, 20), random(-20, 20)));
            nodes.add(testNode);
        }
    }

    private int random(final int min, final int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    class MainLoop implements Runnable {

        @Override
        public void run() {
            log.debug("Starting game loop.");
            var time = System.nanoTime();
            long processingTime;
            while (isRunning) {
                final var deltaTime = System.nanoTime() - time;
                log.debug("Delta: {}.", deltaTime);
                time = System.nanoTime();
                nodes.forEach(node -> node.tick(deltaTime));
                eventBus.publish("tick", "");
                processingTime = System.nanoTime() - time;
                log.debug("Processing: {}.", processingTime);
                while (System.nanoTime() - time < 1_000_000_000) {
                    //
                }
            }
        }
    }

    public List<RemoteNode> getNodes() {
        return nodes;
    }
}
