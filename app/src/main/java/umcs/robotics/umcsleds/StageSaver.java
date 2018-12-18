package umcs.robotics.umcsleds;

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
import com.google.gson.JsonArray;
import java.util.ArrayList;

public class StageSaver {

    private static StageSaver stageSaver;
    private ArrayList<Stage> stages = new ArrayList<Stage>();

    public static synchronized StageSaver getInstance() {
        if (stageSaver == null)
            stageSaver = new StageSaver();
        return stageSaver;
    }

    public StageSaver() {
        //readStagesFromDevice();
    }

    public void addStage(View leds[], Editable name) {
        //readStagesFromDevice();
        Stage tempStage = new Stage();
        tempStage.setName(name);
        for (int i = 0; i <= Variables.numberOfWindows; i++) {
            int color = ((ColorDrawable) leds[i].getBackground()).getColor();
            tempStage.setRgbValue(i, color);
        }
        stages.add(tempStage);
        tempStage.getChanelValues();
        saveStagesOnDevice();
        sendStageToServer(tempStage);
    }

    public void saveStagesOnDevice() {
//        //Stage mStudentObject = stages.get(0);
//        SharedPreferences appSharedPrefs = PreferenceManager
//                .getDefaultSharedPreferences(MainActivity.getAppContext());
//        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
//        Gson gson = new Gson();
//        Log.i("Zapis", "Zapis dziala2");
//        String json = gson.toJson(stages);
//        Log.i("Zapis", "Zapis dziala3");
//        prefsEditor.putString("MyObject", json);
//        prefsEditor.commit();

// Instantiate the RequestQueue.

    }

    private void readStagesFromDevice() {
//        SharedPreferences appSharedPrefs = PreferenceManager
//                .getDefaultSharedPreferences(MainActivity.getAppContext());
//        Gson gson = new Gson();
//        Type type = new TypeToken<ArrayList<Stage>>(){}.getType();
//        String json = appSharedPrefs.getString("MyObject", "");
//        stages = gson.fromJson(json, type);

//        String TAG = "MyTag";
//        StringRequest stringRequest; // Assume this exists.
//        RequestQueue mRequestQueue;  // Assume this exists.
//
//// Set the tag on the request.
//        stringRequest.setTag(TAG);
//
//// Add the request to the RequestQueue.
//        mRequestQueue.add(stringRequest);

        readValuesFromJson();
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
}

