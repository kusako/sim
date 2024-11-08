package ms.braindump.sim.game;

import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import ms.braindump.sim.webservice.RemoteNode;
import ms.braindump.sim.webservice.TestNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class GameServer {

    private static final Logger log = LoggerFactory.getLogger(GameServer.class);

    private List<RemoteNode> nodes;
    private boolean isRunning;
    private Thread mainLoopThread;

    public GameServer() {
        log.debug("Construct GameServer.");
    }

    public synchronized void start() {
        log.debug("Starting Game Server Main Loop");
        if (!isRunning) {
            if (this.mainLoopThread == null) {
                this.mainLoopThread = Thread.ofPlatform().start(new MainLoop());
            }
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
        final var testNode = new TestNode(UUID.randomUUID().toString(), "Agent", new RemoteNode.Position(0, 0), new RemoteNode.Velocity(10, 10));
        nodes.add(testNode);
    }

    class MainLoop implements Runnable {

        @Override
        public void run() {
            log.debug("Starting game loop.");
            var time = System.nanoTime();
            var processingTime = 0L;
            while (isRunning) {
                final var deltaTime = System.nanoTime() - time;
                log.debug("Delta: {}.", deltaTime);
                time = System.nanoTime();
                nodes.forEach(node -> node.tick(deltaTime));
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
