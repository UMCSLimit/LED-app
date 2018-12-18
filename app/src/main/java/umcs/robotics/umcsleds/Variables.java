package umcs.robotics.umcsleds;

import android.support.annotation.ColorInt;

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

}
