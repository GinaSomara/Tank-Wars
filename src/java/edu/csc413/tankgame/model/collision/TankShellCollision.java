package edu.csc413.tankgame.model.collision;

import edu.csc413.tankgame.model.GameWorld;
import edu.csc413.tankgame.model.entity.Entity;
import edu.csc413.tankgame.model.entity.Shell;
import edu.csc413.tankgame.model.entity.Tank;

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

