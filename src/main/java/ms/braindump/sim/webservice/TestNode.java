package ms.braindump.sim.webservice;

import jakarta.annotation.Nonnull;

public class TestNode extends RemoteNode {

    public TestNode(@Nonnull final String uuid, @Nonnull final String scene, @Nonnull final Position position, @Nonnull final Velocity velocity) {
        super(uuid, scene, position, velocity);
    }

    @Override
    public void tick(final long delta) {
        final var deltaInSeconds = delta / 1_000_000_000D;
        if (this.getPosition().x() >= 1_000 || this.getPosition().x() < 0) {
            this.setVelocity(new Velocity(-this.getVelocity().x(), this.getVelocity().y()));
        }
        if (this.getPosition().y() >= 1_000 || this.getPosition().y() < 0) {
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
