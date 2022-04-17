package dev.drawethree.LicenseSystem.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.drawethree.LicenseSystem.license.model.License;
import dev.drawethree.LicenseSystem.software.model.Software;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String password;

    @JsonIgnore
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "roles")
    private String roles;

    @OrderBy("name desc")
    @OneToMany(mappedBy = "creator", cascade = {CascadeType.REMOVE, CascadeType.DETACH})
    private List<Software> softwares;

    @OrderBy("expireDate desc")
    @OneToMany(mappedBy = "licenseUser", cascade = {CascadeType.REMOVE, CascadeType.DETACH})
    @JsonIgnore
    private List<License> licenses;

    @JsonIgnore
    public boolean isCustomer() {
        return roles.contains("CUSTOMER");
    }

    @JsonIgnore
    public boolean isAdmin() {
        return roles.contains("ADMIN");
    }

    @JsonIgnore
    public boolean isCreator() {
        return roles.contains("CREATOR");
    }

}
