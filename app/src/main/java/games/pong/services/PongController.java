package games.pong.services;

import android.graphics.Color;
import android.support.annotation.ColorInt;

import java.util.ArrayList;

import games.pong.entities.Ball;
import games.pong.entities.GameObject;
import games.pong.entities.Player;
import games.pong.entities.Point;
import umcs.robotics.umcsleds.configFiles.Variables;
import umcs.robotics.umcsleds.service.StageSetter;

public class PongController {
    Player leftPlayer;
    Player rightPlayer;
    Ball ball;

    private int mapX = 28;
    private int mapY = 5;
    private int noWindowsSpace = 12;

    private int dirLP = 1;
    private int dirRP = -1;

    int vectorX = -1;
    int vectorY = 1;
    int leftPlayerPointsX = 3;
    int rightPlayerPointsX = 8;

    int leftPlayerPoints = 0;
    int rightPlayerPoints = 0;

    boolean didLeftPWin = false;
    boolean didRightPWin = false;
    public boolean isGameOver = false;

    private ArrayList<GameObject> gameObjects;

    public void init() {
        rightPlayerPoints = 0;
        leftPlayerPoints = 0;
        didLeftPWin = false;
        didRightPWin = false;
        gameObjects = new ArrayList<GameObject>();
        leftPlayer = new Player();
        gameObjects.add(leftPlayer);
        leftPlayer.x = 12;
        leftPlayer.y = 2;
        rightPlayer = new Player();
        gameObjects.add(rightPlayer);
        rightPlayer.x = 27;
        rightPlayer.y = 3;

        ball = new Ball();
        gameObjects.add(ball);
        ball.x = mapX - 8;
        ball.y = 2;
        Variables.getInstance().scoreTextView.setBackgroundColor(Color.BLACK);
}

    public void update() {
        if(didLeftPWin){
            System.out.println("GJ LEFT PLAYER");
            isGameOver = true;
            Variables.getInstance().isGameRunning = false;
            StageSetter.getInstance().BLACK();
        } else if(didRightPWin){
            System.out.println("GJ RIGHT PLAYER");
            isGameOver = true;
            Variables.getInstance().isGameRunning = false;
            StageSetter.getInstance().BLACK();
        } else{
            moveBall();
            movePlayers();
            checkForCollisionWithPlayers();
            checkPointsConditionals();
            renderGame();
        }

    }



    private void checkForCollisionWithPlayers(){

        if(ball.y == leftPlayer.y && ball.x - 1 == leftPlayer.x){
            vectorX *= -1;
        } else if(ball.y == rightPlayer.y && ball.x + 1 == rightPlayer.x){
            vectorX *= -1;
        }
    }

    private void checkPointsConditionals(){
        if ((ball.x == mapX)) {
            System.out.println("LOSEEEEE RIGHT");
            ball.x = mapX - 8;
            ball.y = randomY();
            vectorX *= -1;
            Point p = new Point();
            p.x = leftPlayerPointsX;
            leftPlayerPoints++;
            p.y = mapY - leftPlayerPoints;
            gameObjects.add(p);
        } else if((ball.x < noWindowsSpace)){
            System.out.println("LEFTTTT LOSEEEE");
            ball.x = mapX - 8;
            ball.y = randomY();
            vectorX *= -1;
            Point p = new Point();
            p.x = rightPlayerPointsX;
            rightPlayerPoints++;
            p.y = mapY - rightPlayerPoints;
            gameObjects.add(p);
        }

        if(rightPlayerPoints >= 4){
            didRightPWin = true;
        }
        if(leftPlayerPoints >= 4){
            didLeftPWin = true;
        }
    }

    private void movePlayers(){
        //move player if it possible
        if (!((leftPlayer.y > 3 && dirLP > 0) || (leftPlayer.y <= 0 && dirLP < 0))) {
            leftPlayer.y += dirLP;
        }

        if (!((rightPlayer.y > 3 && dirRP > 0) || (rightPlayer.y <= 0 && dirRP < 0))) {
            rightPlayer.y += dirRP;
        }
        if (leftPlayer.y == 4 || leftPlayer.y == 0) dirLP *= -1;
        if (rightPlayer.y == 4 || rightPlayer.y == 0) dirRP *= -1;

    }

    public void moveBall() {
        //up&down
        if ((ball.y > 3 && vectorY > 0) || (ball.y <= 0 && vectorY < 0)) {
            vectorY *= -1;
        }
            ball.x += vectorX;
            ball.y += vectorY;
    }

    private void renderGame() {
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
                    if (viewId <= 127) {
                        for (GameObject s : gameObjects) {
                            if (s.x == x && s.y == y) {
                                if (s instanceof Ball) {
                                    changeColorOfView(viewId, Color.WHITE);
                                    //changeColorOfView(viewId, ((Ball) s).color);
                                    System.out.print("O");
                                    wasSthPrinted = true;
                                    break;
                                } else if (s == leftPlayer) {
                                    changeColorOfView(viewId, Color.RED);
                                    //changeColorOfView(viewId, ((Player) s).color);
                                    System.out.print("P");
                                    wasSthPrinted = true;
                                    break;
                                } else if(s == rightPlayer){
                                    changeColorOfView(viewId, Color.BLUE);
                                    //changeColorOfView(viewId, ((Player) s).color);
                                    System.out.print("P");
                                    wasSthPrinted = true;
                                    break;
                                }
                                else if (s instanceof Point) {
                                    if(s.x == leftPlayerPointsX){
                                        changeColorOfView(viewId, Color.RED);
                                    }else if(s.x == rightPlayerPointsX){
                                        changeColorOfView(viewId, Color.BLUE);
                                    }
                                    //changeColorOfView(viewId, ((Point) s).color);
                                    System.out.print("H");
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
            System.out.println("\n");
        }
    }

    private int randomX() {
        return (int) (Math.random() * 28);
    }

    private int randomY() {
        return (int) (Math.random() * 5);
    }
    private void changeColorOfView(int id, @ColorInt int color) {
        Variables.getInstance().changeColorOfView(id, color);
    }
}


