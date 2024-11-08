package ms.braindump.sim.webservice;

public class TestNode extends RemoteNode {

    public TestNode(String uuid, String scene, Position position, Velocity velocity) {
        super(uuid, scene, position, velocity);
    }

    @Override
    public void tick(final long delta) {
        double deltaInSeconds = delta / 1_000_000_000D;
        if (this.getPosition().x() >= 1000 || this.getPosition().x() < 0) {
            this.setVelocity(new Velocity(-this.getVelocity().x(), this.getVelocity().y()));
        }
        if (this.getPosition().y() >= 1000 || this.getPosition().y() < 0) {
            this.setVelocity(new Velocity(this.getVelocity().x(), -this.getVelocity().y()));
        }
        this.setPosition(
                new Position(
                        this.getPosition().x() + this.getVelocity().x() * deltaInSeconds,
                        this.getPosition().y() + this.getVelocity().y() * deltaInSeconds
                )
        );
    }
}
