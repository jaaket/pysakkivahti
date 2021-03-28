package fi.jaaket.pysakkivahti.ui;

import fi.jaaket.pysakkivahti.Schedule;
import fi.jaaket.pysakkivahti.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class IndexController {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @GetMapping("/")
    public String index(
            @RequestParam(value = "stopId", required = false, defaultValue = "5010554") String stopId,
            @RequestParam(value = "routeId", required = false, defaultValue = "3001R") String routeId,
            Model model
    ) {
        LocalDate now = LocalDate.now();
        Schedule schedule = scheduleRepository.getForLineAndStopOnDate(routeId, stopId, now);
        ScheduleViewModel scheduleViewModel =
                schedule != null ? new ScheduleViewModel(schedule) : null;
        model.addAttribute("schedule", scheduleViewModel);
        return "index";
    }
}
