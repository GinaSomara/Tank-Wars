package edu.csc413.tankgame.model.entity;

import edu.csc413.tankgame.Constants;
import edu.csc413.tankgame.KeyboardReader;
import edu.csc413.tankgame.model.GameWorld;

public abstract class Tank extends Entity
{
    // ======== STATIC VARIABLES ======== //
    private static final KeyboardReader keyboardReader = KeyboardReader.instance();
    private static final int INITIAL_SHELL_COOLDOWN = 35;

    // ======== INSTANCE VARIABLES ======== //
    private int shellCoolDown;

    // ======== CONSTRUCTOR ======== //
    public Tank(String id, double x, double y, double angle) {
        super(id, x, y, angle);
        shellCoolDown = INITIAL_SHELL_COOLDOWN;
    }

    // ======== INHERITED METHODS ======= //
    @Override
    public void checkBounds(GameWorld gameWorld)
    {
        if(getLeftX() <= Constants.TANK_X_LOWER_BOUND)
            setX(Constants.TANK_X_LOWER_BOUND);
        if(getLeftX() >= Constants.TANK_X_UPPER_BOUND)
            setX(Constants.TANK_X_UPPER_BOUND);

        if(getUpperY() <= Constants.TANK_Y_LOWER_BOUND)
            setY(Constants.TANK_Y_LOWER_BOUND);
        if(getUpperY() >= Constants.TANK_Y_UPPER_BOUND)
            setY(Constants.TANK_Y_UPPER_BOUND);
    }

    /** handleCollision will undo overlap dependent upon which side is overlapping, and what the overlapping entity is; this is the
     * purpose of the parameter adjustPosition. If Tank V Tank, then both tanks will move 50% of the overlap, however with a Tank V Wall
     * collision, the tank will move the full 100% of the overlap. */
    @Override
    public void handleCollisions(String sideOverlapping, double adjustPosition)
    {
        switch (sideOverlapping)
        {
            case "leftOverlap" -> setX(getLeftX() - adjustPosition);
            case "rightOverlap" -> setX(getLeftX() + adjustPosition);
            case "upperOverlap" -> setY(getUpperY() - adjustPosition);
            case "lowerOverlap" -> setY(getUpperY() + adjustPosition);
            default -> throw new RuntimeException("Error, unregistered overlapping side. " +
                    "Side Parameter provided is: " + sideOverlapping + " Only left/right/upper/lower allowed.");
        }
    }

    @Override
    public double getRightX() { return getLeftX() + Constants.TANK_WIDTH; }
    @Override
    public double getLowerY() { return getUpperY() + Constants.TANK_HEIGHT; }

    // ======== PROTECTED METHODS ======== //
    protected KeyboardReader getKeyboardReader() { return keyboardReader;}

    protected void updateShellCoolDown() { if(shellCoolDown > 0) shellCoolDown--; }

    protected void fireShell(GameWorld gameWorld)
    {
        double middleX = Math.abs(getLeftX()-getRightX());

        if(shellCoolDown == 0) {
            //the x,y,angle are all coming from the tank that fired the shot
            Shell newShell = new Shell(getShellX(), getShellY(), getAngle());
            gameWorld.addEntity(newShell);
            shellCoolDown = INITIAL_SHELL_COOLDOWN;
        }
    }

    // ======== PRIVATE METHODS ======== //
    /** You can use getShellX() and getShellY() to determine the x and y coordinates of a Shell that
    *is fired by this tank. The Shell's angle is the same as the Tank's angle. **/
    private double getShellX() {
        return getLeftX() + Constants.TANK_WIDTH / 2 + 45.0 * Math.cos(getAngle()) - Constants.SHELL_WIDTH / 2;
    }

    private double getShellY() {
        return getUpperY() + Constants.TANK_HEIGHT / 2 + 45.0 * Math.sin(getAngle()) - Constants.SHELL_HEIGHT / 2;
    }
}

