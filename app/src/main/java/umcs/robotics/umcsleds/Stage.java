package umcs.robotics.umcsleds;

import android.graphics.Color;
import android.text.Editable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Stage implements Serializable {

    public Editable name;
    public int rgbValue[] = new int[130];

    public void setName(Editable name) {
        this.name = name;
    }
    public void setRgbValue(int i, int rgbValue) {
        this.rgbValue[i] = rgbValue;
    }

    private int chanelValues[] = new int[385];

    public JSONArray getChanelValues() {

        JSONArray obj = new JSONArray();

        for(int i = 0; i <= Variables.numberOfWindows; i++){
            //127*3 = 381
            int red = Color.red(rgbValue[i]);
            int green = Color.green(rgbValue[i]);
            int blue = Color.blue(rgbValue[i]);

            int channelID = 381 - i * 3;
            chanelValues[channelID + 1] = red;
            chanelValues[channelID + 2] = green;
            chanelValues[channelID + 3] = blue;

            obj.put(red);
            obj.put(green);
            obj.put(blue);

            Log.i("RGB", "RGB " + (channelID + 1) + " " + red);
            Log.i("RGB", "RGB " + (channelID + 2) + " " + green);
            Log.i("RGB", "RGB " + (channelID + 3) + " " + blue);
        }

        return obj;
    }
}
