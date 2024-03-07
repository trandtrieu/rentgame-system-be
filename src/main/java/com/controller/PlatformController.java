package com.controller;

import com.dto.PlatformDTO;
import com.service.PlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/rent-game/platform")
public class PlatformController {

    @Autowired
    private PlatformService platformService;

    @GetMapping()
    public List<PlatformDTO> getALlPlatforms() {
        return platformService.getAllPlatforms();
    }
}
