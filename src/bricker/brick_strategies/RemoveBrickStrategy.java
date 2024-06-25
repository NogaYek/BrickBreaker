package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

public class RemoveBrickStrategy implements CollisionStrategy{

    private GameObjectCollection gameObjects;
    private Counter counter;
    private String imagePath;

    /**
    responsible for the result of a collision of the ball with a brick
    */
    public RemoveBrickStrategy(GameObjectCollection gameObjects, Counter counter, String ImagePath) {
        this.gameObjects = gameObjects;
        this.counter = counter;
        imagePath = ImagePath;
    }

    @Override
    public String getImagePath() {
        return imagePath;
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        gameObjects.removeGameObject(thisObj);
        counter.decrement();
    }
}
