package game.model.entity;

import game.Constants;
import game.GameWorld;

public class Shell extends Entity
{
    // ======== CONSTRUCTOR ======== //
    public Shell(double x, double y, double angle) {
        super(generateId(), x, y, angle);
    }

    // ======== INHERITED METHODS ======= //
    @Override
    public double getRightX() { return getLeftX() + Constants.SHELL_WIDTH; }
    @Override
    public double getLowerY() { return getUpperY() + Constants.SHELL_HEIGHT; }

    @Override
    public void move(GameWorld gameWorld) { moveForward(Constants.SHELL_MOVEMENT_SPEED); }

    @Override
    public void checkBounds(GameWorld gameWorld)
    {
        if(getLeftX() <= Constants.SHELL_X_LOWER_BOUND || getLeftX() >= Constants.SHELL_X_UPPER_BOUND ||
           getUpperY() <= Constants.SHELL_Y_LOWER_BOUND || getUpperY() >= Constants.SHELL_Y_UPPER_BOUND)

            gameWorld.addEntityToRemove(this);
    }

    // ======== PRIVATE METHODS ======== //
    private static int nextId = 0;
    private static String generateId() { return "shell-" + nextId++; }
}
