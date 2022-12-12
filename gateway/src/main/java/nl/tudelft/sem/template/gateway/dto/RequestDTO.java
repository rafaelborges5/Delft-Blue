package nl.tudelft.sem.template.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class RequestDTO {
    private String netId;
    private String name;
    private String description;
    private LocalDate preferredDate;
    private int cpu;
    private int gpu;
    private int memory;
}
