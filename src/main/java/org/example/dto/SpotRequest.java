package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpotRequest {
    private String name;
    private String description;
    private String location;
    private Double latitude;
    private Double longitude;
}

