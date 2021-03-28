package fi.jaaket.pysakkivahti;

import org.onebusaway.gtfs.impl.GtfsRelationalDaoImpl;
import org.onebusaway.gtfs.model.*;
import org.onebusaway.gtfs.serialization.GtfsReader;
import org.onebusaway.gtfs.services.GtfsMutableRelationalDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class GtfsScheduleRepository implements ScheduleRepository {
    private static ZoneId ZONE_ID = ZoneId.of("Europe/Helsinki");
    private static TimeZone TZ = TimeZone.getTimeZone(ZONE_ID);

    private Logger logger = LoggerFactory.getLogger(GtfsScheduleRepository.class);
    private GtfsMutableRelationalDao store = new GtfsRelationalDaoImpl();


    public GtfsScheduleRepository(File inputLocation) throws IOException {
        logger.info("Reading GTFS from {}", inputLocation.toString());
        GtfsReader reader = new GtfsReader();
        reader.setInputLocation(inputLocation);
        reader.setEntityStore(store);
        reader.run();
    }

    @Override
    public Schedule getForLineAndStopOnDate(String lineId, String stopId, LocalDate date) {
        Stop stop = store.getStopForId(new AgencyAndId("HSL", stopId));
        if (stop == null) {
            return null;
        }
        Route route = store.getRouteForId(new AgencyAndId("HSL", lineId));
        if (route == null) {
            return null;
        }

        List<ZonedDateTime> arrivalTimes = store.getStopTimesForStop(stop).stream().filter((stopTime) -> {
            Trip trip = stopTime.getTrip();
            ServiceCalendar serviceCalendar = store.getCalendarForServiceId(trip.getServiceId());
            return trip.getRoute().getId().getId().equals(lineId) && isServiceOperationalOnDate(date, serviceCalendar);
        }).sorted(Comparator.comparingInt(StopTime::getArrivalTime))
                .map((stopTime) -> parseArrivalTime(date, stopTime))
                .collect(Collectors.toList());

        return new Schedule(stop.getName(), route.getShortName(), arrivalTimes);
    }

    private static boolean isServiceOperationalOnDate(LocalDate date, ServiceCalendar calendar) {
        LocalDate startDateInclusive = LocalDate.ofInstant(calendar.getStartDate().getAsDate(TZ).toInstant(), ZONE_ID);
        LocalDate endDateInclusive = LocalDate.ofInstant(calendar.getEndDate().getAsDate(TZ).toInstant(), ZONE_ID);
        LocalDate endDateExclusive = endDateInclusive.minus(1, ChronoUnit.DAYS);

        return date.isAfter(startDateInclusive) && date.isBefore(endDateExclusive) &&
                doesWeekdayMatch(date, calendar);
    }

    private static boolean doesWeekdayMatch(LocalDate date, ServiceCalendar serviceCalendar) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return serviceCalendar.getMonday() == 1 && dayOfWeek == DayOfWeek.MONDAY ||
                serviceCalendar.getTuesday() == 1 && dayOfWeek == DayOfWeek.TUESDAY ||
                serviceCalendar.getWednesday() == 1 && dayOfWeek == DayOfWeek.WEDNESDAY ||
                serviceCalendar.getThursday() == 1 && dayOfWeek == DayOfWeek.THURSDAY ||
                serviceCalendar.getFriday() == 1 && dayOfWeek == DayOfWeek.FRIDAY ||
                serviceCalendar.getSaturday() == 1 && dayOfWeek == DayOfWeek.SATURDAY ||
                serviceCalendar.getSunday() == 1 && dayOfWeek == DayOfWeek.SUNDAY;
    }

    private static ZonedDateTime parseArrivalTime(LocalDate date, StopTime stopTime) {
        ZonedDateTime dateMidnight = ZonedDateTime.of(date, LocalTime.MIDNIGHT, ZONE_ID);
        return dateMidnight.plus(stopTime.getArrivalTime(), ChronoUnit.SECONDS);
    }
}
