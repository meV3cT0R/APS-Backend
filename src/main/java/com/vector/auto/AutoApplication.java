package com.vector.auto;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vector.auto.model.Autopart;
import com.vector.auto.model.Category;
import com.vector.auto.model.Role;
import com.vector.auto.model.User;
import com.vector.auto.repository.CatRepo;
import com.vector.auto.repository.PartsRepo;
import com.vector.auto.repository.UserRepo;
import java.util.Map;
import java.util.HashMap;

@SpringBootApplication
public class AutoApplication {
	public static void main(String[] args) {
		SpringApplication.run(AutoApplication.class, args);
	}
	@Bean
	public CommandLineRunner loadSomeData(PartsRepo partsRepo,UserRepo userRepo,PasswordEncoder encoder,CatRepo catRepo) {
		return (args)-> {
			// Autopart part1 = new Autopart();
			// part1.setName("new part");
			// part1.setPrice(984.22);

			// // Optional<Category> cat = catRepo.findById(1);
			// Category cat = new Category();
			
			// cat.setName("Automotive fittings");
			// cat.setImage("https://media.istockphoto.com/id/492251078/photo/engineer-balancing-car-wheel-on-balancer.jpg?s=612x612&w=0&k=20&c=Qit8A04ws5T6pLo1uEemevDhfCYqtrhNPS9SaXimg_s=");
			// cat=catRepo.save(cat);


			// part1.setCategory(null);

			// Map<String,String> map = new HashMap<>();

			// map.put("dimension","2x2");
			// part1.setBrand("Mercedes");
			// part1.setSpecs(map);
			// part1.setImages(Arrays.asList(new String[]{"https://media.torque.com.sg/public/2019/04/owen-developments-turbocharger.jpg","https://turboturbos.com/cdn/shop/articles/4eWj21svKe9cIQGORToPXi4w0UeIxott1608233063_1200x1200.jpg?v=1610326491"}));
			// partsRepo.save(part1);


			
			// Autopart part2 = new Autopart();
			// part2.setImages(Arrays.asList(new String[]{"https://media.torque.com.sg/public/2019/04/owen-developments-turbocharger.jpg","https://turboturbos.com/cdn/shop/articles/4eWj21svKe9cIQGORToPXi4w0UeIxott1608233063_1200x1200.jpg?v=1610326491"}));

			// part2.setName("new part");
			// part2.setPrice(9845.22);
			// part2.setCategory(null);
			// part2.setBrand("BMW");
			// part2.setSpecs(map);

			// partsRepo.save(part2);

			// User admin = new User();
			// admin.setName("super admin");
			// admin.setUsername("superadmin");
			// admin.setPassword(encoder.encode("superadmin"));
			// admin.setRole(Role.ADMIN);

			// userRepo.save(admin);

			// User user = new User();
			// user.setName("user");
			// user.setUsername("user");
			// user.setPassword(encoder.encode("user"));
			// user.setRole(Role.USER);

			// userRepo.save(user);
			// System.out.println("Data inserted");
		};
	}
}
