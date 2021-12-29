package game.model.entity;

import game.Constants;
import game.GameWorld;

public class AITank extends Tank
{
    // ======== CONSTRUCTOR ======== //
    public AITank(String id, double x, double y, double angle) { super(id, x, y, angle); }

    // ======== INHERITED METHODS ======= //
    /** AI Tanks will not move their grid position, but will change their angle in order to point
     * at the PlayerTank as it moves around. */
    @Override
    public void move(GameWorld gameWorld)
    {
        Entity playerTank = gameWorld.getEntity(Constants.PLAYER_TANK_ID);

        /** To figure out what angle the AI tank needs to face, we'll use the
         change in the x and y axes between the AI and player tanks.
            **NOTE: the x and y here are based on the middle most point of the tank */

        double dx = getPlayerTankMiddleX(playerTank) - getLeftX();
        double dy = getPlayerTankMiddleY(playerTank) - getLowerY();
        double angleToPlayer = Math.atan2(dy, dx);
        double angleDifference = getAngle() - angleToPlayer;
        angleDifference -=
                Math.floor(angleDifference / Math.toRadians(360.0) + 0.5)
                        * Math.toRadians(360.0);
        if (angleDifference < -Math.toRadians(3.0))
            turnRight(Constants.TANK_TURN_SPEED);
        else if (angleDifference > Math.toRadians(3.0))
            turnLeft(Constants.TANK_TURN_SPEED);

        fireShell(gameWorld);

        updateShellCoolDown();
    }

    @Override
    public void deductHealth() { deductHealth(Constants.AI_TANK_HEALTH_DEDUCTION); }

    // ======== PRIVATE METHODS ======= //
    private double getPlayerTankMiddleX(Entity playerTank)
    {
        // 1 -> Find the difference between the left/right x values of the player tank,
        //      then find the middle point of that
        double middlePointOfLeftRightX = Math.abs(playerTank.getLeftX() - playerTank.getRightX()) / 2;

        // 2 -> Need to distinguish which way the tank is facing
        if(playerTank.getLeftX() < playerTank.getRightX()) //Tank facing Right
            return playerTank.getLeftX() + middlePointOfLeftRightX;
        else //Tank facing Left
            return playerTank.getRightX() + middlePointOfLeftRightX;
    }

    private double getPlayerTankMiddleY(Entity playerTank)
    {
        // 1 -> Find the difference between the left/right x values of the player tank,
        //      then find the middle point of that
        double middlePointOfLeftRightY = Math.abs(playerTank.getUpperY() - playerTank.getLowerY()) / 2;

        // 2 -> Need to distinguish which way the tank is facing
        if(playerTank.getUpperY() < playerTank.getLowerY())
            return playerTank.getUpperY() + middlePointOfLeftRightY;
        else
            return playerTank.getRightX() + middlePointOfLeftRightY;
    }
}
