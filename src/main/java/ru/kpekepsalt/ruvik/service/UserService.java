package ru.kpekepsalt.ruvik.service;

import ru.kpekepsalt.ruvik.dto.UserDto;
import ru.kpekepsalt.ruvik.exception.DataValidityException;
import ru.kpekepsalt.ruvik.model.User;


public interface UserService {

    /**
     * Search User by id
     * @param id User id
     * @return User data or null if not exists
     */
    User findById(Long id);

    /**
     * Saves user data
     * @param user User data to save
     */
    void save(User user) throws DataValidityException;

    /**
     * Creates user profile
     * @param userDto User information
     * @return Saved user profile data
     */
    User createUser(UserDto userDto) throws DataValidityException;

    /**
     * Searches for user with given login
     * @param login User login
     * @return User data or null if not exists
     */
    User findByLogin(String login);

    /**
     * Updates user profile
     * @param user User profile data
     * @return Updated user profile data
     */
    User updateUser(User user) throws DataValidityException;

    /**
     * Searches for user with given token
     * @param token User token
     * @return User or null if not exists
     */
    User findByToken(String token);

    /**
     * Get current user profile data
     * @return Current user's data
     */
    User getCurrentUser();

    /**
     * Checks if user is authorized
     * @return User authorization status
     */
    boolean isAuth();
}
