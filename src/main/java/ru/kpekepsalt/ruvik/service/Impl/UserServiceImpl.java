package ru.kpekepsalt.ruvik.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kpekepsalt.ruvik.enums.Role;
import ru.kpekepsalt.ruvik.exception.DataValidityException;
import ru.kpekepsalt.ruvik.mapper.UserMapper;
import ru.kpekepsalt.ruvik.objects.ValidationResult;
import ru.kpekepsalt.ruvik.utils.CipherUtils;
import ru.kpekepsalt.ruvik.dto.UserDto;
import ru.kpekepsalt.ruvik.model.User;
import ru.kpekepsalt.ruvik.repository.UserRepository;
import ru.kpekepsalt.ruvik.service.UserService;

import static ru.kpekepsalt.ruvik.utils.ValidationUtils.validate;

/**
 * Service for user profile operations
 */
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
        return userRepository
                .findById(id)
                .orElse(null);
    }

    @Override
    public void save(User user) throws DataValidityException {
        ValidationResult validationResult = validate(user);
        if(!validationResult.isValid()) {
            throw new DataValidityException(
                    validationResult.getFirstErrorMessage()
            );
        }
        userRepository.save(user);
    }

    @Override
    public User createUser(UserDto userDto) throws DataValidityException{
        ValidationResult validationResult = validate(userDto);
        if(!validationResult.isValid())
        {
            throw new DataValidityException(
                    validationResult.getFirstErrorMessage()
            );
        }
        User user = UserMapper.INSTANCE.dtoToUser(userDto);
        user.setRole(Role.USER);
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
    public User updateUser(User user) throws DataValidityException{
        ValidationResult validationResult = validate(user);
        if(!validationResult.isValid()) {
            throw new DataValidityException(
                    validationResult.getFirstErrorMessage()
            );
        }
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
