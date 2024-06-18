package bricker.main;

import bricker.brick_strategies.CollisionStrategy;
import bricker.gameobjects.Ball;
import bricker.gameobjects.Brick;
import bricker.gameobjects.Paddle;
import bricker.power_ups.FastBall;
import bricker.power_ups.PowerUp;
import bricker.power_ups.PowerUpFactory;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import danogl.util.Counter;
import java.awt.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class BrickerGameManager extends GameManager {
    private static final int BORDER_WIDTH= 15;
    private static final float BALL_SPEED = 200;
    private static final float BRICK_NUM = 8;
    private static final int ROW_NUM = 5;
    private static final float BRICK_WIDTH = 30;
    private static final int LIVES=3;
    private static final int HEART_SIZE=30;

    private Ball ball;
    private Paddle paddle;
    private Vector2 windowDimensions;
    private WindowController windowController;
    private Counter brickCounter;
    private Counter livesCounter;
    private ImageReader imageReader;
    private GameObject[] hearts;
    private PowerUpFactory powerUpFactory;
    private Random random;
    private Timer timer;

    public BrickerGameManager(String windowTitle, Vector2 windowDimensions){
        super(windowTitle, windowDimensions);
    }
    /**
    The Bricker game manager is responsible for creating all the game objects and the management of the game
    */

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowController = windowController;
        this.imageReader = imageReader;

        this.random = new Random();
        this.timer = new Timer();

        //create ball
        Renderable ballImage = imageReader.readImage("assets/ball.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop_cut_silenced.wav");
        ball = new Ball(new Vector2(0,0), new Vector2(20,20), ballImage, collisionSound);
        setRandomBallVelocity();
        windowDimensions = windowController.getWindowDimensions();
        ballRecenter();
        this.gameObjects().addGameObject(ball, Layer.STATIC_OBJECTS);
        //Static Objects do not collide with each other but do collide with other layers
        //In order for the ball and the power ups to not collide, they both had to be static objects



        //create paddle
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
        Paddle paddle = new Paddle(new Vector2(Vector2.ZERO), new Vector2(100,15),paddleImage, inputListener, windowDimensions);
        paddle.setCenter(new Vector2(windowDimensions.x()/2, windowDimensions.y()-20));
        this.gameObjects().addGameObject(paddle);
        this.paddle = paddle;


        createWalls();
        createBricks(imageReader);

        //background
        Renderable backgroundColor = new RectangleRenderable(Color.lightGray);
        GameObject background = new GameObject(Vector2.ZERO, windowController.getWindowDimensions(), backgroundColor);
        gameObjects().addGameObject(background, Layer.BACKGROUND);

        //create lives
        livesCounter = new Counter(LIVES);
        hearts = new GameObject[LIVES];
        paintLives();

        //create powerUpsFactory
        powerUpFactory = new PowerUpFactory(ball, paddle);
        shceduleNextPowerUp(); //to randomly create new power up every few seconds
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkBallDirection(); //fix if ball moves too horizontally or too vertically
        checkGameEnd();
    }

    //region Ball Functions
    private void checkBallDirection(){
        //if the ball goes too vertically or horizontally change to a random direction
        if(Math.abs(ball.getVelocity().x())<20 || Math.abs(ball.getVelocity().y())<20){
            setRandomBallVelocity();
        }
    }

    private void ballRecenter(){
        float xLocation = random.nextInt((int) windowDimensions.x());
        Vector2 center = new Vector2(xLocation, windowDimensions.y()*0.5f);
        ball.setCenter(center);
        ball.setVelocity(new Vector2(BALL_SPEED,BALL_SPEED));
    }


    public void setRandomBallVelocity(){
        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random random = new Random();
        if(random.nextBoolean())
            ballVelX*=-1;
        if(random.nextBoolean())
            ballVelY*=-1;

        ball.setVelocity(new Vector2(ballVelX, ballVelY));
    }

    //endregion SomeName

    //region Screen functions
    public void createWalls(){
        this.gameObjects().addGameObject(new GameObject(Vector2.ZERO, new Vector2(BORDER_WIDTH, windowDimensions.y()), null));
        this.gameObjects().addGameObject(new GameObject(new Vector2(windowDimensions.x()-BORDER_WIDTH, 0), new Vector2(BORDER_WIDTH, windowDimensions.y()), null));
        this.gameObjects().addGameObject(new GameObject(Vector2.ZERO, new Vector2(windowDimensions.x(), BORDER_WIDTH), null));
    }
    public float calculateBrickLength(){
        return ((windowDimensions.x()-2*BORDER_WIDTH-BRICK_NUM)/BRICK_NUM);
    }
    public void createBricks( ImageReader imageReader){
        brickCounter = new Counter((int)BRICK_NUM*ROW_NUM);
        Renderable brickImage = imageReader.readImage("assets/brick.png", false);
        float brickLength = calculateBrickLength();

        for(int i=BORDER_WIDTH; i<BRICK_NUM*brickLength; i+=brickLength+1){
            for(int j=BORDER_WIDTH; j< ROW_NUM*BRICK_WIDTH; j+=BRICK_WIDTH+1){
                this.gameObjects().addGameObject(new Brick(new Vector2((float)i,(float)j), new Vector2(brickLength, BRICK_WIDTH), brickImage, new CollisionStrategy(gameObjects(), brickCounter)));
            }
        }
    }

    private void paintLives(){
        Renderable heartImage = imageReader.readImage("assets/heart.png", true);
        for(int i= 0;i<livesCounter.value();i++){
            hearts[i] = new GameObject(new Vector2(HEART_SIZE+i*(HEART_SIZE+5),windowDimensions.y()-HEART_SIZE*2), new Vector2(HEART_SIZE,HEART_SIZE), heartImage);
            this.gameObjects().addGameObject(hearts[i], Layer.BACKGROUND);
        }
    }
    //endregion

    //region Power ups functions
    public void shceduleNextPowerUp(){
        int delay = 1000 + random.nextInt(3000); // Generate a delay between 3s and 8s
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                createPowerUp();
                shceduleNextPowerUp();
            }
        }, delay);
    }
    public void createPowerUp(){
        PowerUp powerUp = powerUpFactory.build(imageReader, windowDimensions, gameObjects());
        this.gameObjects().addGameObject(powerUp, Layer.STATIC_OBJECTS);

    }
    //endregion

    private void checkGameEnd(){
        double ballHeight= ball.getCenter().y();
        String prompt = "";
        if(ballHeight>windowDimensions.y()) {
            livesCounter.decrement();
            gameObjects().removeGameObject(hearts[livesCounter.value()],Layer.BACKGROUND);
            if (livesCounter.value() == 0)
                prompt = "You lose!";
            else
                ballRecenter();
        }
        if(brickCounter.value()==0) {
            prompt = "You win!";
        }
        if(!prompt.isEmpty()){
            prompt+=" Play Again?";
            if(windowController.openYesNoDialog(prompt))
                windowController.resetGame();
            else
                windowController.closeWindow();

        }
    }



    public static void main(String[] args) {
        new BrickerGameManager("Bricker", new Vector2(700, 500)).run();
    }
}
