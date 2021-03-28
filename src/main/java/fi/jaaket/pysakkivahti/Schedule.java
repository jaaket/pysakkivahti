package fi.jaaket.pysakkivahti;

import java.time.ZonedDateTime;
import java.util.Collection;

public class Schedule {
    private String stopName;
    private String lineName;
    private Collection<ZonedDateTime> arrivalTimes;

    public Schedule(String stopName, String lineName, Collection<ZonedDateTime> arrivalTimes) {
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

    public Collection<ZonedDateTime> getArrivalTimes() {
        return arrivalTimes;
    }
}
