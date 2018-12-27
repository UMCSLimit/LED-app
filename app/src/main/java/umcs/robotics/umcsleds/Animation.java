package umcs.robotics.umcsleds;

import java.io.Serializable;
import java.util.ArrayList;

public class Animation implements Serializable {

    private String name;
    private ArrayList<Stage> stages = new ArrayList<>();
    private int times[] = new int[512];

    public Animation(String name){
        this.name = name;
    }



    // GETTERS AND SETTERS
    public ArrayList<Stage> getStages() {
        return stages;
    }

    public void setStages(ArrayList<Stage> stages) {
        this.stages = stages;
    }

    public int[] getTimes() {
        return times;
    }

    public void setTimes(int[] times) {
        this.times = times;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
