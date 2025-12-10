package org.example.service;

import org.example.dto.SpotRequest;
import org.example.dto.SpotResponse;
import org.example.model.Spot;
import org.example.repository.SpotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpotServiceTest {

    @Mock
    @SuppressWarnings("unused")
    private SpotRepository spotRepository;

    @Mock
    @SuppressWarnings("unused")
    private SecurityContext securityContext;

    @Mock
    @SuppressWarnings("unused")
    private Authentication authentication;

    @InjectMocks
    private SpotService spotService;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
        org.mockito.Mockito.lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        org.mockito.Mockito.lenient().when(authentication.getName()).thenReturn("testuser");
    }

    @Test
    void createSpot_ShouldReturnCreatedSpot() {
        SpotRequest request = SpotRequest.builder()
                .name("Test Spot")
                .description("Test Description")
                .location("Test Location")
                .latitude(48.8566)
                .longitude(2.3522)
                .build();

        Spot savedSpot = Spot.builder()
                .id(1L)
                .name("Test Spot")
                .description("Test Description")
                .location("Test Location")
                .latitude(48.8566)
                .longitude(2.3522)
                .createdBy("testuser")
                .build();

        when(spotRepository.save(any(Spot.class))).thenReturn(savedSpot);

        SpotResponse response = spotService.createSpot(request);

        assertNotNull(response);
        assertEquals("Test Spot", response.getName());
        assertEquals("testuser", response.getCreatedBy());
        verify(spotRepository, times(1)).save(any(Spot.class));
    }

    @Test
    void getAllSpots_ShouldReturnListOfSpots() {
        Spot spot1 = Spot.builder()
                .id(1L)
                .name("Spot 1")
                .description("Description 1")
                .location("Location 1")
                .createdBy("user1")
                .build();

        Spot spot2 = Spot.builder()
                .id(2L)
                .name("Spot 2")
                .description("Description 2")
                .location("Location 2")
                .createdBy("user2")
                .build();

        when(spotRepository.findAll()).thenReturn(Arrays.asList(spot1, spot2));

        List<SpotResponse> spots = spotService.getAllSpots();

        assertEquals(2, spots.size());
        assertEquals("Spot 1", spots.get(0).getName());
        assertEquals("Spot 2", spots.get(1).getName());
    }

    @Test
    void getSpotById_ShouldReturnSpot() {
        Spot spot = Spot.builder()
                .id(1L)
                .name("Test Spot")
                .description("Test Description")
                .location("Test Location")
                .createdBy("testuser")
                .build();

        when(spotRepository.findById(1L)).thenReturn(Optional.of(spot));

        SpotResponse response = spotService.getSpotById(1L);

        assertNotNull(response);
        assertEquals("Test Spot", response.getName());
        assertEquals(1L, response.getId());
    }

    @Test
    void getSpotById_WhenNotFound_ShouldThrowException() {
        when(spotRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> spotService.getSpotById(1L));
    }

    @Test
    void updateSpot_ShouldReturnUpdatedSpot() {
        Spot existingSpot = Spot.builder()
                .id(1L)
                .name("Old Name")
                .description("Old Description")
                .location("Old Location")
                .createdBy("testuser")
                .build();

        SpotRequest request = SpotRequest.builder()
                .name("New Name")
                .description("New Description")
                .location("New Location")
                .latitude(48.8566)
                .longitude(2.3522)
                .build();

        Spot updatedSpot = Spot.builder()
                .id(1L)
                .name("New Name")
                .description("New Description")
                .location("New Location")
                .latitude(48.8566)
                .longitude(2.3522)
                .createdBy("testuser")
                .build();

        when(spotRepository.findById(1L)).thenReturn(Optional.of(existingSpot));
        when(spotRepository.save(any(Spot.class))).thenReturn(updatedSpot);

        SpotResponse response = spotService.updateSpot(1L, request);

        assertNotNull(response);
        assertEquals("New Name", response.getName());
        assertEquals("New Description", response.getDescription());
    }

    @Test
    void deleteSpot_ShouldDeleteSpot() {
        when(spotRepository.existsById(1L)).thenReturn(true);

        spotService.deleteSpot(1L);

        verify(spotRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteSpot_WhenNotFound_ShouldThrowException() {
        when(spotRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> spotService.deleteSpot(1L));
    }
}
