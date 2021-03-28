package fi.jaaket.pysakkivahti;

import java.time.LocalDate;

public interface ScheduleRepository {
    Schedule getForLineAndStopOnDate(String lineId, String stopId, LocalDate date);
}
