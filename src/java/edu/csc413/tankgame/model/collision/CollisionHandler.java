package edu.csc413.tankgame.model.collision;

import edu.csc413.tankgame.model.GameWorld;
import edu.csc413.tankgame.model.entity.Entity;

public abstract class CollisionHandler
{

    // ======== INSTANCE VARIABLES ======== //
    private double officialMin;
    private double leftOverlap,
                    rightOverlap,
                    upperOverlap,
                    lowerOverlap;
    /** Used for storing the class types of the entities colliding. This is to replace the repetitive use of instanceOf.
     * Each class will specify their two entities types (clear from the subclass names) */
    private final Class<? extends Entity> entityAType;
    private final Class<? extends Entity> entityBType;


    // ======== CONSTRUCTOR ======== //
    public CollisionHandler(Class<? extends Entity> entityAType, Class<? extends Entity> entityBType)
    {
        this.entityAType = entityAType;
        this.entityBType = entityBType;
    }

    // ======== ABSTRACT METHODS ======== //
    protected abstract void handleIndividualCollisions(Entity entityA, Entity entityB, GameWorld gameWorld);

    // ======== PUBLIC METHODS ======== //
    public void handleCollision(Entity entityA, Entity entityB, GameWorld gameworld)
    {
        /* Pointers to be passed into handleIndividualCollisions(), this is to ensure that
           the correct order of Entities is passed in. handleIndividualCollisions() is dependent
           upon the correct order or entities being passed in.
              **NOTE: class name specifies order Entities will be assigned in */
        Entity entityOne;
        Entity entityTwo;

        // 1 -> Check if Entities are in appropriate class
        if(entityAType.isInstance(entityA) && entityBType.isInstance(entityB))
        {
            entityOne = entityA;
            entityTwo = entityB;
        }
        else if(entityBType.isInstance(entityA) && entityAType.isInstance(entityB))
        {
            entityTwo = entityA;
            entityOne = entityB;
        }
        else  //not correct class
            return;


        // 2 -> Ensuring entities are in fact colliding
        if(!areEntitiesColliding(entityA, entityB))
            return;


        // 3 -> Handle individual collision logic
        handleIndividualCollisions(entityOne, entityTwo, gameworld);
    }

    protected boolean areEntitiesColliding(Entity entityA, Entity entityB)
    {
        return entityA.getRightX() > entityB.getLeftX() && entityA.getLeftX() < entityB.getRightX() &&
                entityA.getLowerY() > entityB.getUpperY() && entityA.getUpperY() < entityB.getLowerY();
    }

    // ======== PROTECTED METHODS ======== //
    protected void findSmallestOverlap(Entity entityA, Entity entityB)
    {
        leftOverlap = Math.abs(entityA.getLeftX() - entityB.getRightX());
        rightOverlap = Math.abs(entityA.getRightX() - entityB.getLeftX());
        upperOverlap = Math.abs(entityA.getUpperY() - entityB.getLowerY());
        lowerOverlap = Math.abs(entityA.getLowerY() - entityB.getUpperY());

         officialMin = Math.min(Math.min(leftOverlap, rightOverlap), Math.min(upperOverlap, lowerOverlap));
    }

    protected void unCollideEntities(Entity entityA, Entity entityB, double distanceToAdjust)
    {
        //**NOTE: Normally comparing doubles with == is not necessarily desirable, but in this case officialMin
        // is created directly from the overlap values. All of which are private variables anyways.

        // 4 -> Call classes to move (un-overlap) each respective items
        if(officialMin == leftOverlap) {
            entityA.handleCollisions( "rightOverlap" ,distanceToAdjust);
            entityB.handleCollisions("leftOverlap", distanceToAdjust);
        } else if(officialMin == rightOverlap) {
            entityA.handleCollisions( "leftOverlap" ,distanceToAdjust);
            entityB.handleCollisions("rightOverlap", distanceToAdjust);
        } else if(officialMin == upperOverlap) {
            entityA.handleCollisions("lowerOverlap", distanceToAdjust);
            entityB.handleCollisions("upperOverlap", distanceToAdjust);
        } else if(officialMin == lowerOverlap) {
            entityA.handleCollisions("upperOverlap", distanceToAdjust);
            entityB.handleCollisions("lowerOverlap", distanceToAdjust);
        }
    }

    protected double getOfficialMin() { return officialMin; }
}

