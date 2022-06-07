package pl.banksystem.logic.domain;

import lombok.*;
import pl.banksystem.logic.account.Account;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String username;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "client")
    @ToString.Exclude
    private List<Account> accounts;

    public AppUser(String username, String password) {
        this.username = username;
        this.password = password;
    }
}