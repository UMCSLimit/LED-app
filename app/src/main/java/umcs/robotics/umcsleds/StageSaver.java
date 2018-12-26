package umcs.robotics.umcsleds;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
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

public class StageSaver {

    private static StageSaver stageSaver;
    private ArrayList<Stage> stages;

    public static synchronized StageSaver getInstance() {
        if (stageSaver == null)
            stageSaver = new StageSaver();
        return stageSaver;
    }

    public StageSaver() {
        stages = new ArrayList<Stage>();
        readStagesFromDevice();
    }

    public void addStage(View leds[], Editable name) {
        Stage tempStage = new Stage();
        tempStage.setName(name.toString());
        for (int i = 0; i <= Variables.numberOfWindows; i++) {
            int color = ((ColorDrawable) leds[i].getBackground()).getColor();
            tempStage.setRgbValue(i, color);
        }
        stages.add(tempStage);
        saveStagesOnDevice();
        //sendStageToServer(tempStage);
    }

    public void saveStagesOnDevice() {
        String filename = "stages.txt";
        Gson gson = new Gson();
        String s = gson.toJson(stages);
        FileOutputStream outputStream;
        try {
            outputStream = MainActivity.getAppContext().openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(s.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readStagesFromDevice() {
        try {
            FileInputStream fis = MainActivity.getAppContext().openFileInput("stages.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) { sb.append(line); }
            String json = sb.toString();
            Gson gson = new Gson();
            stages = gson.fromJson(json, new TypeToken<ArrayList<Stage>>(){}.getType());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //readValuesFromJson();
    }




    public ArrayList<Stage> getStages() {
        return stages;
    }

    public void removeStage(int id){
        stages.remove(id);
        saveStagesOnDevice();
    }

}

