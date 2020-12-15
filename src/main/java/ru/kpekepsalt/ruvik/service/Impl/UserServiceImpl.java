package ru.kpekepsalt.ruvik.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import ru.kpekepsalt.ruvik.dto.UserDto;
import ru.kpekepsalt.ruvik.model.AppUserDetails;
import ru.kpekepsalt.ruvik.model.User;
import ru.kpekepsalt.ruvik.repository.UserRepository;
import ru.kpekepsalt.ruvik.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final int TOKEN_LENGTH = 32;
    private final int DB_KEY_LENGTH = 16;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User findByLoginAndPassword(String login, String password) {
        return userRepository.findByLoginAndPassword(login, password).orElse(null);
    }

    @Override
    public User createUser(UserDto userDto) {
        User user = new User(userDto);
        user.setPassword(
                passwordEncoder.encode(userDto.getPassword())
        );
        user.setToken(
                CipherUtils.randomStringKey(TOKEN_LENGTH)
        );
        user.setDatabaseKey(
                CipherUtils.randomStringKey(DB_KEY_LENGTH)
        );
        user.setOldDatabaseKey(
                user.getDatabaseKey()
        );
        return user;
    }

    @Override
    public User updateUser(User user) {
        user.setToken(
                CipherUtils.randomStringKey(TOKEN_LENGTH)
        );
        user.setOldDatabaseKey(user.getDatabaseKey());
        user.setDatabaseKey(
                CipherUtils.randomStringKey(DB_KEY_LENGTH)
        );
        return user;
    }

    @Override
    public User findByToken(String token) {
        return userRepository.findByToken(token).orElse(null);
    }

    @Override
    public User getCurrentUser() {
        return userDetailsService.getUser();
    }

    @Override
    public boolean isAuth() {
        return userDetailsService.isAuthenticated();
    }

    @Override
    public User findByLogin(String login) {
        return userRepository.findByLogin(login).orElse(null);
    }

}
