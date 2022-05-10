package pl.banksystem;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.banksystem.logic.domain.AppUser;
import pl.banksystem.logic.domain.Role;
import pl.banksystem.logic.service.UserService;

@SpringBootApplication
public class BankSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankSystemApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner run(UserService userService) {
        return args -> {
            userService.saveUser(new AppUser("root", "root"));
            userService.saveRole(new Role("USER"));
            userService.saveRole(new Role("ADMIN"));
            userService.addRoleToUser("root", "USER");
            userService.addRoleToUser("root", "ADMIN");
        };
    }
}