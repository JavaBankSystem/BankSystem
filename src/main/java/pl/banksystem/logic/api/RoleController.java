package pl.banksystem.logic.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.banksystem.logic.domain.AppUser;
import pl.banksystem.logic.domain.Role;
import pl.banksystem.logic.service.RoleService;
import pl.banksystem.logic.service.UserService;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;
    private final UserService userService;

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok().body(roleService.getAllRoles());
    }

    @GetMapping("/roles/{roleName}")
    public ResponseEntity<Role> getRole(@PathVariable String roleName) {
        return ResponseEntity.ok().body(roleService.getRole(roleName));
    }

    @PostMapping("/roles")
    public ResponseEntity<Role> saveRole(@RequestBody Role role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/role/save").toUriString());
        roleService.saveRole(role);
        return ResponseEntity.created(uri).body(role);
    }

    @PostMapping("/roles/addtouser")
    public ResponseEntity<AppUser> addRoleToUser(
            @RequestParam(name = "username") String username,
            @RequestParam(name = "roleName") String roleName) {
        roleService.addRoleToUser(username, roleName);
        return ResponseEntity.ok().body(userService.getUser(username));
    }
}
