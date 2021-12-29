package game.model.entity;

import game.Constants;
import game.GameWorld;

public class PlayerTank extends Tank
{
    // ======== CONSTRUCTOR ======== //
    public PlayerTank(String id, double x, double y, double angle) {
        super(id, x, y, angle);
    }

    // ======== INHERITED METHODS ======= //
    @Override
    public void move(GameWorld gameWorld)
    {
        if (getKeyboardReader().upPressed()) moveForward(Constants.TANK_MOVEMENT_SPEED);
        if (getKeyboardReader().downPressed()) moveBackward(Constants.TANK_MOVEMENT_SPEED);
        if (getKeyboardReader().leftPressed()) turnLeft(Constants.TANK_TURN_SPEED);
        if (getKeyboardReader().rightPressed()) turnRight(Constants.TANK_TURN_SPEED);
        if(getKeyboardReader().spacePressed()) fireShell(gameWorld);

        updateShellCoolDown();
    }

    @Override
    public void deductHealth() { deductHealth(Constants.PLAYER_TANK_HEALTH_DEDUCTION); }
}
