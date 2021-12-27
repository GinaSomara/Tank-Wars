package edu.csc413.tankgame.model;

import edu.csc413.tankgame.Constants;
import edu.csc413.tankgame.model.entity.Entity;
import edu.csc413.tankgame.model.entity.Heart;
import edu.csc413.tankgame.view.Grid;

import java.util.*;

/**
 * GameWorld holds all the model objects present in the game. GameWorld tracks all moving entities like tanks and
 * shells, and provides access to this information for any code that needs it (such as GameDriver or entity classes).
 */
public class GameWorld
{
    // ======== INSTANCE VARIABLES ======== //
    private ArrayList<Entity> entities;
    /**
     * The entities arrayList above is iterated through often in updateGame() within GameDriver. Because we cannot
     * remove/add the Entity object within it whilst iterating through it, the two below arrayLists will represent
     * any "staged or queued" entities that are waiting to be either added or removed from the official entities arrayList
     */
    private ArrayList<Entity> entitiesToAdd;
    private ArrayList<Entity> entitiesToRemove;
    private final Grid[][] grid;

    // ======== CONSTRUCTOR ======== //
    public GameWorld()
    {
        entities = new ArrayList<>();
        entitiesToAdd = new ArrayList<>();
        entitiesToRemove = new ArrayList<>();

        grid = new Grid[Constants.GRID_ROW_AMOUNT][Constants.GRID_COLUMN_AMOUNT];
    }

    // ======== PUBLIC METHODS ======== //
    public void setUpGrid()
    {
        for(int r=0 ; r < grid.length ; r++) {
            for(int c=0 ; c < grid[r].length ; c++) {
                grid[r][c] = new Grid();
            }
        }
    }
    public Grid[][] getGrid() { return grid; }

    /** Any entities to be added will wait in the entitiesToAdd list until they can be added into the official entities arrayList */
    public void addEntity(Entity entity) { entitiesToAdd.add(entity); }
    /** Any entities to be removed will wait in the entitiesToRemove list until they can be removed from the official entities arrayList */
    public void addEntityToRemove(Entity entity) { entitiesToRemove.add(entity); }
    public List<Entity> getEntities() { return entities; }
    public List<Entity> getEntitiesToAdd() { return entitiesToAdd; }
    public List<Entity> getEntitiesToBeRemoved() { return entitiesToRemove; }

    /** getEntity is for retrieving a specific entity */
    public Entity getEntity(String id)
    {
        for(Entity entity : entities)
        {
            if(entity.getId().equals(id))
                return entity;
        }
        throw new RuntimeException("No Entity found matching specified id of '" + id + "'");
    }

    /** Adding all staged entities to the official entities ArrayList + Removing all staged entities from staged array list */
    public void addStagedEntities()
    {
        entities.addAll(entitiesToAdd);
        entitiesToAdd.clear();
    }

    /** Removing all staged entities to the official entities ArrayList + Removing all staged entities from staged array list */
    public void removeStagedEntities()
    {
        entities.removeAll(entitiesToRemove);
        entitiesToRemove.clear();
    }

    public void clearAllListsForReset()
    {
        entities.clear();
        entitiesToAdd.clear();
        entitiesToRemove.clear();
    }

    /** The Grid will be cleared each iteration of updateGame() within GameDriver to ensure that as each entity moves, the grid
     * is also updated
     *      NOTE: There could be potential for a more efficient solution here in terms of walls. Walls do not move, they are only destroyed.
     *             A possible solution to make this a bit more efficient would be to leave the walls within in the grid, and only remove
     *             them from the grid when they are destroyed. */
    public void clearGrid()
    {
        for(int row=0 ; row<grid.length ; row++) {
            for(int col=0 ; col<grid[row].length ; col++) {
                grid[row][col].clearEntitiesInGridBox();
            }
        }
    }

    /** removeEntity is currently unused. However, it could be used for the above solution of creating a more efficient Grid. Therefore, I will
     * leave it here for a potential upgrade to Grid in the future */
    public void removeEntity(String id)
    {
        for(Entity entity : entities)
        {
            if(entity.getId().equals(id))
            {
                entities.remove(entity);
                break;
            }
        }
    }

    /** heartAlreadyDisplayed ensures that only one heart will be displayed at a time */
    public boolean heartAlreadyDisplayed()
    {
        // 1 -> Check both lists for hearts, if found return true;
        for(Entity entity : entities)
        {
            if(entity instanceof Heart)
                return true;
        }

        for(Entity entity : entitiesToAdd)
        {
            if(entity instanceof Heart)
                return true;
        }

        return false;
    }
}
