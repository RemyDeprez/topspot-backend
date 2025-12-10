package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.SpotRequest;
import org.example.dto.SpotResponse;
import org.example.model.Spot;
import org.example.repository.SpotRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpotService {

    private final SpotRepository spotRepository;

    public SpotResponse createSpot(SpotRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Spot spot = Spot.builder()
                .name(request.getName())
                .description(request.getDescription())
                .location(request.getLocation())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .createdBy(username)
                .build();

        spot = spotRepository.save(spot);
        return mapToResponse(spot);
    }

    public List<SpotResponse> getAllSpots() {
        return spotRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public SpotResponse getSpotById(Long id) {
        Spot spot = spotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Spot not found with id: " + id));
        return mapToResponse(spot);
    }

    public SpotResponse updateSpot(Long id, SpotRequest request) {
        Spot spot = spotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Spot not found with id: " + id));

        spot.setName(request.getName());
        spot.setDescription(request.getDescription());
        spot.setLocation(request.getLocation());
        spot.setLatitude(request.getLatitude());
        spot.setLongitude(request.getLongitude());

        spot = spotRepository.save(spot);
        return mapToResponse(spot);
    }

    public void deleteSpot(Long id) {
        if (!spotRepository.existsById(id)) {
            throw new RuntimeException("Spot not found with id: " + id);
        }
        spotRepository.deleteById(id);
    }

    private SpotResponse mapToResponse(Spot spot) {
        return SpotResponse.builder()
                .id(spot.getId())
                .name(spot.getName())
                .description(spot.getDescription())
                .location(spot.getLocation())
                .latitude(spot.getLatitude())
                .longitude(spot.getLongitude())
                .createdBy(spot.getCreatedBy())
                .build();
    }
}

