package fi.jaaket.pysakkivahti;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;

@Configuration
public class Config {
    @Bean
    public ScheduleRepository scheduleRepository() throws IOException {
        String GtfsZipPath = System.getenv("GTFS_ZIP_PATH");
        return new GtfsScheduleRepository(new File(GtfsZipPath));
    }
}
