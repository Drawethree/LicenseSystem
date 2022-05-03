package dev.drawethree.LicenseSystem.user.model;

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

    public static final long MAX_SOFTWARE_PER_CREATOR = 5;

    public static final long MAX_LICENSES_PER_SOFTWARE = 25;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "role")
    private String role;

    @OrderBy("id desc")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "creator", cascade = CascadeType.ALL)
    private List<Software> softwares;

    @OrderBy("id desc")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "licenseUser", cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.PERSIST})
    private List<License> licenses;

    @PreRemove
    private void preRemove() {
        for (License license : licenses) {
            license.setLicenseUser(null);
            license.setExpireDate(null);
            license.setActivationDate(null);
        }
    }

    public boolean isCustomer() {
        return "CUSTOMER".equalsIgnoreCase(role);
    }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }

    public boolean isCreator() {
        return "CREATOR".equalsIgnoreCase(role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id == user.id && username.equals(user.username) && password.equals(user.password) && email.equals(user.email) && createdAt.equals(user.createdAt) && role.equals(user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, email, createdAt, role);
    }
}
