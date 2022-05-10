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
import pl.banksystem.logic.domain.Role;
import pl.banksystem.logic.repository.RoleRepository;
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
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUser saveUser(AppUser user) {
        log.info("Saving new user {} to the database", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return appUserRepository.save(user);
    }

    public Role saveRole(Role role) {
        log.info("Saving new role {} to the database", role.getName());
        return roleRepository.save(role);
    }

    public void addRoleToUser(String username, String roleName) {
        log.info("Adding role {} to user {}", roleName, username);
        AppUser user = appUserRepository.findUserByUsername(username);
        Role role = roleRepository.findByName(roleName);
        user.getRoles().add(role);
    }

    public AppUser getUser(String username) {
        log.info("Fetching user {}", username);
        return appUserRepository.findUserByUsername(username);
    }

    public Role getRole(String roleName) {
        log.info("Fetching role {}", roleName);
        return roleRepository.findByName(roleName);
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