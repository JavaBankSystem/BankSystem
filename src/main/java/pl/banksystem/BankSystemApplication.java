package pl.banksystem;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.banksystem.logic.domain.AppUser;
import pl.banksystem.logic.domain.Role;
import pl.banksystem.logic.service.RoleService;
import pl.banksystem.logic.service.UserService;

@SpringBootApplication
public class BankSystemApplication {
    //@Autowired
    //private EmailSenderService senderService;

    public static void main(String[] args) {
        SpringApplication.run(BankSystemApplication.class, args);
    }

    /*    @EventListener(ApplicationReadyEvent.class)
        public void sendMail() throws MessagingException {
            senderService.sendMail("michal.jemiolek@gmail.com","Email from java bank system",
                    "Hello user.\nWe are sorry but you don't have creditworthiness to take a loan in our bank");
            senderService.sendMessageWithAttachment("michal.jemiolek@gmail.com","Email from java bank system",
                    "Hello user.","C:\\Users\\plik.gif");
        }
    */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner run(UserService userService, RoleService roleService) {
        return args -> {
            userService.saveUser(new AppUser("root", "root"));
            roleService.saveRole(new Role("USER"));
            roleService.saveRole(new Role("ADMIN"));
            roleService.addRoleToUser("root", "USER");
            roleService.addRoleToUser("root", "ADMIN");
        };
    }
}