package umcs.robotics.umcsleds;

import android.support.annotation.ColorInt;
import android.view.View;

public class MyOnClickListiner implements View.OnClickListener {

    View view;
    @ColorInt int sliderColor = 0;
    Variables var = Variables.getInstance();

    public MyOnClickListiner(View view){
        this.view = view;
    }

    private void changeColor() {

        view.setBackgroundColor(sliderColor);

        if(Variables.getInstance().isLiveMode){
            sendActualStageToServer();
        }
        if(Variables.getInstance().isAnimationBarShowed){
            AnimationCreator.getInstance().replaceStageInAnimation(Variables.getInstance().awesomeViewsArr, Variables.getInstance().valueOfTimeLineBar);
        }

    }

    private void sendActualStageToServer() {
        StageSender.getInstance().sendActualStageToServer();
    }

    @Override
    public void onClick(View v) {
        this.sliderColor = var.sliderColor;
        changeColor();
    }
}
