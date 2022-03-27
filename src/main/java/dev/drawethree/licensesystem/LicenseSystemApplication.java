package dev.drawethree.LicenseSystem;

import dev.drawethree.LicenseSystem.model.Software;
import dev.drawethree.LicenseSystem.repo.SoftwareRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class LicenseSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(LicenseSystemApplication.class, args);
	}

	@Configuration
	static
	class LoadDatabase {

		private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

		@Bean
		CommandLineRunner initDatabase(SoftwareRepository repository) {

			return args -> {
				Software sw = new Software();
				sw.setId(1);
				sw.setName("Test");
				sw.setDescription("Test desc");
				log.info("Preloading " + repository.save(sw));
			};
		}
	}

}
