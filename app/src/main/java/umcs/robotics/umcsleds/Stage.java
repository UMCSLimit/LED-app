package umcs.robotics.umcsleds;

import android.graphics.Color;
import android.text.Editable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Stage implements Serializable {
    public String name;
    public int rgbValue[] = new int[130];
    public void setName(String name) {
        this.name = name;
    }
    public void setRgbValue(int i, int rgbValue) {
        this.rgbValue[i] = rgbValue;
    }
}
