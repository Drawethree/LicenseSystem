package dev.drawethree.LicenseSystem.license.utils;

import java.util.UUID;

public final class LicenseKeyGenerator {

	public static String generateNewLicenseKey() {
		return UUID.randomUUID().toString();
	}
}
