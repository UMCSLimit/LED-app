package umcs.robotics.umcsleds.service;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import umcs.robotics.umcsleds.activities.MainActivity;
import umcs.robotics.umcsleds.configFiles.PrivateConfig;
import umcs.robotics.umcsleds.configFiles.Variables;
import umcs.robotics.umcsleds.dataTemplate.StageToJson;


public class StageSender {

    private static StageSender stageSender;
    private String chanelValuesJson;
    int chanelValues[] = new int[385];
    StageToJson stageToJson = new StageToJson();

    RequestQueue queue = Volley.newRequestQueue(MainActivity.getAppContext());

    public static synchronized StageSender getInstance() {
        if (stageSender == null)
            stageSender = new StageSender();
        return stageSender;
    }

    public StageSender() {

    }

    public void sendActualStageToServer() {
        //Convert values from views to colors to PX140

        for (int i = 0; i <= Variables.numberOfWindows; i++) {
            int color = ((ColorDrawable) Variables.getInstance().awesomeViewsArr[i].getBackground()).getColor();

            int channelID = 381 - i * 3;
            stageToJson.stage[channelID + 1] = Color.red(color);
            stageToJson.stage[channelID + 2] = Color.green(color);
            stageToJson.stage[channelID + 3] = Color.blue(color);
        }

        //Convert values to Json
        Gson gson = new Gson();
        chanelValuesJson = gson.toJson(stageToJson);
        Log.d("Json", "Json1 " + chanelValuesJson);

        //Sending to server
        sendStageToServer();
    }

    private void sendStageToServer() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PrivateConfig.getInstance().urlUpdateStage,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("wysylanie", "wysylanie nie dziala");
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                Log.d("Json ", "Json " + chanelValuesJson);
                return chanelValuesJson.getBytes();
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        System.gc();
    }

    private void readValuesFromJson() {
        RequestQueue queue = Volley.newRequestQueue(MainActivity.getAppContext());


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, PrivateConfig.getInstance().urlGetStage,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.i("Odbior", "Odbior " + response.substring(0, 1167));

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Odbior", "Odbior nie dziala");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

}
