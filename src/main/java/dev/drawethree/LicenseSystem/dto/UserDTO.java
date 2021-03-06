package dev.drawethree.LicenseSystem.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {

    private int id;

    private String username;

    private LocalDateTime createdAt;

    private String role;
}
