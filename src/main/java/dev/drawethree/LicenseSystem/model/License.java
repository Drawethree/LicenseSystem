package dev.drawethree.LicenseSystem.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

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

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "software_id", nullable = false)
    private Software software;

    @Column(name = "license_key", nullable = false, unique = true)
    private String licenseKey;

    @Column(name = "duration", nullable = false)
    private int duration;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "customer_id", nullable = false)
    private User licenseUser;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime expiresAt;

    @Column(name = "status_id")
    @Enumerated(EnumType.ORDINAL)
    private LicenseStatus status;

    public boolean isActive() {
        return LocalDateTime.now().isBefore(this.expiresAt);
    }

    public boolean isExpired() {
        return !isActive();
    }
}
