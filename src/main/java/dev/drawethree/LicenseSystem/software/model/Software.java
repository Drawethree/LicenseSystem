package dev.drawethree.LicenseSystem.software.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import dev.drawethree.LicenseSystem.license.model.License;
import dev.drawethree.LicenseSystem.user.model.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "visible", nullable = false)
    private boolean visible;

    @OrderBy("id desc")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "software", cascade = CascadeType.ALL)
    private List<License> licenses;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "creator_id")
    @JsonBackReference
    private User creator;

    @JsonIgnore
    public List<License> getActiveLicenses() {
        return this.licenses.stream().filter(License::isActive).collect(Collectors.toList());
    }

    /*public void addLicense(License license) {

        if (this.licenses == null) {
            this.licenses = new ArrayList<>();
        }

        license.setSoftware(this);
        licenses.add(license);
    }

    public void removeLicense(License license) {

        if (this.licenses == null) {
            this.licenses = new ArrayList<>();
        }

        if (!this.licenses.contains(license)) {
            return;
        }

        license.setSoftware(null);
        licenses.remove(license);
    }*/

}
