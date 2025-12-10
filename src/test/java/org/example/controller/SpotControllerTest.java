package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.SpotRequest;
import org.example.dto.SpotResponse;
import org.example.service.SpotService;
import org.example.service.JwtService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SpotController.class)
@AutoConfigureMockMvc(addFilters = false)
class SpotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SpotService spotService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    @WithMockUser
    void getAllSpots_ShouldReturnListOfSpots() throws Exception {
        SpotResponse spot1 = SpotResponse.builder()
                .id(1L)
                .name("Skatepark Central")
                .description("Great skatepark")
                .location("Paris")
                .latitude(48.8566)
                .longitude(2.3522)
                .createdBy("user1")
                .build();

        SpotResponse spot2 = SpotResponse.builder()
                .id(2L)
                .name("Street Spot")
                .description("Nice street spot")
                .location("Lyon")
                .latitude(45.7640)
                .longitude(4.8357)
                .createdBy("user2")
                .build();

        List<SpotResponse> spots = Arrays.asList(spot1, spot2);

        when(spotService.getAllSpots()).thenReturn(spots);

        mockMvc.perform(get("/api/spots"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Skatepark Central"))
                .andExpect(jsonPath("$[1].name").value("Street Spot"));
    }

    @Test
    @WithMockUser
    void getSpotById_ShouldReturnSpot() throws Exception {
        SpotResponse spot = SpotResponse.builder()
                .id(1L)
                .name("Skatepark Central")
                .description("Great skatepark")
                .location("Paris")
                .latitude(48.8566)
                .longitude(2.3522)
                .createdBy("user1")
                .build();

        when(spotService.getSpotById(1L)).thenReturn(spot);

        mockMvc.perform(get("/api/spots/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Skatepark Central"))
                .andExpect(jsonPath("$.location").value("Paris"));
    }

    @Test
    @WithMockUser
    void createSpot_ShouldReturnCreatedSpot() throws Exception {
        SpotRequest request = SpotRequest.builder()
                .name("New Spot")
                .description("Awesome spot")
                .location("Marseille")
                .latitude(43.2965)
                .longitude(5.3698)
                .build();

        SpotResponse response = SpotResponse.builder()
                .id(1L)
                .name("New Spot")
                .description("Awesome spot")
                .location("Marseille")
                .latitude(43.2965)
                .longitude(5.3698)
                .createdBy("testuser")
                .build();

        when(spotService.createSpot(any(SpotRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/spots")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Spot"))
                .andExpect(jsonPath("$.location").value("Marseille"));
    }

    @Test
    @WithMockUser
    void updateSpot_ShouldReturnUpdatedSpot() throws Exception {
        SpotRequest request = SpotRequest.builder()
                .name("Updated Spot")
                .description("Updated description")
                .location("Nice")
                .latitude(43.7102)
                .longitude(7.2620)
                .build();

        SpotResponse response = SpotResponse.builder()
                .id(1L)
                .name("Updated Spot")
                .description("Updated description")
                .location("Nice")
                .latitude(43.7102)
                .longitude(7.2620)
                .createdBy("testuser")
                .build();

        when(spotService.updateSpot(eq(1L), any(SpotRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/spots/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Spot"))
                .andExpect(jsonPath("$.location").value("Nice"));
    }

    @Test
    @WithMockUser
    void deleteSpot_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/spots/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
