package com.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Service
public class SteamService {

    private static final Logger logger = LoggerFactory.getLogger(SteamService.class);

    @Value("${autohotkey.path}")
    private String autoHotkeyPath;

    @Value("${steam.script.path}")
    private String scriptPath;

    public void launchSteam(String username, String password) {
        try {
            logger.info("Launching Steam...");

            ProcessBuilder processBuilder = new ProcessBuilder(autoHotkeyPath, scriptPath, username, password);
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new RuntimeException("Failed to execute AutoHotkey script");
            }

            logger.info("Logged in to Steam successfully!");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to execute commands: " + e.getMessage(), e);
        }
    }
}
