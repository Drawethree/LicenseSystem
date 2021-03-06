package dev.drawethree.LicenseSystem.license.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import dev.drawethree.LicenseSystem.software.model.Software;
import dev.drawethree.LicenseSystem.user.model.User;
import dev.drawethree.LicenseSystem.utils.DateUtils;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "license")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class License {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "software_id")
    @JsonBackReference
    private Software software;

    @Column(name = "license_key", nullable = false, unique = true)
    private String licenseKey;

    @Column(name = "duration", nullable = false)
    private int duration;

    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "license_user_id")
    @JsonBackReference
    private User licenseUser;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expire_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime expireDate;

    @Column(name = "activation_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime activationDate;

    public boolean isActive() {

        if (isPermanent()) {
            return licenseUser != null;
        }

        return licenseUser != null && expireDate != null && LocalDateTime.now().isBefore(this.expireDate);
    }

    public boolean isExpired() {

        if (isPermanent()) {
            return false;
        }

        return expireDate != null && LocalDateTime.now().isAfter(this.expireDate);
    }

    public boolean isWaitingForActivation() {
        return licenseUser == null && activationDate == null;
    }

    public boolean isPermanent() {
        return duration == 0;
    }

    public String getExpiresInAsString() {

        if (this.duration == 0) {
            return "Never";
        }

        if (isExpired()) {
            return "Expired";
        }

        LocalDateTime current = LocalDateTime.now();
        long millisDiff = ChronoUnit.MILLIS.between(current, expireDate);
        return DateUtils.formatMillisDDHHMMSS(millisDiff);
    }

    public String getLicenseStatus() {
        if (isActive()) {
            return "Active";
        } else if (isExpired()) {
            return "Expired";
        } else if (isWaitingForActivation()) {
            return "Waiting For Activation";
        }
        return "Unknown";
    }

    public void activate(User user) {
        this.setActivationDate(LocalDateTime.now());

        if (this.getDuration() > 0) {
            this.setExpireDate(LocalDateTime.now().plusDays(this.getDuration()));
        }

        this.setLicenseUser(user);
    }
}
