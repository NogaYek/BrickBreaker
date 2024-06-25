package bricker.power_ups;

import bricker.gameobjects.Paddle;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class PowerUpFactory {

    private static final float POWERUP_SPEED = 80;
    private Paddle paddle;
    private WindowController windowController;
    private ImageReader imageReader;
    private Vector2 windowDimensions;
    private GameObjectCollection gameObjects;
    private boolean fastGame;

    public PowerUpFactory(ImageReader imageReader, Vector2 windowDimensions, GameObjectCollection gameObjects, WindowController windowController, Paddle paddle) {
        this.paddle = paddle;
        this.windowController = windowController;
        this.imageReader = imageReader;
        this.windowDimensions = windowDimensions;
        this.gameObjects = gameObjects;
        this.fastGame = false;
    }

    /**
     the power up factory chooses a power-up randomly whenever called from the BrickGameManager.
     if we are in slow game we don't want any more slow-game power-ups, and vice versa.
     Also we don't want the paddle to be longer than half of the screen.
     So the list of options for power-ups is edited before choosing a random power-up from it
     */

    public PowerUp build() {
        Random random = new Random();

        List<String> powerUps = new ArrayList<>( Arrays.asList("FastGame", "SlowGame", "LongPaddle"));
        if(this.fastGame) { //don't release more fast game power ups
            powerUps.remove("FastGame");
        }
        if(!this.fastGame) { //don't release more slow game power ups
            powerUps.remove("SlowGame");
        }
        if(this.paddle.getDimensions().x()>windowDimensions.x()*0.5) { //don't release more paddle power ups
            powerUps.remove("LongPaddle");
        }
        //powerUps is never empty because fast game is either true or false
        String name = powerUps.get(random.nextInt(powerUps.size()));

        PowerUp powerUp = null;
        Renderable image;
        float xLocation = random.nextInt((int) windowDimensions.x());
        Vector2 topLeftCorner = new Vector2(xLocation, windowDimensions.y()*0.5f);

        switch (name) {
            case "FastGame":
                image = imageReader.readImage("assets/quicken.png", true);
                powerUp = new FastGame(topLeftCorner, new Vector2(75, 45), image, gameObjects, paddle, windowController);
                fastGame = true;
                break;

            case "SlowGame":
                image = imageReader.readImage("assets/slow.png", true);
                powerUp = new SlowGame(topLeftCorner, new Vector2(75, 45), image, gameObjects, paddle, windowController);
                fastGame = false;
                break;

            case "LongPaddle":
                image = imageReader.readImage("assets/paddle.png", true);
                powerUp = new LongPaddle(topLeftCorner, new Vector2(50, 10), image, gameObjects, paddle, windowController);
                break;
        }

        powerUp.setVelocity(new Vector2(0,POWERUP_SPEED));

        return powerUp;
    }




}
