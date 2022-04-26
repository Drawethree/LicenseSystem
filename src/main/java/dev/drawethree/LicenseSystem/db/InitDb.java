package dev.drawethree.LicenseSystem.db;

import dev.drawethree.LicenseSystem.license.generation.LicenseGenerator;
import dev.drawethree.LicenseSystem.license.model.License;
import dev.drawethree.LicenseSystem.software.model.Software;
import dev.drawethree.LicenseSystem.user.model.User;
import dev.drawethree.LicenseSystem.user.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Component
public class InitDb {

	private final UserService userService;

	private final LicenseGenerator licenseGenerator;

	public InitDb(UserService userService, @Qualifier("UUIDLicenseGenerator") LicenseGenerator licenseGenerator) {
		this.userService = userService;
		this.licenseGenerator = licenseGenerator;
	}

	@PostConstruct
	public void init() {
		initUsers();
	}

	private void initUsers() {

		User admin = new User();
		admin.setUsername("admin");
		admin.setPassword("admin");
		admin.setRole("ADMIN");
		admin.setEmail("admin@test.com");
		admin.setCreatedAt(LocalDateTime.now());
		admin.setId(1);
		userService.saveAndEncryptPassword(admin);

		User customer = new User();
		customer.setUsername("customer");
		customer.setPassword("customer");
		customer.setRole("CUSTOMER");
		customer.setEmail("customer@test.com");
		customer.setCreatedAt(LocalDateTime.now());
		customer.setId(2);
		userService.saveAndEncryptPassword(customer);

		User creator = new User();
		creator.setUsername("creator");
		creator.setPassword("creator");
		creator.setRole("CREATOR");
		creator.setEmail("creator@test.com");
		creator.setCreatedAt(LocalDateTime.now());
		creator.setId(3);

		Software software1 = new Software();
		software1.setName("Stronghold Crusader");
		software1.setLicenses(new ArrayList<>());
		software1.setCreatedAt(LocalDateTime.now());
		software1.setDescription("Strategic Game.");
		software1.setVisible(true);
		software1.setId(1);
		software1.setCreator(creator);

		Software software2 = new Software();
		software2.setName("Warhammer I");
		software2.setLicenses(new ArrayList<>());
		software2.setCreatedAt(LocalDateTime.now());
		software2.setDescription("Another Strategic Game.");
		software2.setVisible(true);
		software2.setId(2);
		software2.setCreator(creator);

		License license = new License();
		license.setDuration(0);
		license.setLicenseKey(licenseGenerator.generateLicenseKey());
		license.setSoftware(software1);
		license.setCreatedAt(LocalDateTime.now());
		license.setId(1);
		license.activate(customer);

		License license2 = new License();
		license2.setDuration(0);
		license2.setLicenseKey(licenseGenerator.generateLicenseKey());
		license2.setSoftware(software2);
		license2.setCreatedAt(LocalDateTime.now());
		license2.setId(2);
		license2.activate(customer);

		userService.saveAndEncryptPassword(creator);

	}
}