package bricker.power_ups;

import bricker.gameobjects.Ball;
import bricker.gameobjects.Paddle;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Random;

public class PowerUpFactory {
    private static final float POWERUP_SPEED = 80;
    private Ball ball;
    private Paddle paddle;

    public PowerUpFactory(Ball ball, Paddle paddle) {
        this.ball = ball;
        this.paddle = paddle;
    }

    /**
     the power up factory chooses a power-up randomly whenever called from the BrickGameManager
     */

    public PowerUp build(ImageReader imageReader, Vector2 windowDimensions, GameObjectCollection gameObjects) {

        Random random = new Random();
        String[] powerUps = {"FastBall", "SlowBall", "LongPaddle"};
        String name = powerUps[random.nextInt(powerUps.length)];

        PowerUp powerUp = null;
        Renderable image;
        float xLocation = random.nextInt((int) windowDimensions.x());
        Vector2 topLeftCorner = new Vector2(xLocation, windowDimensions.y()*0.5f);

        switch (name) {
            case "FastBall":
                image = imageReader.readImage("assets/quicken.png", true);
                powerUp = new FastBall(topLeftCorner,new Vector2(75,45), image, gameObjects, ball, paddle);
                break;
            case "SlowBall":
                image = imageReader.readImage("assets/slow.png", true);
                powerUp = new SlowBall(topLeftCorner,new Vector2(75,45), image, gameObjects, ball, paddle);
                break;
            case "LongPaddle":
                if(paddle.getDimensions().x()>windowDimensions.x()) //if the paddle is too long don't create more LongPaddle
                    break;
                image = imageReader.readImage("assets/paddle.png", true);
                powerUp = new LongPaddle(topLeftCorner,new Vector2(50,10), image, gameObjects, ball, paddle);
                break;

        }
        powerUp.setVelocity(new Vector2(0,POWERUP_SPEED));

        return powerUp;
    }


}
