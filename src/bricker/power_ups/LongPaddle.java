package bricker.power_ups;

import bricker.gameobjects.Ball;
import bricker.gameobjects.Paddle;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;


public class LongPaddle extends PowerUp{

    private final Ball ball;
    private final Paddle paddle;



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
    public LongPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, GameObjectCollection gameObjects, Ball ball, Paddle paddle) {
        super(topLeftCorner, dimensions, renderable, "LongPaddle", gameObjects);
        this.ball = ball;
        this.paddle = paddle;

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

    @Override
    public void removeEffect() {
        paddle.setDimensions(paddle.getDimensions().multX(0.5f));
    }


}

