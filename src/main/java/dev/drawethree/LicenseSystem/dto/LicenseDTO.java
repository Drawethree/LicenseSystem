package dev.drawethree.LicenseSystem.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import dev.drawethree.LicenseSystem.dto.SoftwareDTO;
import dev.drawethree.LicenseSystem.dto.UserDTO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LicenseDTO {

    private int id;

    @JsonBackReference
    private SoftwareDTO software;

    private int duration;

    @JsonBackReference
    private UserDTO licenseUser;

    private LocalDateTime createdAt;

    private LocalDateTime activationDate;
}
