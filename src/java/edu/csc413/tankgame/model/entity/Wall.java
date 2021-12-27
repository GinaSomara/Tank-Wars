package edu.csc413.tankgame.model.entity;

import edu.csc413.tankgame.Constants;
import edu.csc413.tankgame.model.GameWorld;
import edu.csc413.tankgame.model.entity.Entity;

public class Wall extends Entity
{
    // ======== CONSTRUCTOR ======== //
    public Wall(double x, double y) {
        super(generateId(),x,y,0.0);
    }

    // ======== INHERITED METHODS ======= //
    @Override
    public double getRightX() { return getLeftX() + Constants.WALL_WIDTH; }
    @Override
    public double getLowerY() { return getUpperY() + Constants.WALL_HEIGHT; }

    @Override
    public void deductHealth() { deductHealth(10); }

    // ======== PRIVATE METHODS ======== //
    private static int nextId = 0;
    private static String generateId() { return "wall-" + nextId++; }
}
