package bricker.main;

import bricker.brick_strategies.BrickStrategyFactory;
import bricker.brick_strategies.CollisionStrategy;
import bricker.brick_strategies.RemoveBrickStrategy;
import bricker.gameobjects.Ball;
import bricker.gameobjects.Brick;
import bricker.gameobjects.Paddle;
import bricker.power_ups.PowerUp;
import bricker.power_ups.PowerUpFactory;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import danogl.util.Counter;

import javax.crypto.spec.PSource;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;

public class BrickerGameManager extends GameManager {
    private static final int BORDER_WIDTH= 15;
    private static final float BALL_SPEED = 300;
    private static final float BRICK_NUM = 8;
    private static final int ROW_NUM = 5;
    private static final float BRICK_WIDTH = 30;
    private static final int LIVES=3;
    private static final int HEART_SIZE=30;

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
    private SoundReader soundReader;

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
        this.soundReader = soundReader;

        this.random = new Random();
        this.timer = new Timer();
        windowDimensions = windowController.getWindowDimensions();

        //create ball
        createBall();


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
        powerUpFactory = new PowerUpFactory(imageReader, windowDimensions, gameObjects(), windowController, paddle);
        shceduleNextPowerUp(); //to randomly create new power up every few seconds
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        for(GameObject object : gameObjects()){
            if(object.getTag().equals("Ball")){
                checkBallDirection((Ball)object);//fix if ball moves too horizontally or too vertically
                double ballHeight = object.getCenter().y();
                if(ballHeight>windowDimensions.y()) {
                    gameObjects().removeGameObject(object, Layer.STATIC_OBJECTS);
                }
            }
        }

        checkGameEnd();
    }

    //region Ball Functions
    private void createBall(){
        Random random = new Random();
        float xLocation = random.nextInt((int) windowDimensions.x());
        Vector2 center = new Vector2(xLocation, windowDimensions.y()*0.5f);
        //ball.setVelocity(new Vector2(BALL_SPEED,BALL_SPEED));
        windowDimensions = windowController.getWindowDimensions();
        Renderable ballImage = imageReader.readImage("assets/ball.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop_cut_silenced.wav");
        createBall(ballImage, collisionSound, windowDimensions, gameObjects(), center);
    }

    public static void createBall(Renderable ballImage, Sound collisionSound, Vector2 windowDimensions, GameObjectCollection gameObjects, Vector2 location){
        Ball ball = new Ball(location, new Vector2(20,20), ballImage, collisionSound);
        setRandomBallVelocity(ball);
        gameObjects.addGameObject(ball, Layer.STATIC_OBJECTS);

        //Static Objects do not collide with each other but do collide with other layers
        //In order for the ball and the power ups to not collide, they both had to be static objects
    }


    private void checkBallDirection(Ball ball){
        //if the ball goes too vertically or horizontally change to a random direction
        if(Math.abs(ball.getVelocity().x())<20 || Math.abs(ball.getVelocity().y())<20){
            setRandomBallVelocity(ball);
        }
    }


    public static void setRandomBallVelocity(Ball ball){
        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random random = new Random();
        if(random.nextBoolean())
            ballVelX*=-1;
        if(random.nextBoolean())
            ballVelY*=-1;

        ball.setVelocity(new Vector2(ballVelX, ballVelY));
    }

    //endregion

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
        //create a strategy: what happens when the ball hits the brick
        BrickStrategyFactory brickStrategyFactory = new BrickStrategyFactory(gameObjects(),brickCounter, imageReader, soundReader, windowDimensions);
        float brickLength = calculateBrickLength();
        for(int i=BORDER_WIDTH; i<BRICK_NUM*brickLength; i+=brickLength+1){
            for(int j=BORDER_WIDTH; j< ROW_NUM*BRICK_WIDTH; j+=BRICK_WIDTH+1){
                CollisionStrategy strategy = brickStrategyFactory.getStrategy();
                Renderable brickImage = imageReader.readImage(strategy.getImagePath(), false);
                this.gameObjects().addGameObject(new Brick(new Vector2((float)i,(float)j), new Vector2(brickLength, BRICK_WIDTH), brickImage, strategy));
            }
        }
    }

    private void paintLives(){ //paint the heart shapes of the lives
        Renderable heartImage = imageReader.readImage("assets/heart.png", true);
        for(int i= 0;i<livesCounter.value();i++){
            hearts[i] = new GameObject(new Vector2(HEART_SIZE+i*(HEART_SIZE+5),windowDimensions.y()-HEART_SIZE*2), new Vector2(HEART_SIZE,HEART_SIZE), heartImage);
            this.gameObjects().addGameObject(hearts[i], Layer.BACKGROUND);
        }
    }
    //endregion

    //region Power ups functions
    public void shceduleNextPowerUp(){
        int delay = 1000 + random.nextInt(1000); // Generate a delay between 3s and 8s TODO change back to 3000
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                createPowerUp();
                shceduleNextPowerUp();
            }
        }, delay);
    }
    public void createPowerUp(){
        PowerUp powerUp = powerUpFactory.build();
        this.gameObjects().addGameObject(powerUp, Layer.STATIC_OBJECTS);

    }
    //endregion

    private void checkGameEnd(){
        String prompt = "";
        boolean areThereBalls = false;
        for(GameObject object : gameObjects()){
            if(object.getTag().equals("Ball")){
                areThereBalls = true;
            }
        }
        if(!areThereBalls) {
            livesCounter.decrement();
            gameObjects().removeGameObject(hearts[livesCounter.value()],Layer.BACKGROUND);
            if (livesCounter.value() == 0)
                prompt = "You lose!";
            else
                createBall();
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
