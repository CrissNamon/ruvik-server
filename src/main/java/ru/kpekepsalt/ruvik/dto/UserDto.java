package ru.kpekepsalt.ruvik.dto;

import ru.kpekepsalt.ruvik.model.User;

import javax.validation.constraints.NotBlank;

public class UserDto {

    private Long id;

    private String publicIdentityKey;

    @NotBlank(message = "Public key A can't be empty")
    private String publicIdentityA;

    @NotBlank(message = "Public key B can't be empty")
    private String publicIdentityB;

    @NotBlank(message = "User login can't be empty")
    private String login;

    @NotBlank(message = "User password can't be empty")
    private String password;

    private String databaseKey;
    private String oldDatabaseKey;
    private String token;

    public UserDto() {}

    public UserDto(User user) {
        setId(user.getId());
        setLogin(user.getLogin());
        setPassword(user.getPassword());
        setPublicIdentityA(user.getPublicIdentityKeyA());
        setPublicIdentityB(user.getPublicIdentityKeyB());
        setDatabaseKey(user.getDatabaseKey());
        setOldDatabaseKey(user.getOldDatabaseKey());
        setToken(user.getToken());
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

    public String getPublicIdentityKey() {
        return publicIdentityKey;
    }

    public void setPublicIdentityKey(String publicIdentityKey) {
        this.publicIdentityKey = publicIdentityKey;
    }

    public String getDatabaseKey() {
        return databaseKey;
    }

    public void setDatabaseKey(String databaseKey) {
        this.databaseKey = databaseKey;
    }

    public String getOldDatabaseKey() {
        return oldDatabaseKey;
    }

    public void setOldDatabaseKey(String oldDatabaseKey) {
        this.oldDatabaseKey = oldDatabaseKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPublicIdentityA() {
        return publicIdentityA;
    }

    public void setPublicIdentityA(String publicIdentityA) {
        this.publicIdentityA = publicIdentityA;
    }

    public String getPublicIdentityB() {
        return publicIdentityB;
    }

    public void setPublicIdentityB(String publicIdentityB) {
        this.publicIdentityB = publicIdentityB;
    }
}
