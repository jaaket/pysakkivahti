package fi.jaaket.pysakkivahti.ui;

import fi.jaaket.pysakkivahti.Schedule;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ScheduleViewModel {
    private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    private String stopName;
    private String lineName;
    private List<ScheduleLine> scheduleLines;

    public ScheduleViewModel(ZonedDateTime now, Schedule schedule) {
        this.stopName = schedule.getStopName();
        this.lineName = schedule.getLineName();
        this.scheduleLines = makeScheduleLines(now, schedule.getArrivalTimes());
    }

    private List<ScheduleLine> makeScheduleLines(ZonedDateTime now, List<ZonedDateTime> arrivalTimes) {
        return findUpcomingAndLastPastArrivalTimes(now, arrivalTimes).stream()
                .map((arrivalTime) -> makeScheduleLine(now, arrivalTime))
                .collect(Collectors.toList());
    }

    private List<ZonedDateTime> findUpcomingAndLastPastArrivalTimes(
            ZonedDateTime now,
            Collection<ZonedDateTime> arrivalTimes
    ) {
        List<ZonedDateTime> result = new ArrayList<>();
        boolean previousWasInThePast = true;
        ZonedDateTime previousArrivalTime = null;
        ZonedDateTime lastArrivalTimeBeforeUpcoming = null;

        for (ZonedDateTime arrivalTime : arrivalTimes) {
            if (isArrivalTimeUpcoming(now, arrivalTime)) {
                result.add(arrivalTime);
                if (previousWasInThePast) {
                    lastArrivalTimeBeforeUpcoming = previousArrivalTime;
                }
                previousWasInThePast = false;
            }
            previousArrivalTime = arrivalTime;
        }
        if (lastArrivalTimeBeforeUpcoming != null) {
            result.add(0, lastArrivalTimeBeforeUpcoming);
        }

        return result;
    }

    private static boolean isArrivalTimeUpcoming(ZonedDateTime now, ZonedDateTime arrivalTime) {
        return arrivalTime.isAfter(now);
    }

    private static ScheduleLine makeScheduleLine(ZonedDateTime now, ZonedDateTime arrivalTime) {
        return new ScheduleLine(arrivalTime.format(timeFormatter), isArrivalTimeUpcoming(now, arrivalTime));
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
