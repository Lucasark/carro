package com.intro.sandbox.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class SchedulerService {

    @Scheduled(cron = "0 */1 * * * ?") // Runs every hour at minute 0
    public void performScheduledTask() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForObject("https://carro-w0n1.onrender.com", String.class);
        log.info("Scheduled task executed successfully at {}", System.currentTimeMillis());
    }
}
