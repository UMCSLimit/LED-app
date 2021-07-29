package umcs.robotics.umcsleds.dataTemplate;

import umcs.robotics.umcsleds.configFiles.Variables;

public class StageToJson {
    public String name = "stage";
    public String key = Variables.udpKey;
    public int[] stage = new int[420];
}
