package umcs.robotics.umcsleds.service;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import umcs.robotics.umcsleds.activities.MainActivity;
import umcs.robotics.umcsleds.configFiles.Variables;
import umcs.robotics.umcsleds.dataTemplate.StageToJson;


public class StageSender {

    private static StageSender stageSender;
    private String chanelValuesJson; //= "";
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
            serverAddr = InetAddress.getByName(Variables.getInstance().udpServerIP);
            int udp_port = Integer.parseInt(Variables.getInstance().udpPort);
            udpSocket = new DatagramSocket(udp_port);
        } catch (SocketException e) {
            Log.e("Udp:", "Socket Error:", e);
        } catch (IOException e) {
            Log.e("Udp Send:", "Socket IO Error:", e);
        }
    }

    public void sendActualStageToServer() {
        int windows_without_led = 11;
        for (int i=0; i < windows_without_led*3; i++){
            stageToJson.stage[i] = 0;
        }

        for (int i = 0; i <= Variables.numberOfWindows; i++) {
            int color = ((ColorDrawable) Variables.getInstance().awesomeViewsArr[i].getBackground()).getColor();
            Log.d("Window ID", "Window ID " + i);

            int channelID = (windows_without_led*3)+(i*3);
            stageToJson.key = Variables.udpKey;
            stageToJson.stage[channelID + 0] = Color.red(color);
            stageToJson.stage[channelID + 1] = Color.green(color);
            stageToJson.stage[channelID + 2] = Color.blue(color);
        }

        //Convert values to Json
        Gson gson = new Gson();
        chanelValuesJson = gson.toJson(stageToJson);
        Log.d("Json", "Json1 " + chanelValuesJson);
        sendStageUDP();
    }

    private void sendStageUDP(){
        try {
            byte[] buf = chanelValuesJson.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr,
                    Integer.parseInt(Variables.getInstance().udpPort));
            udpSocket.send(packet);
        } catch (SocketException e) {
            Log.e("Udp:", "Socket Error:", e);
        } catch (IOException e) {
            Log.e("Udp Send:", "Socket IO Error:", e);
        }

    }
}
