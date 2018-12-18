package umcs.robotics.umcsleds;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MyOnClickListiner implements View.OnClickListener {

    View view;
    @ColorInt int sliderColor = 0;
    Variables var = Variables.getInstance();

    public MyOnClickListiner(View view){
        this.view = view;
    }

//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        this.sliderColor = var.sliderColor;
//        changeColor();
//        return false;
//    }


    private void changeColor() {
        //Log.i("Kolor", "Kolor " + sliderColor);
        view.setBackgroundColor(sliderColor);
    }

    @Override
    public void onClick(View v) {
        this.sliderColor = var.sliderColor;
        changeColor();
    }
}
