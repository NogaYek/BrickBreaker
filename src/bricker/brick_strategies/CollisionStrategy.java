package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

public class CollisionStrategy {

    private GameObjectCollection gameObjects;
    private Counter counter;

    /**
    responsible for the result of a collision of the ball with a brick
    */
    public CollisionStrategy(GameObjectCollection gameObjects, Counter counter) {
        this.gameObjects = gameObjects;
        this.counter = counter;
    }

    public void onCollision(GameObject thisObj, GameObject otherObj){
        gameObjects.removeGameObject(thisObj);
        counter.decrement();
    }
}
