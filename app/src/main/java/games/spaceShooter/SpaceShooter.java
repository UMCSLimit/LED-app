package games.spaceShooter;

import android.graphics.Color;

import games.spaceShooter.entities.SpaceShip;
import games.spaceShooter.services.SpaceController;
import umcs.robotics.umcsleds.configFiles.Variables;
import umcs.robotics.umcsleds.service.StageSender;

public class SpaceShooter {

    private SpaceController spaceController;
    private int LEVEL;

    public SpaceShooter() {
        spaceController = new SpaceController();
        spaceController.init();

        LEVEL = Variables.getInstance().gameLevel;


        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!spaceController.isGameOver) {
                        spaceController.update();

                        if(Variables.getInstance().isLiveMode){
                            StageSender.getInstance().sendActualStageToServer();
                        }

                        sleep(360 / (LEVEL * 2));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    public boolean isGameOver(){
        return spaceController.isGameOver;
    }

    public void setSpaceShipMove(SpaceController.Moves move) {
        spaceController.makeMove(move);
    }
}