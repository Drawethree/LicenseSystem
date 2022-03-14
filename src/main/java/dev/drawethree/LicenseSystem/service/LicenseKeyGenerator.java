package dev.drawethree.LicenseSystem.service;

import java.util.UUID;

public class LicenseKeyGenerator {

	public static String generateNewLicenseKey() {
		return UUID.randomUUID().toString();
	}
}
