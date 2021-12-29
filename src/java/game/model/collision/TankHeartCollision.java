package game.model.collision;

import game.GameWorld;
import game.model.entity.*;

public class TankHeartCollision extends CollisionHandler
{
    // ======== CONSTRUCTOR ======== //
    public TankHeartCollision()
    {
        super(Tank.class, Heart.class);
    }

    // ======== INHERITED METHODS ======== //
    @Override
    protected void handleIndividualCollisions(Entity entityA, Entity entityB, GameWorld gameWorld)
    {
        // 1 -> Remove Heart from the gameWorld
        gameWorld.addEntityToRemove(entityB);

        // 2 -> Add health to Tank
        entityA.addHealth(50);
    }
}
