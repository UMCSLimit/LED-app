package games.pong;

import games.pong.services.PongController;
import umcs.robotics.umcsleds.configFiles.Variables;
import umcs.robotics.umcsleds.service.StageSender;

public class Pong {
    private PongController pongController;

    public Pong(){
        System.out.printf("XD");

        pongController = new PongController();
        pongController.init();

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!pongController.isGameOver) {

                        pongController.update();
                        sleep(500);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    public boolean isGameOver(){
        return pongController.isGameOver;
    }


}
