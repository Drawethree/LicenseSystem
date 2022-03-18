package dev.drawethree.LicenseSystem.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "License")
@Data
@NoArgsConstructor
public class License {

	@Column(nullable = false)
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="software_id", nullable=false)
	private Software software;

	@Column(nullable = false, unique = true)
	private String licenseKey;

	@Column(nullable=false)
	private String licenseUser;

	@Column(nullable=false)
	private LocalDateTime createdAt;

	private LocalDateTime expiresAt;

	@Column(name = "status_id")
	@Enumerated
	private LicenseStatus status;
}
