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

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import umcs.robotics.umcsleds.activities.MainActivity;
import umcs.robotics.umcsleds.configFiles.PrivateConfig;
import umcs.robotics.umcsleds.configFiles.Variables;
import umcs.robotics.umcsleds.dataTemplate.StageToJson;


public class StageSender {

    private static StageSender stageSender;
    private String chanelValuesJson; //= "";
    int chanelValues[] = new int[385];
    StageToJson stageToJson = new StageToJson();

    RequestQueue queue = Volley.newRequestQueue(MainActivity.getAppContext());

    DatagramSocket udpSocket;
    InetAddress serverAddr;
    public static synchronized StageSender getInstance() {
        if (stageSender == null)
            stageSender = new StageSender();
        return stageSender;
    }

    public StageSender() {
        try{
            serverAddr = InetAddress.getByName(PrivateConfig.getInstance().udpServerIP);
            udpSocket = new DatagramSocket(PrivateConfig.getInstance().udpPort);
        } catch (SocketException e) {
            Log.e("Udp:", "Socket Error:", e);
        } catch (IOException e) {
            Log.e("Udp Send:", "Socket IO Error:", e);
        }
    }

    public void sendActualStageToServer() {
        //Convert values from views to colors to PX140

        for (int i = 0; i <= Variables.numberOfWindows; i++) {
            int color = ((ColorDrawable) Variables.getInstance().awesomeViewsArr[i].getBackground()).getColor();

            Log.d("Window ID", "Window ID " + i);

            int channelID = 381 - i * 3;
            stageToJson.stage[channelID + 0] = Color.red(color);
            stageToJson.stage[channelID + 1] = Color.green(color);
            stageToJson.stage[channelID + 2] = Color.blue(color);
        }

        //Convert values to Json
        Gson gson = new Gson();
        chanelValuesJson = gson.toJson(stageToJson);
        Log.d("Json", "Json1 " + chanelValuesJson);

        //Sending to server
        //sendStageToServer();
        sendStageUDP();
    }

//    private void sendStageToServer() {
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, PrivateConfig.getInstance().urlUpdateStage,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.i("wysylanie", "wysylanie nie dziala");
//            }
//        }) {
//            @Override
//            public byte[] getBody() throws AuthFailureError {
//                Log.d("Json ", "Json " + chanelValuesJson);
//                return chanelValuesJson.getBytes();
//            }
//            @Override
//            public String getBodyContentType() {
//                return "application/json";
//            }
//        };
//
//        // Add the request to the RequestQueue.
//        queue.add(stringRequest);
//        System.gc();
//    }

    private void sendStageUDP(){
        try {
            byte[] buf = chanelValuesJson.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length,serverAddr, PrivateConfig.getInstance().udpPort);
            udpSocket.send(packet);
            //udpSocket.receive();
        } catch (SocketException e) {
            Log.e("Udp:", "Socket Error:", e);
        } catch (IOException e) {
            Log.e("Udp Send:", "Socket IO Error:", e);
        }

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
