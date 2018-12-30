package snake;

import snake.services.SnakeController;
import umcs.robotics.umcsleds.Variables;

public class Snake {

    private SnakeController snakeController;

    private int LEVEL = 3;

    public Snake() {

        snakeController = new SnakeController();
        snakeController.init();

        LEVEL = Variables.getInstance().gameLevel;

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!snakeController.isGameOver) {
                        snakeController.update();
                        sleep(300 / (LEVEL * 2));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    public void setSnakeDirection(SnakeController.Direction dir) {
        snakeController.setDirection(dir);
    }

}
