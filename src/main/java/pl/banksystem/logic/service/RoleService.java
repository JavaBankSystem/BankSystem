package pl.banksystem.logic.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.banksystem.logic.domain.AppUser;
import pl.banksystem.logic.domain.Role;
import pl.banksystem.logic.repository.AppUserRepository;
import pl.banksystem.logic.repository.RoleRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final AppUserRepository appUserRepository;

    public void saveRole(Role role) {
        log.info("Saving new role {} to the database", role.getName());
        roleRepository.save(role);
    }

    public List<Role> getAllRoles() {
        log.info("Fetching all roles");
        return roleRepository.findAll();
    }

    public Role getRole(String roleName) {
        log.info("Fetching role {}", roleName);
        return roleRepository.findByName(roleName);
    }

    public void addRoleToUser(String username, String roleName) {
        log.info("Adding role {} to user {}", roleName, username);
        AppUser user = appUserRepository.findUserByUsername(username);
        Role role = roleRepository.findByName(roleName);
        user.getRoles().add(role);
    }
}
