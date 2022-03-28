package dev.drawethree.LicenseSystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LicenseStatus {

	ACTIVE(1, "Active", "License is active"),
	WAITING_FOR_ACTIVATION(2, "Waiting For Activation", "License is waiting for activation"),
	EXPIRED(3, "Expired", "License has expired");

	private int id;
	private String name;
	private String description;
}
