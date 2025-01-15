package com.mismola.footballeventsstatistics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

//temporary datasouce exclusion
@SpringBootApplication //(exclude = {DataSourceAutoConfiguration.class })
public class FootballEventsStatisticsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FootballEventsStatisticsApplication.class, args);
    }

}
