package game.model.collision;

import game.GameWorld;
import game.model.entity.Entity;
import game.model.entity.Tank;

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
