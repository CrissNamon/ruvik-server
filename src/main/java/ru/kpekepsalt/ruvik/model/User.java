package ru.kpekepsalt.ruvik.model;

import org.springframework.stereotype.Component;
import ru.kpekepsalt.ruvik.dto.UserDto;
import ru.kpekepsalt.ruvik.enums.Role;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity(name = "appuser")
@Component
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "login")
    @NotBlank(message = "User login can't be empty")
    private String login;

    @Column(name = "password")
    @NotBlank(message = "User password can't be empty")
    private String password;

    @Column(name = "token")
    private String token;

    @Column(name = "identity_key_a")
    private String publicIdentityKeyA;

    @Column(name = "identity_key_b")
    private String publicIdentityKeyB;

    @Column(name = "database_key")
    private String databaseKey;

    @Column(name = "role")
    private Role role;

    @Transient
    private String oldDatabaseKey;

    public User() {}

    public User(UserDto userDto) {
        this.id = userDto.getId();
        this.login = userDto.getLogin();
        this.publicIdentityKeyA = userDto.getPublicIdentityA();
        this.publicIdentityKeyB = userDto.getPublicIdentityB();
        this.role = Role.USER;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPublicIdentityKeyA() {
        return publicIdentityKeyA;
    }

    public void setPublicIdentityKeyA(String publicIdentityKeyA) {
        this.publicIdentityKeyA = publicIdentityKeyA;
    }

    public String getPublicIdentityKeyB() {
        return publicIdentityKeyB;
    }

    public void setPublicIdentityKeyB(String publicIdentityKeyB) {
        this.publicIdentityKeyB = publicIdentityKeyB;
    }

    public String getDatabaseKey() {
        return databaseKey;
    }

    public void setDatabaseKey(String databaseKey) {
        this.databaseKey = databaseKey;
    }

    public static User copy(User from) {
        User to = new User();
        to.setToken(from.getToken());
        to.setDatabaseKey(from.getDatabaseKey());
        to.setId(from.getId());
        to.setPublicIdentityKeyA(from.getPublicIdentityKeyA());
        to.setPublicIdentityKeyB(from.getPublicIdentityKeyB());
        to.setLogin(from.getLogin());
        to.setPassword(from.getPassword());
        return to;
    }

    public String getOldDatabaseKey() {
        return oldDatabaseKey;
    }

    public void setOldDatabaseKey(String oldDatabaseKey) {
        this.oldDatabaseKey = oldDatabaseKey;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
