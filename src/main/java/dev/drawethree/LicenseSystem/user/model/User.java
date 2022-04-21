package dev.drawethree.LicenseSystem.user.model;

import dev.drawethree.LicenseSystem.license.model.License;
import dev.drawethree.LicenseSystem.software.model.Software;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public static long MAX_LICENSES_PER_SOFTWARE = 25;

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

    @Column(name = "roles")
    private String roles;

    @OrderBy("name desc")
    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    private List<Software> softwares;

    @OrderBy("expireDate desc")
    @OneToMany(mappedBy = "licenseUser", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    private List<License> licenses;

    public boolean isCustomer() {
        return roles.contains("CUSTOMER");
    }

    public boolean isAdmin() {
        return roles.contains("ADMIN");
    }

    public boolean isCreator() {
        return roles.contains("CREATOR");
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

    public void addLicense(License license) {
        if (licenses == null) {
            licenses = new ArrayList<>();
        }
        licenses.add(license);
        license.setLicenseUser(this);
    }

    public void removeLicense(License license) {
        if (licenses == null) {
            licenses = new ArrayList<>();
        }
        licenses.remove(license);
        license.setLicenseUser(null);
    }

    public void addSoftware(Software software) {

        if (softwares == null) {
            softwares = new ArrayList<>();
        }

        softwares.add(software);
        software.setCreator(this);
    }

    public void removeSoftware(Software software) {
        if (softwares == null) {
            softwares = new ArrayList<>();
        }
        softwares.remove(software);
        software.setCreator(null);
    }
}
