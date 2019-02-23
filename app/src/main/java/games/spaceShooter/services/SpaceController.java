package games.spaceShooter.services;


import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.util.Log;

import java.util.ArrayList;

import games.spaceShooter.entities.GameObject;
import games.spaceShooter.entities.LaserBeam;
import games.spaceShooter.entities.Meteoryt;
import games.spaceShooter.entities.SpaceShip;
import umcs.robotics.umcsleds.configFiles.Variables;
import umcs.robotics.umcsleds.service.StageSetter;

public class SpaceController {

    public enum Moves {
        UP, DOWN, SHOOT
    }

    private ArrayList<SpaceShip> spaceShips;
    private ArrayList<Meteoryt> meteoryts;
    private ArrayList<LaserBeam> laserBeams;
    private ArrayList<GameObject> objects;

    private int startX = 24;
    private int startY = 3;

    private short moveDir = 0;
    private boolean shootLaser = false;

    private double newMeteorPropability = 7;
    private int flagAmmo = 0;
    private int ammoPerXframes = 10;
    private int ammoOverall = 1;

    private float points = 0;
    private float pointsForMeteor = 2;
    private float pointsForColision = -10;
    private int laserDistance = 16;

    //MapSize
    private int mapX = 28;
    private int mapY = 5;
    private int noWindowsSpace = 12;

    //game states
    public boolean isGameOver = false;
    public boolean isGameStarted = false;


    private int LEVEL;

    public void init() {
        spaceShips = new ArrayList<>();
        meteoryts = new ArrayList<>();
        laserBeams = new ArrayList<>();
        objects = new ArrayList<>();

        LEVEL = Variables.getInstance().gameLevel;
        ammoPerXframes *= LEVEL / 1.5;
        newMeteorPropability /= LEVEL;

        initSpaceShip();
        initNewMeteor();
        initNewMeteor();
        render();
        isGameStarted = true;
    }

    public void update() {
        if (!isGameStarted) {
            renderStartGame();
            isGameStarted = true;
        } else {
            moveSpaceShip();
            moveMeteor();
            meteorGenerator();
            Variables.getInstance().scoreTextView.setText(points + " points");
            points += 0.22 * LEVEL;
            newMeteorPropability = ((10.0 + (double) ammoOverall) / (double) (ammoOverall) * 2.7); //more value = less meteors
            shootLaser();
            checkCollisions();
            checkGameOver();
            render();
            addingAmmo();
            moveLaserBeem();
        }
    }

    private void initNewMeteor() {
        int randomY = (int) (Math.random() * 4) + 1;

        Meteoryt meteoryt1 = new Meteoryt();
        meteoryt1.x = -1;
        meteoryt1.y = randomY;
        meteoryt1.y2 = randomY;
        meteoryts.add(meteoryt1);
        objects.add(meteoryt1);
    }

    private void initSpaceShip() {
        SpaceShip noseCone = new SpaceShip();
        noseCone.x = startX;
        noseCone.y = startY;
        spaceShips.add(noseCone);

        SpaceShip mid = new SpaceShip();
        mid.x = startX + 1;
        mid.y = startY;
        spaceShips.add(mid);

        SpaceShip engine = new SpaceShip();
        engine.x = startX + 2;
        engine.y = startY;
        engine.color = Color.RED;
        spaceShips.add(engine);

        objects.addAll(spaceShips);
    }


    private void moveMeteor() {
        for (int i = 0; i < meteoryts.size(); i++) {
            Meteoryt met = meteoryts.get(i);
            met.x += 1;
            met.x2 += 1;
            if(met.x > 28){
                meteoryts.remove(met);
                i--;
            }
        }
    }

    private void moveSpaceShip() {
        if((spaceShips.get(0).y + moveDir) < (mapY) && spaceShips.get(0).y + moveDir > 0){
            for (int i = 0; i < spaceShips.size(); i++) {
                SpaceShip ss = spaceShips.get(i);
                ss.y += moveDir;
            }
        }
        moveDir = 0;
    }

    private void moveLaserBeem() {
        for (int i = 0; i < laserBeams.size(); i++) {
            LaserBeam lb = laserBeams.get(i);
            lb.x -= 1;
            if(lb.x < mapX - laserDistance){
                laserBeams.remove(lb);
                objects.remove(lb);
            }
        }
    }

    private void meteorGenerator() {
        boolean isNew = ((Math.random() * newMeteorPropability) <= 1);
        if(isNew){
            initNewMeteor();
        }
    }

    private void addingAmmo() {
        flagAmmo++;
        if(flagAmmo >= ammoPerXframes){
            if((spaceShips.get(0).ammo < 5)){
                spaceShips.get(0).ammo++;
                ammoOverall++;
                System.gc();
            }
            flagAmmo = 0;
        }
    }

    private void shootLaser() {
        if(shootLaser && (spaceShips.get(0).ammo > 0)){
            spaceShips.get(0).ammo--;
            LaserBeam laser = new LaserBeam();
            laser.x = startX - 1;
            laser.y = spaceShips.get(0).y;
            laserBeams.add(laser);
            objects.add(laser);
            shootLaser = false;
        }
    }

    private void checkCollisions() {
        checkCollisionsMeteorLaser();
        checkCollisionsMeteorShip();
    }

    private void checkCollisionsMeteorShip() {
        for(int i = 0; i < spaceShips.size(); i++){
            SpaceShip ship = spaceShips.get(i);
            for(int j = 0; j < meteoryts.size(); j++){
                Meteoryt meteoryt = meteoryts.get(j);
                //Meteoryt meteoryt1 = meteoryts.get(j + 1);
                if((ship.x == meteoryt.x && ship.y == meteoryt.y)){
                        //|| (ship.x == meteoryt1.x && ship.y == meteoryt1.y)){
                    //
                    spaceShips.get(0).hp -= 2;
                    objects.removeAll(meteoryts);
                    meteoryts.removeAll(meteoryts);
                    points += pointsForColision * LEVEL;
                }
            }
        }
    }

    private void checkCollisionsMeteorLaser() {
        for(int i = 0; i <laserBeams.size(); i++){
            LaserBeam laser = laserBeams.get(i);
            for(int j = 0; j < meteoryts.size(); j++){
                Meteoryt meteoryt = meteoryts.get(j);
                if((laser.x == meteoryt.x &&
                        laser.y == meteoryt.y) || (laser.x == meteoryt.x2 &&
                        laser.y == meteoryt.y2)){
                    objects.remove(meteoryt);
                    meteoryts.remove(meteoryt);
                    objects.remove(laser);
                    laserBeams.remove(laser);
                    points += pointsForMeteor * LEVEL;
                    break;
                }
            }
        }
    }

    private void checkGameOver() {
        if(spaceShips.get(0).hp <= 0){
            isGameOver = true;
        }
    }

    private void render() {
        if (isGameOver) {
            Variables.getInstance().isGameRunning = false;
            renderGameOverScreen();
        } else {
            renderGameState();
        }
    }

    private void renderGameState() {
        for (int y = 0; y < mapY; y++) {
            for (int x = 0; x < mapX; x++) {
                boolean wasSthPrinted = false;
                int viewId = y * (mapX) + x;

                //Generate 1 row
                if (y == 0 && viewId >= 0) {
                    if(viewId < spaceShips.get(0).hp){
                        changeColorOfView(viewId, Color.GREEN);
                        wasSthPrinted = true;
                    } else if(viewId - 10 <= spaceShips.get(0).ammo && viewId >= 11){
                        changeColorOfView(viewId, Color.BLUE);
                        wasSthPrinted = true;
                    }

                } else {
                    viewId -= noWindowsSpace;
                    if (viewId <= 127) {
                        for (GameObject s : objects) {
                            if (s.x == x && s.y == y) {
                                if (s instanceof SpaceShip) {
                                    changeColorOfView(viewId, ((SpaceShip) s).color);
                                    //System.out.print("<");
                                    wasSthPrinted = true;
                                    break;
                                } else if (s instanceof Meteoryt) {
                                    changeColorOfView(viewId, ((Meteoryt) s).color);
                                    //System.out.print("O");
                                    wasSthPrinted = true;
                                    break;
                                } else if (s instanceof LaserBeam){
                                    changeColorOfView(viewId, ((LaserBeam) s).color);
                                    //System.out.print("-");
                                    wasSthPrinted = true;
                                    break;
                                }
                            }
                            if(s instanceof Meteoryt){
                                if((((Meteoryt) s).x2 == x) &&  (((Meteoryt) s).y == y)){
                                    changeColorOfView(viewId, ((Meteoryt) s).color);
                                    //System.out.print("O");
                                    wasSthPrinted = true;
                                    break;
                                }
                            }
                        }
                    }
                }

                if (!wasSthPrinted && viewId >= 0) {
                    changeColorOfView(viewId, Color.BLACK);
                    //System.out.print(".");
                }
            }
            System.out.println(Math.random());
        }
    }

    private void renderStartGame() {
        try {
            StageSetter.getInstance().THREE();
            Thread.sleep(1000);
            StageSetter.getInstance().TWO();
            Thread.sleep(1000);
            StageSetter.getInstance().ONE();
            Thread.sleep(1000);
            StageSetter.getInstance().GO();
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void renderGameOverScreen() {
        try {
            StageSetter.getInstance().BLACK();
            Thread.sleep(200);
            StageSetter.getInstance().RED();
            Thread.sleep(200);
            StageSetter.getInstance().BLACK();
            Thread.sleep(200);
            StageSetter.getInstance().RED();
            Thread.sleep(200);
            StageSetter.getInstance().BLACK();
            Thread.sleep(200);
            StageSetter.getInstance().RED();
            Thread.sleep(200);
            StageSetter.getInstance().setGAME();
            Thread.sleep(1000);
            StageSetter.getInstance().setOVER();
            Thread.sleep(1000);
            StageSetter.getInstance().sadFace();
            Thread.sleep(1000);
            StageSetter.getInstance().BLACK();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.print("GAME OVER XD");

    }

    private void changeColorOfView(int id, @ColorInt int color) {
        Variables.getInstance().changeColorOfView(id, color);
    }

    public void makeMove(Moves move) {
        switch (move){
            case SHOOT:
                shootLaser = true;
                break;
            case DOWN:
                moveDir = 1;
                break;
            case UP:
                moveDir = -1;
                break;
        }
    }
}
