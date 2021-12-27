package edu.csc413.tankgame.view;

import edu.csc413.tankgame.Constants;
import edu.csc413.tankgame.model.entity.Entity;

import java.util.ArrayList;

public class Grid
{
    // ======== STATIC VARIABLES ======== //
   public static double height = Constants.SCREEN_HEIGHT / Constants.GRID_COLUMN_AMOUNT;
   public static double width = Constants.SCREEN_WIDTH / Constants.GRID_ROW_AMOUNT;

    // ======== INSTANCE VARIABLES ======== //
    private final ArrayList<Entity> entitiesInGridBox;

    // ======== CONSTRUCTOR ======== //
    public Grid() {
        entitiesInGridBox = new ArrayList<>();
    }

    // ======== PUBLIC METHODS ======== //
    public void addEntityToGridBox(Entity entity)
    {
        if(!entitiesInGridBox.contains(entity))
            entitiesInGridBox.add(entity);
    }

    public boolean containsTwoOrMoreEntities() { return entitiesInGridBox.size() > 1; }

    public ArrayList<Entity> getEntitiesInGridBox() { return entitiesInGridBox; }

    public void clearEntitiesInGridBox() { entitiesInGridBox.clear(); }
}