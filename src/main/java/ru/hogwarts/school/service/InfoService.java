package ru.hogwarts.school.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class InfoService {
    private final ServerProperties serverProperties;

    public Integer getPort() {
        return serverProperties.getPort();
    }

    public Integer doSomething() {
        var start = System.currentTimeMillis();
        int result = 0;
        for (int i = 1; i < 1_000_000; i++) {
            result += i;
        }
        var end = System.currentTimeMillis();
        log.info("doSomething выполнил работу за {} мс", end - start);
        return result;
    }
}
