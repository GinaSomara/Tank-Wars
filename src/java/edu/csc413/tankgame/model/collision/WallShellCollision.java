package edu.csc413.tankgame.model.collision;

import edu.csc413.tankgame.model.GameWorld;
import edu.csc413.tankgame.model.entity.*;


public class WallShellCollision extends CollisionHandler
{
    // ======== CONSTRUCTOR ======== //
    public WallShellCollision()
    {
        super(Wall.class, Shell.class);
    }

    // ======== INHERITED METHODS ======== //
    @Override
    protected void handleIndividualCollisions(Entity entityA, Entity entityB, GameWorld gameWorld) {
        // 3 -> Remove shell from the gameWorld
        gameWorld.addEntityToRemove(entityB);

        // 4 -> Deduct health from wall
        entityA.deductHealth();
    }
}
