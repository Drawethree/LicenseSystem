package dev.drawethree.LicenseSystem.utils;

import java.util.UUID;

public final class LicenseKeyGenerator {

	public static String generateNewLicenseKey() {
		return UUID.randomUUID().toString();
	}
}
