package dev.drawethree.LicenseSystem.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SoftwareDTO {

    private int id;

    private String name;

    private String description;

    private LocalDateTime createdAt;

    @JsonManagedReference
    private List<LicenseDTO> licenses;

    @JsonBackReference
    private UserDTO creator;

}
