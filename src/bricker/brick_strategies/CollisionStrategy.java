package bricker.brick_strategies;

import danogl.GameObject;

public interface CollisionStrategy {
    public String getImagePath();
    public void onCollision(GameObject thisObj, GameObject otherObj);
}
