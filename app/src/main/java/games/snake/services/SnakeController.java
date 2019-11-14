package games.snake.services;

import android.graphics.Color;
import android.support.annotation.ColorInt;

import java.util.ArrayList;

import games.snake.entities.Food;
import games.snake.entities.GameObject;
import games.snake.entities.SnakeSegment;
import games.spaceShooter.entities.Meteoryt;
import umcs.robotics.umcsleds.service.StageSetter;
import umcs.robotics.umcsleds.configFiles.Variables;

public class SnakeController {

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private Direction direction = Direction.RIGHT;

    private ArrayList<SnakeSegment> snakeBody;
    private ArrayList<Food> foods;
    private ArrayList<GameObject> objects;

    //Direction
    private int xDir = 0;
    private int yDir = 1;

    //Start
    private int xStart = 2;
    private int yStart = 6;

    //Previous
    private int prevDirX = 0;
    private int prevDirY = 0;

    //game states
    public boolean isGameOver = false;
    public boolean isGameStarted = false;

    //MapSize
    private int mapX = Variables.width;
    private int mapY = Variables.height;
    private int noWindowsSpace = 0;

    public void init() {
        snakeBody = new ArrayList<SnakeSegment>();
        foods = new ArrayList<Food>();
        objects = new ArrayList<GameObject>();

        initSnakeBody();
        initFood();

    }

    public void update() {
        if (!isGameStarted) {
            renderStartGame();
            isGameStarted = true;
        } else {
            checkInputAndMoveSnake();
            checkCollisionWithFood();
            checkGameOverConditionals();
            render();
        }
    }

    private void initSnakeBody() {
        SnakeSegment head = new SnakeSegment();
        head.x = xStart;
        head.y = yStart;

        snakeBody.add(head);

        SnakeSegment ss0 = new SnakeSegment();
        ss0.x = xStart + 1;
        ss0.y = yStart;
        snakeBody.add(ss0);

        objects.addAll(snakeBody);
    }

    private void initFood() {
        Food f = new Food();
        objects.add(f);
        foods.add(f);
        f.x = randomX();
        f.y = (int) (Math.random() * 3 + 1);
    }

    private void checkInput() {
        switch (direction) {
            case UP:
                xDir = 0;
                if (yDir == 0)
                    yDir = 1;
                break;

            case DOWN:
                xDir = 0;
                if (yDir == 0)
                    yDir = -1;
                break;

            case LEFT:
                if (xDir == 0)
                    xDir = -1;
                yDir = 0;
                break;

            case RIGHT:
                if (xDir == 0)
                    xDir = 1;
                yDir = 0;
                break;
        }
    }

    private int deleyY = 0;

    private void checkInputAndMoveSnake() {
        //x times slower for Y
        if (yDir != 0) {
            if (deleyY >= 1) {
                checkInput();
                moveSnakeBody();
                deleyY = 0;
            } else {
                deleyY++;
            }
        } else {
            checkInput();
            moveSnakeBody();
        }
    }

    private void moveSnakeBody() {
        for (int i = 0; i < snakeBody.size(); i++) {
            SnakeSegment ss = snakeBody.get(i);

            if (i == 0) {
                //head
                prevDirX = ss.x;
                prevDirY = ss.y;

                ss.x += xDir;
                ss.y += yDir;
            } else {
                int a = prevDirX;
                int b = prevDirY;

                prevDirX = ss.x;
                prevDirY = ss.y;

                ss.x = a;
                ss.y = b;
            }
        }
    }

    private int randomX() {
        return (int) (Math.random() * Variables.width);
    }

    private int randomY() {
        return (int) (Math.random() * Variables.height);
    }

    private void checkGameOverConditionals() {
        checkCollisionWithOwnBody();
        overTheMap();
    }

    private void checkCollisionWithOwnBody() {
        for (SnakeSegment snakeSegment : snakeBody) {
            if (snakeSegment != snakeBody.get(0)) {
                if (snakeBody.get(0).x == snakeSegment.x && snakeBody.get(0).y == snakeSegment.y) {
                    isGameOver = true;
                }
            }
        }
    }

    private void overTheMap() {
//        if ((snakeBody.get(0).y == 0) && (snakeBody.get(0).x < noWindowsSpace)) {
//            isGameOver = true;
//        } else
            if (snakeBody.get(0).y > (mapY - 1) || snakeBody.get(0).y < 0) {
            isGameOver = true;
        } else if (snakeBody.get(0).x > (mapX - 1) || snakeBody.get(0).x < 0) {
            isGameOver = true;
        }
    }

    private void checkCollisionWithFood() {
        //kolizja z food
        for (int i = 0; i < snakeBody.size(); i++) {
            SnakeSegment ss = snakeBody.get(i);
            for (int x = 0; x < foods.size(); x++) {
                Food f = foods.get(x);
                if (f.x == ss.x && f.y == ss.y) {
                    foods.remove(f);
                    objects.remove(f);
                    //nowy segment ogon
                    SnakeSegment tail = snakeBody.get(snakeBody.size() - 1);

                    SnakeSegment ss1 = new SnakeSegment();

                    ss1.x = tail.x;
                    ss1.y = tail.y;

                    snakeBody.add(ss1);
                    objects.add(ss1);

                    newFood();
                    Variables.getInstance().scoreTextView.setText(Integer.toString(snakeBody.size()));

                    break;
                }
            }
        }
    }

    private void newFood() {
        //food w losowym miejscu
        Food f3 = new Food();

        f3.x = randomX();
        f3.y = randomY();

        //Random food where snake is not
        int i = 0;
        for (; i < snakeBody.size(); i++) {
            if (snakeBody.get(i).x == f3.x && snakeBody.get(i).y == f3.y) {
                f3.x = randomX();
                f3.y = randomY();
                //break and check position of food for new coordinates
                i = 0;
            } else if (f3.x < noWindowsSpace && f3.y == 0){
                f3.x = randomX();
                f3.y = randomY();
                i = 0;
            }
        }

        objects.add(f3);
        foods.add(f3);
    }

    private void render() {
        if (isGameOver) {
            Variables.getInstance().isGameRunning = false;
            renderGameOverScreen();
        } else {
            renderGameState();
        }
    }

    public void renderStartGame() {

//        try {
////            StageSetter.getInstance().THREE();
////            Thread.sleep(1000);
////            StageSetter.getInstance().TWO();
////            Thread.sleep(1000);
////            StageSetter.getInstance().ONE();
////            Thread.sleep(1000);
////            StageSetter.getInstance().GO();
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }

    public void renderGameOverScreen() {

//        try {
//            StageSetter.getInstance().BLACK();
//            Thread.sleep(200);
//            StageSetter.getInstance().RED();
//            Thread.sleep(200);
//            StageSetter.getInstance().BLACK();
//            Thread.sleep(200);
//            StageSetter.getInstance().RED();
//            Thread.sleep(200);
//            StageSetter.getInstance().BLACK();
//            Thread.sleep(200);
//            StageSetter.getInstance().RED();
//            Thread.sleep(200);
//            StageSetter.getInstance().setGAME();
//            Thread.sleep(1000);
//            StageSetter.getInstance().setOVER();
//            Thread.sleep(1000);
//            StageSetter.getInstance().sadFace();
//            Thread.sleep(1000);
//            StageSetter.getInstance().BLACK();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        System.out.print("GAME OVER XD");

    }

    private void renderGameState() {

        for (int y = 0; y < mapY; y++) {
            for (int x = 0; x < mapX; x++) {
                boolean wasSthPrinted = false;
                int viewId = y * (mapX) + x;

                //Generate 1 row
                if (y == 0 && x < noWindowsSpace) {
                    System.out.print("X");
                    wasSthPrinted = true;
                } else {
                    viewId -= noWindowsSpace;

                    if (viewId <= Variables.numberOfWindows) {
                        for (GameObject s : objects) {
                            if (s.x == x && s.y == y) {
                                if (s instanceof SnakeSegment) {
                                    changeColorOfView(viewId, ((SnakeSegment) s).color);
                                    System.out.print("*");
                                    wasSthPrinted = true;
                                    break;
                                } else if (s instanceof Food) {
                                    changeColorOfView(viewId, ((Food) s).color);
                                    System.out.print("F");
                                    wasSthPrinted = true;
                                    break;
                                }
                            }
                        }
                    }
                }

                if (!wasSthPrinted) {
                    changeColorOfView(viewId, Color.BLACK);
                    System.out.print(".");
                }
            }
            System.out.println(randomX());
        }
    }

    private void changeColorOfView(int id, @ColorInt int color) {
        Variables.getInstance().changeColorOfView(id, color);
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
