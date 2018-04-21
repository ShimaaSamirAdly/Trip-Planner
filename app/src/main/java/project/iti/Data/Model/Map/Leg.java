package project.iti.Data.Model.Map;

import java.util.List;

/**
 * Created by asmaa on 03/12/2018.
 */

public class Leg {
    private Distance distance;
    private Duration duration;
    private List<Step> steps = null;

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }
}
