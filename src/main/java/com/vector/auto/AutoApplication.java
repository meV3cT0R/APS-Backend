package com.vector.auto;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vector.auto.model.Autopart;
import com.vector.auto.model.Role;
import com.vector.auto.model.User;
import com.vector.auto.repository.PartsRepo;
import com.vector.auto.repository.UserRepo;

@SpringBootApplication
public class AutoApplication {
	public static void main(String[] args) {
		SpringApplication.run(AutoApplication.class, args);
	}
	@Bean
	public CommandLineRunner loadSomeData(PartsRepo partsRepo,UserRepo userRepo,PasswordEncoder encoder) {
		return (args)-> {
			// Autopart part1 = new Autopart();
			// part1.setName("new part");
			// part1.setPrice(9845.22);
			// part1.setCategory("Engine parts");
			// part1.setBrand("Mercedes");

			// partsRepo.save(part1);
			// Autopart part2 = new Autopart();
			// part2.setName("new part");
			// part2.setPrice(9845.22);
			// part2.setCategory("Brake parts");
			// part2.setBrand("BMW");
			// partsRepo.save(part2);

			User admin = new User();
			admin.setName("super admin");
			admin.setUsername("superadmin");
			admin.setPassword(encoder.encode("superadmin"));
			admin.setRole(Role.ADMIN);

			userRepo.save(admin);
			System.out.println("Data inserted");
		};
	}
}
