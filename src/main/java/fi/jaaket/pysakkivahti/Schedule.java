package fi.jaaket.pysakkivahti;

import java.time.ZonedDateTime;
import java.util.List;

public class Schedule {
    private String stopName;
    private String lineName;
    private List<ZonedDateTime> arrivalTimes;

    public Schedule(String stopName, String lineName, List<ZonedDateTime> arrivalTimes) {
        this.stopName = stopName;
        this.lineName = lineName;
        this.arrivalTimes = arrivalTimes;
    }

    public String getStopName() {
        return stopName;
    }

    public String getLineName() {
        return lineName;
    }

    public List<ZonedDateTime> getArrivalTimes() {
        return arrivalTimes;
    }
}
