package fi.jaaket.pysakkivahti.ui;

import fi.jaaket.pysakkivahti.Schedule;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ScheduleViewModel {
    private static ZoneId ZONE_ID = ZoneId.of("Europe/Helsinki");
    private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    private String stopName;
    private String lineName;
    private List<ScheduleLine> scheduleLines;

    public ScheduleViewModel(Schedule schedule) {
        this.stopName = schedule.getStopName();
        this.lineName = schedule.getLineName();
        this.scheduleLines = makeScheduleLines(schedule);
    }

    private List<ScheduleLine> makeScheduleLines(Schedule schedule) {
        ZonedDateTime now = ZonedDateTime.now(ZONE_ID);
        return schedule.getArrivalTimes().stream().map((arrivalTime) ->
                new ScheduleLine(arrivalTime.format(timeFormatter), arrivalTime.isAfter(now))
        ).collect(Collectors.toList());
    }

    public String getStopName() {
        return stopName;
    }


    public String getLineName() {
        return lineName;
    }

    public List<ScheduleLine> getScheduleLines() {
        return scheduleLines;
    }
}
