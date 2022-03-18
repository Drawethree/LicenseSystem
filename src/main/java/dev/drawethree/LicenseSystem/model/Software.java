package dev.drawethree.licensesystem.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "software")
@NoArgsConstructor
@AllArgsConstructor
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

	@OrderBy("id desc")
	@OneToMany(mappedBy = "software",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@ToString.Exclude
	private List<License> licenses;

	public Software(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public void addLicense(License license) {
		if (licenses == null) {
			licenses = new ArrayList<>();
		}
		licenses.add(license);
	}

}
