package bricker.power_ups;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Timer;
import java.util.TimerTask;

public abstract class PowerUp extends GameObject {
    protected String name;
    private GameObjectCollection gameObjects;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */


    /**
     this abstract class defines a power-up bonus that drops from the middle of the screen
     and has some effect on the other objects if caught by the paddle
     */
    public PowerUp(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,String name, GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, renderable);
        this.name = name;
        this.gameObjects = gameObjects;

    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if(other.getTag().equals("Paddle")){
            this.gameObjects.removeGameObject(this, Layer.STATIC_OBJECTS);
            applyEffect();

        }
    }

    public abstract void applyEffect();


}
