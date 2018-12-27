package umcs.robotics.umcsleds;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AnimationCreator {

    private static AnimationCreator animationCreator;
    public static synchronized AnimationCreator getInstance() {
        if (animationCreator == null)
            animationCreator = new AnimationCreator();
        return animationCreator;
    }
    private int nextFreeID;
    private ArrayList<Animation> animations;

    public Animation curretAnimation;

    public AnimationCreator(){
        animations = new ArrayList<>();
        readAnimationsFromDevice();
        nextFreeID = animations.size();
    }

    public void createNewAnimation(String name){
        curretAnimation = new Animation(name);
        animations.add(nextFreeID, curretAnimation);
        nextFreeID++;
    }

    public void addStageToAnimation(View leds[], int id){
        Stage tempStage = new Stage();
        for (int i = 0; i <= Variables.numberOfWindows; i++) {
            int color = ((ColorDrawable) leds[i].getBackground()).getColor();
            tempStage.setRgbValue(i, color);
        }
        curretAnimation.getStages().add(id, tempStage);
    }

    public void removeStageFromAnimation(int id){
        curretAnimation.getStages().remove(id);
    }

    public void saveAnimationsOnDevice() {
        String filename = "animations.txt";
        Gson gson = new Gson();
        String s = gson.toJson(animations);
        FileOutputStream outputStream;
        try {
            outputStream = MainActivity.getAppContext().openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(s.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readAnimationsFromDevice() {
        try {
            FileInputStream fis = MainActivity.getAppContext().openFileInput("animations.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) { sb.append(line); }
            String json = sb.toString();
            Gson gson = new Gson();
            animations = gson.fromJson(json, new TypeToken<ArrayList<Animation>>(){}.getType());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeAnimation(int id){
        animations.remove(id);
        nextFreeID--;
    }

    public ArrayList<Animation> getAnimations() {
        return animations;
    }

    public Animation getCurretAnimation() {
        return curretAnimation;
    }

    public void setCurretAnimation(Animation curretAnimation) {
        this.curretAnimation = curretAnimation;
    }
}
