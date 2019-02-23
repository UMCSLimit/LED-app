package games.snake;

import games.snake.services.SnakeController;
import umcs.robotics.umcsleds.service.StageSender;
import umcs.robotics.umcsleds.configFiles.Variables;

public class Snake  {

    private SnakeController snakeController;

    private int LEVEL;

    public Snake() {
        startGame();
    }

    public void startGame(){
        LEVEL = Variables.getInstance().gameLevel;

        snakeController = new SnakeController();
        snakeController.init();

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!snakeController.isGameOver) {
                        snakeController.update();

                        if(Variables.getInstance().isLiveMode){
                            StageSender.getInstance().sendActualStageToServer();
                        }

                        sleep(400 / (LEVEL * 2));
                    }
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
