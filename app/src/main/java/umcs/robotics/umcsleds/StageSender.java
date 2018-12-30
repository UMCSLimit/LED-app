package umcs.robotics.umcsleds;

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


public class StageSender {

    private static StageSender stageSender;
    private String chanelValuesJson;
    int chanelValues[] = new int[385];
    StageToJson stageToJson = new StageToJson();

    public static synchronized StageSender getInstance() {
        if (stageSender == null)
            stageSender = new StageSender();
        return stageSender;
    }

    public StageSender() {

    }

    public void sendActualStageToServer() {
        //Convert values from views to colors to PX140
        //int chanelValues[] = new int[385];

        for (int i = 0; i <= Variables.numberOfWindows; i++) {
            int color = ((ColorDrawable) Variables.getInstance().awesomeViewsArr[i].getBackground()).getColor();

            int channelID = 381 - i * 3;
            chanelValues[channelID + 1] = Color.red(color);
            chanelValues[channelID + 2] = Color.green(color);
            chanelValues[channelID + 3] = Color.blue(color);
            stageToJson.stage[channelID + 1] = Color.red(color);
            stageToJson.stage[channelID + 2] = Color.green(color);
            stageToJson.stage[channelID + 3] = Color.blue(color);


            Log.i("RGB", "RGB(B) id \t" + (channelID + 3) + "\t value \t" + chanelValues[channelID + 3]);
            Log.i("RGB", "RGB(G) id \t" + (channelID + 2) + "\t value \t" + chanelValues[channelID + 2]);
            Log.i("RGB", "RGB(R) id \t" + (channelID + 1) + "\t value \t" + chanelValues[channelID + 1]);
        }

        //Convert values to Json
        Gson gson = new Gson();
        chanelValuesJson = gson.toJson(stageToJson);
        Log.d("Json", "Json1 " + chanelValuesJson);

        //Sending to server
        sendStageToServer(chanelValuesJson);
    }

    private void sendStageToServer(String str) {
        RequestQueue queue = Volley.newRequestQueue(MainActivity.getAppContext());
        String url = "http://192.168.1.3:5000/updatestage";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.i("wysylanie", "wysylanie");

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
    }

    private void readValuesFromJson() {
        RequestQueue queue = Volley.newRequestQueue(MainActivity.getAppContext());
        String url = "http://192.168.1.3:5000/getstage";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
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
