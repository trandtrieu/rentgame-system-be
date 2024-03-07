package com.service;

import com.dto.PlatformDTO;
import com.model.Platform;
import com.repository.PlatformRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlatformService {

    @Autowired
    private PlatformRepository platformRepository;

    public List<PlatformDTO> getAllPlatforms() {
        List<Platform> platforms = platformRepository.findAll();
        return platforms.stream().map(this::convertToDTO).collect(Collectors.toList());
    }


    private PlatformDTO convertToDTO(Platform platform) {
        PlatformDTO platformDTO = new PlatformDTO();
        platformDTO.setId(platform.getId());
        platformDTO.setName(platform.getName());
        return platformDTO;
    }


}
