package dev.drawethree.licensesystem.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "license")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class License {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "software_id", nullable = false)
    private Software software;

    @Column(name = "license_key", nullable = false, unique = true)
    private String licenseKey;

    @Column(nullable = false)
    private String licenseUser;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "status_id")
    @Enumerated(EnumType.ORDINAL)
    private LicenseStatus status;


    public boolean isActive() {
        return LicenseStatus.ACTIVE == status;
    }

    public boolean isExpired() {
        return LicenseStatus.EXPIRED == status;
    }
}
