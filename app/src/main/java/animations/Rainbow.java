package animations;

import android.graphics.Color;
import android.support.annotation.ColorInt;

import java.util.Timer;

import games.pong.entities.Ball;
import games.pong.entities.GameObject;
import games.pong.entities.Point;
import umcs.robotics.umcsleds.configFiles.Variables;

public class Rainbow {
     Thread thread;

    private int mapX = 28;
    private int mapY = 5;
    private int noWindowsSpace = 12;

    public Boolean stopAnimation;


        public Rainbow(){


            Variables.getInstance().isGameStoped = false;

            thread = new Thread() {
                @Override
                public void run() {
                    try {
                        Variables.getInstance().isRainbow = true;
                        while (!Variables.getInstance().isGameStoped) {

                            test();

                            sleep(20);

                        }
                        Variables.getInstance().isGameStoped = true;
                        Variables.getInstance().isRainbow = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();
        }

        public void stopAnimation(){
            Variables.getInstance().isGameStoped = true;
        }


    private void render(float startHue8, int yHueDelta8, int xHueDelta8) {

        float lineStartHue = startHue8;

        for (int y = 0; y < mapY; y++) {

            lineStartHue += yHueDelta8;

            float pixelHue = lineStartHue;

            for (int x = 0; x < mapX; x++) {

                pixelHue += xHueDelta8;
                pixelHue += 360/mapX;
                pixelHue %= 360;

                int viewId = y * (mapX) + x;

                if (y == 0 && x < noWindowsSpace) {
                    System.out.print("X");
                } else {
                    viewId -= noWindowsSpace;

                    if(Variables.getInstance().isTouched && Variables.getInstance().touchedX == x) {

                        if (viewId <= 127 && viewId >= 0) {
                            float arr[] = {pixelHue, 1f, 1f};
                            int tempColor = Color.HSVToColor(arr);

                            changeColorOfView(viewId, tempColor);
                            //leds[ XY(x, y)]  = CHSV( pixelHue, 255, 255);
                        }
                    } else {
                        changeColorOfView(viewId, Color.BLACK);
                    }
                }
            }
        }
        System.out.println("\n");
    }


    private void test(){
        long ms =  (System.currentTimeMillis()) / 5;
        // ms %= 1000;

        int yHueDelta32 = ((int) Math.cos( ms * (0.0010) ) * (350 / mapX));
        int xHueDelta32 = ((int) Math.cos( ms * (0.001) ) * (310 / mapY));

        render( ms % 360, yHueDelta32 , xHueDelta32);
    }


    private void changeColorOfView(int id, @ColorInt int color) {
        Variables.getInstance().changeColorOfView(id, color);
    }
}




/*

void DrawOneFrame( byte startHue8, int8_t yHueDelta8, int8_t xHueDelta8)
{
  byte lineStartHue = startHue8;
  for( byte y = 0; y < kMatrixHeight; y++) {
    lineStartHue += yHueDelta8;
    byte pixelHue = lineStartHue;
    for( byte x = 0; x < kMatrixWidth; x++) {
      pixelHue += xHueDelta8;
      leds[ XY(x, y)]  = CHSV( pixelHue, 255, 255);
    }
  }
}

void test(){
    uint32_t ms = millis();
    int32_t yHueDelta32 = ((int32_t)cos16( ms * (27/1) ) * (350 / kMatrixWidth));
    int32_t xHueDelta32 = ((int32_t)cos16( ms * (39/1) ) * (310 / kMatrixHeight));
    DrawOneFrame( ms / 65536, yHueDelta32 / 32768, xHueDelta32 / 32768);
    if( ms < 5000 ) {
      FastLED.setBrightness( scale8( BRIGHTNESS, (ms * 256) / 5000));
    } else {
      FastLED.setBrightness(BRIGHTNESS);
    }
    FastLED.show();
}


 */