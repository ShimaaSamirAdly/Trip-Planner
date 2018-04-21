package project.iti.Data.Model.Map;

import java.util.List;

/**
 * Created by asmaa on 03/12/2018.
 */

public class Route {
    private List<Leg> legs = null;

    public List<Leg> getLegs() {
        return legs;
    }

    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }
}
