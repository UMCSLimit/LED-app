package games.pong;

import games.pong.services.PongController;
import umcs.robotics.umcsleds.configFiles.Variables;
import umcs.robotics.umcsleds.service.StageSender;

public class Pong {
    private PongController pongController;

    Thread thread;

    Boolean isGameStoped;

    public Pong(){
        System.out.printf("XD");

        pongController = new PongController();
        pongController.init();

        Variables.getInstance().isGameStoped = false;

        thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!pongController.isGameOver && !Variables.getInstance().isGameStoped) {

                        pongController.update();
                        sleep(500);
                    }
                    pongController.isGameOver = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    public void stopGame(){
        isGameStoped = true;
    }


    public boolean isGameOver(){
        return pongController.isGameOver;
    }


}
