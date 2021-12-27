package edu.csc413.tankgame.model.collision;

import edu.csc413.tankgame.model.GameWorld;
import edu.csc413.tankgame.model.entity.Entity;
import edu.csc413.tankgame.model.entity.Shell;
import edu.csc413.tankgame.model.entity.Tank;

public class ShellShellCollision extends CollisionHandler
{
    // ======== CONSTRUCTOR ======== //
    public ShellShellCollision()
    {
        super(Shell.class, Shell.class);
    }

    // ======== INHERITED METHODS ======== //
    @Override
    protected void handleIndividualCollisions(Entity entityA, Entity entityB, GameWorld gameWorld)
    {
        // 1 -> Remove both from the gameWorld (Sprites removed when arraylist is emptied)
        gameWorld.addEntityToRemove(entityA);
        gameWorld.addEntityToRemove(entityB);
    }


}
