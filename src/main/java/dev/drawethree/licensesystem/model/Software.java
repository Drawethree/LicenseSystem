package dev.drawethree.LicenseSystem.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity(name = "Software")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class Software {

	@Id
	private Integer id;

	@Column(nullable = false, unique = true)
	private String name;

	@Column(nullable = false)
	private String description;

	@OrderBy("id desc")
	@OneToMany(mappedBy = "software")
	@ToString.Exclude
	private List<License> licenses;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Software software = (Software) o;
		return id != null && Objects.equals(id, software.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
