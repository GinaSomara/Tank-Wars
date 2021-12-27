package edu.csc413.tankgame.model.entity;

import edu.csc413.tankgame.Constants;
import edu.csc413.tankgame.model.GameWorld;


public class Heart extends Entity
{
    // ======== CONSTRUCTOR ======== //
    public Heart(double x, double y) {
        super(generateId(), x, y, 0);
    }

    // ======== INHERITED METHODS ======== //
    @Override
    public double getRightX() { return getLeftX() + Constants.HEART_WIDTH; }

    @Override
    public double getLowerY() { return getUpperY() + Constants.HEART_HEIGHT; }

    // ======== PRIVATE METHODS ======== //
    private static int nextId = 0;
    private static String generateId() { return "heart-" + nextId++; }
}
