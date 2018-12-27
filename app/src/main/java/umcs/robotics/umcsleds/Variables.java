package umcs.robotics.umcsleds;

import android.support.annotation.ColorInt;
import android.view.View;

public class Variables {

    private static Variables variables;

    public static synchronized Variables getInstance(){
        if(variables == null){
            variables = new Variables();
        }
        return variables;
    }

    @ColorInt int sliderColor;

    public static int numberOfWindows = 127;
    public boolean isLiveMode = false;
    public View awesomeViewsArr[] = new View[140];
    public boolean wasSettingOpen = false;
    public boolean isAnimationBarShowed = false;
    public int valueOfTimeLineBar = 0;
}
