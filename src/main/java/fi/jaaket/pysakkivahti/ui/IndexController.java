package fi.jaaket.pysakkivahti.ui;

import fi.jaaket.pysakkivahti.Schedule;
import fi.jaaket.pysakkivahti.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class IndexController {
    @Autowired
    private ScheduleRepository scheduleRepository;
    private static ZoneId ZONE_ID = ZoneId.of("Europe/Helsinki");
    private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    @GetMapping("/")
    public String index(Model model) {
        LocalDate now = LocalDate.now();
        Schedule schedule = scheduleRepository.getForLineAndStopOnDate("3001R", "5010554", now);
        model.addAttribute("stopName", schedule.getStopName());
        model.addAttribute("lineName", schedule.getLineName());
        model.addAttribute("scheduleLines", makeScheduleLines(schedule));
        return "index";
    }

    private List<ScheduleLine> makeScheduleLines(Schedule schedule) {
        ZonedDateTime now = ZonedDateTime.now(ZONE_ID);
        return schedule.getArrivalTimes().stream().map((arrivalTime) ->
            new ScheduleLine(arrivalTime.format(timeFormatter), arrivalTime.isAfter(now))
        ).collect(Collectors.toList());
    }
}
