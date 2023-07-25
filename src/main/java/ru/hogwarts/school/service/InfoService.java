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
}
