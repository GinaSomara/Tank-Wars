package edu.csc413.tankgame.model.collision;

import edu.csc413.tankgame.model.GameWorld;
import edu.csc413.tankgame.model.entity.*;

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
