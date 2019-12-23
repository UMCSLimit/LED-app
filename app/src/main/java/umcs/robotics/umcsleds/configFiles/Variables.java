package umcs.robotics.umcsleds.configFiles;

import android.support.annotation.ColorInt;
import android.view.View;
import android.widget.TextView;

import umcs.robotics.umcsleds.service.AnimationCreator;
import umcs.robotics.umcsleds.service.StageSender;

public class Variables {

    private static Variables variables;

    public static synchronized Variables getInstance(){
        if(variables == null){
            variables = new Variables();
        }
        return variables;
    }

    public @ColorInt int sliderColor;
    public static int numberOfWindows = 127;
    public boolean isLiveMode = false;
    public View awesomeViewsArr[] = new View[128];
    public boolean wasSettingOpen = false;
    public boolean isAnimationBarShowed = false;
    public int valueOfTimeLineBar = 0;
    public boolean isGameRunning = false;
    public int gameLevel = 3;
    public TextView scoreTextView;
    public boolean isGameStoped = false;

    public int touchedX = 0;
    public int touchedY = 0;
    public boolean isTouched = false;

    public boolean isRainbow = false;

    public void changeColorOfView(int id, int color){
        awesomeViewsArr[id].setBackgroundColor(color);
    }

    public void changeColorOfView(int id){
        awesomeViewsArr[id].setBackgroundColor(sliderColor);

        if(isLiveMode){
            StageSender.getInstance().sendActualStageToServer();
        }

        if(isAnimationBarShowed){
            AnimationCreator.getInstance().replaceStageInAnimation(Variables.getInstance().awesomeViewsArr, Variables.getInstance().valueOfTimeLineBar);
        }
    }
}
