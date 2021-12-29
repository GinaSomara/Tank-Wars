package game.model.collision;

import game.GameWorld;
import game.model.entity.Entity;
import game.model.entity.Shell;

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
