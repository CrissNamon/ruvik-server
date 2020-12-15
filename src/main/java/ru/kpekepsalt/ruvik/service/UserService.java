package ru.kpekepsalt.ruvik.service;

import ru.kpekepsalt.ruvik.dto.UserDto;
import ru.kpekepsalt.ruvik.model.User;


public interface UserService {

    User findById(Long id);
    void save(User user);
    User findByLoginAndPassword(String login, String password);
    User createUser(UserDto userDto);
    User findByLogin(String login);
    User updateUser(User user);
    User findByToken(String token);
    User getCurrentUser();
    boolean isAuth();
}
