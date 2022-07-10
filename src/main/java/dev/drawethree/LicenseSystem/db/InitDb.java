package dev.drawethree.LicenseSystem.db;

import dev.drawethree.LicenseSystem.user.model.User;
import dev.drawethree.LicenseSystem.user.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@Component
public class InitDb {

	private final UserService userService;

	public InitDb(UserService userService)  {
		this.userService = userService;
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
		userService.saveAndEncryptPassword(creator);

	}
}