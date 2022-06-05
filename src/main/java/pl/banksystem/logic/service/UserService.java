package pl.banksystem.logic.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.banksystem.logic.domain.AppUser;
import pl.banksystem.logic.repository.AppUserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public void saveUser(AppUser user) {
        log.info("Saving new user {} to the database", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        appUserRepository.save(user);
    }

    public AppUser getUser(String username) {
        log.info("Fetching user {}", username);
        return appUserRepository.findUserByUsername(username);
    }

    public List<AppUser> getAllUsers() {
        log.info("Fetching all users");
        return appUserRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findUserByUsername(username);

        if (user == null) {
            log.error("Couldn't find user \"{}\" in the database", username);
            throw new UsernameNotFoundException(String.format("Couldn't find user %s", username));
        }
        log.info("Found user \"{}\" in the database", username);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new User(user.getUsername(), user.getPassword(), authorities);
    }
}