package umcs.robotics.umcsleds.dataTemplate;

import java.io.Serializable;

import umcs.robotics.umcsleds.configFiles.Variables;

public class Stage implements Serializable {
    private String name;
    private int rgbValue[] = new int[Variables.numberOfWindows + 1];

    //GETTERS and SETTERS
    public void setName(String name) {
        this.name = name;
    }

    public void setRgbValue(int i, int rgbValue) {
        this.rgbValue[i] = rgbValue;
    }

    public String getName() {
        return name;
    }

    public int[] getRgbValue() {
        return rgbValue;
    }
}
