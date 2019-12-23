package games.snake;

import games.snake.services.SnakeController;
import umcs.robotics.umcsleds.service.StageSender;
import umcs.robotics.umcsleds.configFiles.Variables;

public class Snake  {

    private SnakeController snakeController;

    private int LEVEL;

    Thread thread;

    public Snake() {
        startGame();
    }

    public void startGame(){
        LEVEL = Variables.getInstance().gameLevel;

        snakeController = new SnakeController();
        snakeController.init();

        Variables.getInstance().isGameStoped = false;

        thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!snakeController.isGameOver && !Variables.getInstance().isGameStoped) {
                        snakeController.update();

                        if(Variables.getInstance().isLiveMode){
                            StageSender.getInstance().sendActualStageToServer();
                        }

                        sleep(400 / (LEVEL * 2));
                    }
                    snakeController.isGameOver = true;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    public boolean isGameOver(){
        return snakeController.isGameOver;
    }


    public void setSnakeDirection(SnakeController.Direction dir) {
        snakeController.setDirection(dir);
    }

}
