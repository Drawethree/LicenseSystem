package dev.drawethree.LicenseSystem.software.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import dev.drawethree.LicenseSystem.license.model.LicenseDTO;
import dev.drawethree.LicenseSystem.user.model.UserDTO;
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
