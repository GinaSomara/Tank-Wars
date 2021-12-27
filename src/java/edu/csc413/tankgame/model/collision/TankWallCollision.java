package edu.csc413.tankgame.model.collision;

import edu.csc413.tankgame.model.GameWorld;
import edu.csc413.tankgame.model.entity.Entity;
import edu.csc413.tankgame.model.entity.Tank;
import edu.csc413.tankgame.model.entity.Wall;

public class TankWallCollision extends CollisionHandler
{
    // ======== CONSTRUCTOR ======== //
    public TankWallCollision()
    {
        super(Tank.class, Wall.class);
    }

    // ======== INHERITED METHODS ======== //
    @Override
    protected void handleIndividualCollisions(Entity entityA, Entity entityB, GameWorld gameWorld)
    {
        // 1 -> Find overlap **NOTE: All sides here are based on entityA, our "anchored" entity! **
        findSmallestOverlap(entityA, entityB);

        // 2 -> Determine overlap distance
        // wall should not move at all, tank should be reassigned full overlap distance
        double distanceToAdjust = getOfficialMin();

        // 3 -> Call classes to move (un-overlap) each respective items
        unCollideEntities(entityA, entityB, distanceToAdjust);
    }
}
