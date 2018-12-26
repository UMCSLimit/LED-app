package umcs.robotics.umcsleds;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
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

    private void readValuesFromJson() {
        RequestQueue queue = Volley.newRequestQueue(MainActivity.getAppContext());
        String url ="http://212.182.27.226:5000/getstage";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.i("Wysylanie", "Wysylanie " + response.substring(0,1167));

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Wysylanie", "Wysylanie nie dziala");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void sendStageToServer(Stage stage) {
        JsonArray postData = new JsonArray();
    }

    public ArrayList<Stage> getStages() {
        return stages;
    }
    public void removeStage(int id){
        stages.remove(id);
        saveStagesOnDevice();
    }

}

