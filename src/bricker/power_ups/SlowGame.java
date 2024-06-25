package bricker.power_ups;

import bricker.gameobjects.Ball;
import bricker.gameobjects.Paddle;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.ArrayList;
import java.util.List;


public class SlowGame extends PowerUp{

    private final Paddle paddle;
    private WindowController windowController;


    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */

    /**
     this power-up makes the ball go slower by 0.8, for 5 seconds
     */
    public SlowGame(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, GameObjectCollection gameObjects, Paddle paddle, WindowController windowController) {
        super(topLeftCorner, dimensions, renderable, "SlowBall", gameObjects);
        this.paddle = paddle;


        this.windowController = windowController;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

    }

    @Override
    public void applyEffect() {
        windowController.setTimeScale(1);
    }




}

