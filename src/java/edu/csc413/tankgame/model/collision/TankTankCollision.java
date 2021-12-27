package edu.csc413.tankgame.model.collision;

import edu.csc413.tankgame.model.GameWorld;
import edu.csc413.tankgame.model.entity.Entity;
import edu.csc413.tankgame.model.entity.Tank;
import edu.csc413.tankgame.model.entity.Wall;

public class TankTankCollision extends CollisionHandler
{
    // ======== CONSTRUCTOR ======== //
    public TankTankCollision()
    {
        super(Tank.class, Tank.class);
    }

    // ======== INHERITED METHODS ======== //
    @Override
    protected void handleIndividualCollisions(Entity entityA, Entity entityB, GameWorld gameWorld)
    {
        // 1 -> Find the smallest overlap, move tanks based on smallest overlap
        findSmallestOverlap(entityA, entityB);

        // 2 -> Determine overlap distance -- each tank should move 50%
        double distanceToAdjust = getOfficialMin() / 2;

        // 3 -> Call classes to move (un-overlap) each respective items
        unCollideEntities(entityA, entityB, distanceToAdjust);
    }
}
