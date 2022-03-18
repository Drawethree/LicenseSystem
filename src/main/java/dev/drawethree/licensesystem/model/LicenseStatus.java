package dev.drawethree.LicenseSystem.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum LicenseStatus {

	ACTIVE(1, "Active", "License is active"),
	WAITING_FOR_ACTIVATION(2, "Waiting For Activation", "License is waiting for activation"),
	EXPIRED(3, "Expired", "License has expired");

	private Integer id;
	private String name;
	private String description;
}
