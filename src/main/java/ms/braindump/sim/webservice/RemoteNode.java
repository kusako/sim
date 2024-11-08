package ms.braindump.sim.webservice;

public class RemoteNode {
    private String uuid;
    private String scene;
    private Position position;
    private Velocity velocity;

    public RemoteNode(String uuid, String scene, Position position, Velocity velocity) {
        this.uuid = uuid;
        this.scene = scene;
        this.position = position;
        this.velocity = velocity;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }


    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public void setVelocity(Velocity velocity) {
        this.velocity = velocity;
    }

    public Velocity getVelocity() {
        return velocity;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public void tick(final long delta) {
    }

    public record Position(double x, double y) {
    }

    ;

    public record Velocity(double x, double y) {
    }

    ;
}
