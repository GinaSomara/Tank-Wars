package game.model.entity;

import game.Constants;

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
