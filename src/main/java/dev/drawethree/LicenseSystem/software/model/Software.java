package dev.drawethree.LicenseSystem.software.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.drawethree.LicenseSystem.license.model.License;
import dev.drawethree.LicenseSystem.user.model.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "software")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class Software {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "visible", nullable = false)
    private boolean visible;

    @OrderBy("id desc")
    @OneToMany(mappedBy = "software", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private List<License> licenses;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "creator_id", nullable = false)
    @JsonIgnore
    private User creator;

    public Software(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @JsonIgnore
    public List<License> getActiveLicenses() {
        return licenses.stream().filter(License::isActive).collect(Collectors.toList());
    }
}
