package bricker.brick_strategies;


import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.Random;


public class BrickStrategyFactory {
    private Vector2 windowDimensions;
    private SoundReader soundReader;
    private danogl.gui.ImageReader imageReader;
    private GameObjectCollection gameObjects;
    private Counter counter;


    public BrickStrategyFactory(GameObjectCollection gameObjects, Counter counter, ImageReader imageReader, SoundReader soundReader, Vector2 windowDimensions){
        this.gameObjects = gameObjects;
        this.counter = counter;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.windowDimensions = windowDimensions;
    }

    public CollisionStrategy getStrategy(){
        Random random = new Random();
        int rand = random.nextInt(100);
        CollisionStrategy strategy = null;
        if(rand<20){ //setting the probability of add balls to 20%
            strategy =new AddBallsStrategy(gameObjects, counter, imageReader, soundReader, windowDimensions, "assets/greenBrick.png");

        }
        else{
            strategy = new RemoveBrickStrategy(gameObjects, counter, "assets/yellowBrick.png");

        }
        return strategy;
    }
}
