package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.SpotRequest;
import org.example.dto.SpotResponse;
import org.example.service.SpotService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/spots")
@RequiredArgsConstructor
public class SpotController {

    private final SpotService spotService;

    @GetMapping
    public ResponseEntity<List<SpotResponse>> getAllSpots() {
        return ResponseEntity.ok(spotService.getAllSpots());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpotResponse> getSpotById(@PathVariable Long id) {
        return ResponseEntity.ok(spotService.getSpotById(id));
    }

    @PostMapping
    public ResponseEntity<SpotResponse> createSpot(@RequestBody SpotRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(spotService.createSpot(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpotResponse> updateSpot(
            @PathVariable Long id,
            @RequestBody SpotRequest request
    ) {
        return ResponseEntity.ok(spotService.updateSpot(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpot(@PathVariable Long id) {
        spotService.deleteSpot(id);
        return ResponseEntity.noContent().build();
    }
}

