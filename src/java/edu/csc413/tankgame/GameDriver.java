package edu.csc413.tankgame;

import edu.csc413.tankgame.model.*;
import edu.csc413.tankgame.model.collision.*;
import edu.csc413.tankgame.model.entity.*;
import edu.csc413.tankgame.view.*;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Random;

public class GameDriver
{
    // ======== STATIC VARIABLES ======== //
    private static final KeyboardReader keyboardReader = KeyboardReader.instance();

    // ======== INSTANCE VARIABLES ======== //
    private final MainView mainView;
    private final RunGameView runGameView;
    private final GameWorld gameWorld;
    private final ArrayList<CollisionHandler> collisionHandlers;

    // ======== CONSTRUCTOR ======== //
    public GameDriver() {
        mainView = new MainView(this::startMenuActionPerformed);
        runGameView = mainView.getRunGameView();
        gameWorld = new GameWorld();

        collisionHandlers = new ArrayList<>();
        collisionHandlers.add(new TankTankCollision());
        collisionHandlers.add(new TankShellCollision());
        collisionHandlers.add(new TankWallCollision());
        collisionHandlers.add(new ShellShellCollision());
        collisionHandlers.add(new WallShellCollision());
        collisionHandlers.add(new TankHeartCollision());
    }

    // ======== PUBLIC METHODS ======== //
    public void start() {
        mainView.setScreen(MainView.Screen.START_GAME_SCREEN);
    }

    // ======== PRIVATE METHODS ======== //
    private void startMenuActionPerformed(ActionEvent actionEvent) {
        switch (actionEvent.getActionCommand()) {
            case StartMenuView.START_BUTTON_ACTION_COMMAND -> runGame();
            case StartMenuView.EXIT_BUTTON_ACTION_COMMAND -> mainView.closeGame();
            default -> throw new RuntimeException("Unexpected action command: " + actionEvent.getActionCommand());
        }
    }

    private void runGame() {
        mainView.setScreen(MainView.Screen.RUN_GAME_SCREEN);
        Runnable gameRunner = () -> {
            setUpGame();
            while (updateGame() && !keyboardReader.escapePressed()) {
                runGameView.repaint();
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException exception) {
                    throw new RuntimeException(exception);
                }
            }
            mainView.setScreen(MainView.Screen.END_MENU_SCREEN);
            resetGame();
        };
        new Thread(gameRunner).start();
    }

    /**
     * setUpGame is called once at the beginning when the game is started. Entities that are present from the start
     * should be initialized here, with their corresponding sprites added to the RunGameView.
     */
    private void setUpGame()
    {
        //  1- > Set up Grid system
        gameWorld.setUpGrid();

        //  2 -> Create walls + Add to Game World
        for(WallInformation walls : WallInformation.readWalls())
        {
            Wall wall = new Wall(walls.getX(), walls.getY());
            gameWorld.addEntity(wall);
            runGameView.addSprite(wall.getId(), walls.getImageFile(), wall.getLeftX(), wall.getUpperY(), 0.0);
        }

        //  3 -> Create tanks + Add to Game World
        PlayerTank playerTank = new PlayerTank(
                            Constants.PLAYER_TANK_ID,
                            Constants.PLAYER_TANK_INITIAL_X,
                            Constants.PLAYER_TANK_INITIAL_Y,
                            Constants.PLAYER_TANK_INITIAL_ANGLE);
        gameWorld.addEntity(playerTank);

        AITank aiTank1 = new AITank(
                        Constants.AI_TANK_1_ID,
                        Constants.AI_TANK_1_INITIAL_X,
                        Constants.AI_TANK_1_INITIAL_Y,
                        Constants.AI_TANK_1_INITIAL_ANGLE);
        gameWorld.addEntity(aiTank1);

        AITank aiTank2 = new AITank(
                    Constants.AI_TANK_2_ID,
                    Constants.AI_TANK_2_INITIAL_X,
                    Constants.AI_TANK_2_INITIAL_Y,
                    Constants.AI_TANK_2_INITIAL_ANGLE);
        gameWorld.addEntity(aiTank2);

        //  4 -> Moving new tanks + walls from stagedEntities arrayList into entities arrayList
        gameWorld.addStagedEntities();

        //  5 -> Adding all new entities as sprites in order to be displayed
        runGameView.addSprite(
                playerTank.getId(),
                RunGameView.PLAYER_TANK_IMAGE_FILE,
                playerTank.getLeftX(),
                playerTank.getUpperY(),
                playerTank.getAngle());
        runGameView.addSprite(
                aiTank1.getId(),
                RunGameView.AI_TANK_IMAGE_FILE,
                aiTank1.getLeftX(),
                aiTank1.getUpperY(),
                aiTank1.getAngle());
        runGameView.addSprite(
                aiTank2.getId(),
                RunGameView.AI_TANK_IMAGE_FILE,
                aiTank2.getLeftX(),
                aiTank2.getUpperY(),
                aiTank2.getAngle());
        // Walls already previously added (in for loop)
    }

    /**
     * updateGame is repeatedly called in the gameplay loop. The code in this method should run a single frame of the
     * game. As long as it returns true, the game will continue running. If the game should stop for whatever reason
     * (e.g. the player tank being destroyed, escape being pressed), it should return false.
     */
    private boolean updateGame()
    {
        //  1 -> Move all entities in list, then check bounds, then assign to a Grid Box
        for(Entity entity : gameWorld.getEntities()) {
            entity.move(gameWorld);
            entity.checkBounds(gameWorld);
            assignToGrid(entity);
        }

        // 2 -> Check for health status
        for(Entity entity : gameWorld.getEntities())
        {
            if(!entity.stillAlive()) {
                if(entity instanceof PlayerTank)
                    return false;
                else gameWorld.addEntityToRemove(entity);
            }

            /* If playerTank's health is < 30, add health boost to game
                    **NOTE: Only one heart will be displayed at a time */
            if(entity instanceof PlayerTank && entity.getHealth() < 30 && !gameWorld.heartAlreadyDisplayed())
                createHeart(entity);
        }

         // 3 -> Collision Detection
        Grid[][] grid = gameWorld.getGrid();
        for(int row=0 ; row<grid.length ; row++) {     //constant time loop O(100)
            for(int col=0 ; col<grid[row].length ; col++) {
                if(grid[row][col].containsTwoOrMoreEntities())
                    handleCollisions(grid[row][col].getEntitiesInGridBox());
            }
        }

        // 4 -> Add staged entities to the runGameView for display (sprites), will be shown next updateGame() call
        for(Entity entity : gameWorld.getEntitiesToAdd())
        {
            if(entity instanceof Shell)
                runGameView.addSprite(entity.getId(), RunGameView.SHELL_IMAGE_FILE, entity.getLeftX(), entity.getUpperY(), entity.getAngle());
            if(entity instanceof Heart)
                runGameView.addSprite(entity.getId(), RunGameView.HEART_IMAGE_FILE, entity.getLeftX(), entity.getUpperY(), entity.getAngle());
        }
        gameWorld.addStagedEntities();     //entities have now been displayed on screen, so we can move over new entities

        // 5 -> Remove entities that are out of bounds or have been destroyed
        for(Entity entity : gameWorld.getEntitiesToBeRemoved()) {
            runGameView.removeSprite(entity.getId());
        }
        gameWorld.removeStagedEntities();

        // 6 -> Checking to ensure there are still AI Tanks present, if none - Player wins & end game
        boolean aiTanksLeft = gameWorld.getEntities().stream().filter(AITank.class::isInstance).findAny().isEmpty();
        if(aiTanksLeft)
            return false;

        // 7 -> updated Sprite location, to be displayed next updateGame() call
        for(Entity entity : gameWorld.getEntities()) {
            runGameView.setSpriteLocationAndAngle(entity.getId(), entity.getLeftX(), entity.getUpperY(), entity.getAngle());
        }

        // 8 -> Clear entire Grid for next round
        gameWorld.clearGrid();

        return true;
    }

    /**
     * resetGame is called at the end of the game once the gameplay loop exits. This should clear any existing data from
     * the game so that if the game is restarted, there aren't any things leftover from the previous run.
     */
    private void resetGame()
    {
        gameWorld.clearAllListsForReset();
        runGameView.reset();
    }

    /**
     *  assignGrid is to assign each entity in the game into a specific grid (represented as a 2D array 11x10). The grid is cleared out every updateGame()
     *  and entities are reassigned to the grid based upon their new location
     **/
    private void assignToGrid(Entity entity)
    {
        // 1 -> Locate which grid(s) each respective side would fit into
        int rowOfUpperSide = (int) entity.getUpperY() / 100;
        int rowOfLowerSide = (int) entity.getLowerY() / 100;
        int columnOfLeftSide = (int) entity.getLeftX() / 100;
        int columnOfRightSide = (int) entity.getRightX() / 100;

        /* 2 -> Attempt to add to all combinations
                  (i.e: 1 gridBox, 2 gridBoxes, 3 gridBoxes, 4 gridBoxes)
                  ** if .addEntityToGridBox() already contains that entity, it will NOT be added
                   * Example, If entity is only in one grid, then .addEntity will attempt to add it four times,
                     only successfully adding it the first call.*/
        gameWorld.getGrid()[rowOfUpperSide][columnOfLeftSide].addEntityToGridBox(entity);
        gameWorld.getGrid()[rowOfUpperSide][columnOfRightSide].addEntityToGridBox(entity);
        gameWorld.getGrid()[rowOfLowerSide][columnOfLeftSide].addEntityToGridBox(entity);
        gameWorld.getGrid()[rowOfLowerSide][columnOfRightSide].addEntityToGridBox(entity);
    }

    /**
     * handleCollisions is for checking any possible combinations of collisions. Each pair is tested against the fixed CollisionHandler arrayList.
     * By testing possible combinations against specific predetermined classes of collisions, we can create scalable, organized design.
     *  **/
    private void handleCollisions(ArrayList<Entity> entitiesPossiblyColliding)
    {
        // 1 -> Run through all pairs of entities to check possible collisions
            for(int a=0 ; a < entitiesPossiblyColliding.size() ; a++) {
                for(int b=a+1 ; b < entitiesPossiblyColliding.size() ; b++) {
                    // ** Below For Loop is O Constant TIME
                    for(CollisionHandler collisionTypes : collisionHandlers)
                        collisionTypes.handleCollision(entitiesPossiblyColliding.get(a),entitiesPossiblyColliding.get(b), gameWorld);
                }
            }
    }

    /**
     * createHeart is a simple method to create a new heart object to boost players health. We are generating a random (x,y) for the heart's location.
     * The heart does have the possibility of overlapping with another entity. However, this is not handled due to the focus of this project being
     * focused on software design and not minor UI details.
     *  **/
    private void createHeart(Entity entity)
    {
        // 1 -> Generate random coordinates to place heart
        Random rand = new Random();
        int randomX = rand.nextInt((int) Constants.TANK_X_UPPER_BOUND);
        int randomY = rand.nextInt((int) Constants.TANK_Y_UPPER_BOUND);

        // 2 -> Create new Heart for PlayerTank health boost
        Heart heart = new Heart(randomX, randomY);

        /* 3 -> Add entity to the gameWorld
                entity will be added to grid box on next round */
        gameWorld.addEntity(heart);
    }



    // ======== MAIN ======== //
    public static void main(String[] args) {
        GameDriver gameDriver = new GameDriver();
        gameDriver.start();
    }
}
