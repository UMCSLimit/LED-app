package umcs.robotics.umcsleds;

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
