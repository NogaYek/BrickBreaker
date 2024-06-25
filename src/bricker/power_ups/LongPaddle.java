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
import java.util.Timer;
import java.util.TimerTask;


public class LongPaddle extends PowerUp{


    private final Paddle paddle;
    private WindowController windowController;

    Timer timer;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */

    /**
     this power-up makes the paddle twice as long, for 5 seconds
     */
    public LongPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, GameObjectCollection gameObjects,  Paddle paddle, WindowController windowController) {
        super(topLeftCorner, dimensions, renderable, "LongPaddle", gameObjects);
        this.paddle = paddle;

        this.windowController = windowController;
        this.timer = new Timer();
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

    }

    @Override
    public void applyEffect() {
        paddle.setDimensions(paddle.getDimensions().multX(2));
        scheduleRemoveEffect(5000);
    }
    public void scheduleRemoveEffect(int delay){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                removeEffect();
            }
        }, delay);
    }
    public void removeEffect() {
        paddle.setDimensions(paddle.getDimensions().multX(0.5f));
    }


}

