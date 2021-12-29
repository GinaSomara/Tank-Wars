package game.model.collision;

import game.GameWorld;
import game.model.entity.Entity;
import game.model.entity.Shell;
import game.model.entity.Tank;

public class TankShellCollision extends CollisionHandler
{
    // ======== CONSTRUCTOR ======== //
    public TankShellCollision()
    {
        super(Tank.class, Shell.class);
    }

    // ======== INHERITED METHODS ======== //
    @Override
    protected void handleIndividualCollisions(Entity entityA, Entity entityB, GameWorld gameWorld)
    {
        // 1 -> Remove shell from the gameWorld
        gameWorld.addEntityToRemove(entityB);

        // 2 -> Deduct health from tank
        entityA.deductHealth();
    }
}

