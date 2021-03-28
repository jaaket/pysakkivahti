package fi.jaaket.pysakkivahti;

import fi.jaaket.pysakkivahti.ui.ScheduleLine;
import fi.jaaket.pysakkivahti.ui.ScheduleViewModel;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ScheduleViewModelTest {
    private static ZoneId ZONE_ID = ZoneId.of("Europe/Helsinki");

    @Test
    void testScheduleLinesArrivalTimeFormatted() {
        ScheduleViewModel scheduleViewModel =
                new ScheduleViewModel(time(9, 0), fakeSchedule());

        assertEquals("10:00", scheduleViewModel.getScheduleLines().get(0).getArrivalTime());
    }

    @Test
    void testScheduleLinesBeforeFirstArrival() {
        ScheduleViewModel scheduleViewModel =
                new ScheduleViewModel(time(9, 0), fakeSchedule());

        assertEquals(3, scheduleViewModel.getScheduleLines().size());
    }

    @Test
    void testScheduleLinesAfterFirstArrival() {
        ScheduleViewModel scheduleViewModel =
                new ScheduleViewModel(time(10, 1), fakeSchedule());

        List<ScheduleLine> lines = scheduleViewModel.getScheduleLines();
        assertEquals(3, lines.size());
        assertFalse(lines.get(0).isHighlighted());
        assertTrue(lines.get(1).isHighlighted());
    }


    @Test
    void testScheduleLinesAfterSecondArrival() {
        ScheduleViewModel scheduleViewModel =
                new ScheduleViewModel(time(10, 31), fakeSchedule());

        List<ScheduleLine> lines = scheduleViewModel.getScheduleLines();
        assertEquals(2, lines.size());
        assertFalse(lines.get(0).isHighlighted());
        assertTrue(lines.get(1).isHighlighted());
    }

    @Test
    void testScheduleLinesAfterLastArrival() {
        ScheduleViewModel scheduleViewModel =
                new ScheduleViewModel(time(11, 1), fakeSchedule());

        List<ScheduleLine> lines = scheduleViewModel.getScheduleLines();
        assertEquals(0, lines.size());
    }

    private Schedule fakeSchedule() {
        List<ZonedDateTime> arrivalTimes = List.of(
                time(10, 0),
                time(10, 30),
                time(11, 0)
        );
        return new Schedule("test stop", "test line", arrivalTimes);
    }

    private ZonedDateTime time(int hour, int minute) {
        return ZonedDateTime.of(LocalDate.of(2021, 3, 15), LocalTime.of(hour, minute), ZONE_ID);
    }
}
