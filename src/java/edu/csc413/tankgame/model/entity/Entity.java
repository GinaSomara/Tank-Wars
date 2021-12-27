// PROVIDED CLASS

package edu.csc413.tankgame.model.entity;

import edu.csc413.tankgame.Constants;
import edu.csc413.tankgame.model.GameWorld;

/**
 * A general concept for an entity. This includes everything that can move or be interacted with, such
 * as tanks, shells, walls, hearts, etc.
 */
public abstract class Entity
{
    // ======== INSTANCE VARIABLES ======== //
    private String id;  // id is so that we are not creating new tank objects on the screen but are moving the current ones we have -- eliminating glitches
    private double x;
    private double y;
    private double angle;
    private int health;

    // ======== CONSTRUCTOR ======== //
    public Entity(String id, double x, double y, double angle) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.health = 100;
    }

    // ======== ABSTRACT  METHODS ======== //
    public abstract double getRightX();

    /** lowerY is based on the visual representation, the "bottom" of the tank, wall, etc.*/
    public abstract double getLowerY();

    // ======== PUBLIC METHODS ======== //
    public String getId() { return id; }

    public double getLeftX() {
        return x;
    }

    /** upperY is based on the visual representation, the "top" of the tank, wall, etc.*/
    public double getUpperY() { return y; }

    public double getAngle() { return angle; }

    public int getHealth() { return health; }
    public boolean stillAlive() { return health > 0; }
    public void addHealth(int healthBoost) { health += healthBoost;  }

    /** Since not all the subclasses move, this method will not be abstract. */
    public void move(GameWorld gameWorld) { }

    /** Since not all the subclasses move, there is no need to check bounds on certain entities.
     * This method will not be abstract. */
    public void checkBounds(GameWorld gameWorld) { }

    public void deductHealth() { }

    /** Some entities can move, but not all entities need to handle collisions. Some entities, such as
     *  shell and heart, will be directly removed from the GameWorld in their respective Collision Handler
     *  classes. Therefore, method does not need to be abstract. */
    public void handleCollisions(String side, double adjustPosition) {}

    // ======== PROTECTED METHODS ======== //
    protected void setX(double x) { this.x = x;}
    protected void setY(double y) { this.y = y;}
    protected void deductHealth(int minusHealth) { health -= minusHealth; }

    protected void moveForward(double movementSpeed)
    {
        x += movementSpeed * Math.cos(angle);
        y += movementSpeed * Math.sin(angle);
    }

    protected void moveBackward(double movementSpeed)
    {
        x -= movementSpeed * Math.cos(angle);
        y -= movementSpeed * Math.sin(angle);
    }

    protected void turnLeft(double turnSpeed) { angle -= turnSpeed; }
    protected void turnRight(double turnSpeed) { angle += turnSpeed; }
}
