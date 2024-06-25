package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class AddBallsStrategy extends RemoveBrickStrategy{
    private final GameObjectCollection gameObjects;
    private final ImageReader imageReader;
    private final SoundReader soundReader;
    private final Vector2 windowDimensions;
    private String imagePath;

    /**
     * responsible for the result of a collision of the ball with a brick
     *  @param gameObjects
     * @param counter
     */
    public AddBallsStrategy(GameObjectCollection gameObjects, Counter counter, ImageReader imageReader, SoundReader soundReader, Vector2 windowDimensions, String imagePath) {
        super(gameObjects, counter, imagePath);
        this.gameObjects = gameObjects;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.windowDimensions = windowDimensions;
        this.imagePath = imagePath;
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        Vector2 location = new Vector2(thisObj.getCenter().x(), windowDimensions.y()*0.5f);
        super.onCollision(thisObj, otherObj);
        Renderable ballImage = imageReader.readImage("assets/ball.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop_cut_silenced.wav");

        BrickerGameManager.createBall(ballImage, collisionSound, windowDimensions, gameObjects, location);



    }
}
