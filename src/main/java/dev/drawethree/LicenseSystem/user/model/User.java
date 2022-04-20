package dev.drawethree.LicenseSystem.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.drawethree.LicenseSystem.license.model.License;
import dev.drawethree.LicenseSystem.software.model.Software;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

    public static long MAX_SOFTWARE_PER_CREATOR = 5;

    public static long MAX_LICENSES_PER_SOFTWARE = 50;

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


    public boolean canCreateSoftware() {
        if (isAdmin()) {
            return true;
        }
        return isCreator() && this.getSoftwares().size() < MAX_SOFTWARE_PER_CREATOR;
    }

    public boolean canCreateLicense(Software software) {
        if (isAdmin()) {
            return true;
        }

        return isCreator() && software.getCreator().equals(this) && software.getLicenses().size() < MAX_LICENSES_PER_SOFTWARE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id == user.id && username.equals(user.username) && password.equals(user.password) && email.equals(user.email) && createdAt.equals(user.createdAt) && roles.equals(user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, email, createdAt, roles);
    }
}
