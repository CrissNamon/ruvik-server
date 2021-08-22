package ru.kpekepsalt.ruvik.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kpekepsalt.ruvik.dto.ErrorResponseDto;
import ru.kpekepsalt.ruvik.dto.ResponseDto;
import ru.kpekepsalt.ruvik.dto.UserDto;
import ru.kpekepsalt.ruvik.exception.DataValidityException;
import ru.kpekepsalt.ruvik.mapper.UserMapper;
import ru.kpekepsalt.ruvik.model.User;
import ru.kpekepsalt.ruvik.objects.ValidationResult;
import ru.kpekepsalt.ruvik.service.UserService;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotBlank;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static ru.kpekepsalt.ruvik.Urls.API_PATH;
import static ru.kpekepsalt.ruvik.Urls.GATE;
import static ru.kpekepsalt.ruvik.utils.ValidationUtils.validate;

/**
 * Controller for user account authorization
 */
@RestController
@RequestMapping(API_PATH + GATE.END_POINT)
@Validated
public class GateController {

    @Autowired
    private UserService userService;

    /**
     * @return User information
     */
    @GetMapping(GATE.AUTH)
    public ResponseEntity<ResponseDto<UserDto>> authUserByLogin() throws DataValidityException {
        if (!userService.isAuth()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = userService.getCurrentUser();
        User newUser = userService.updateUser(user);
        userService.save(newUser);
        UserDto dto = UserMapper.INSTANCE.userToDto(newUser);
        return ResponseEntity.ok(
                new ResponseDto<>("", dto)
        );
    }

    /**
     * @param token User token for authorization
     * @return User information
     */
    @GetMapping(GATE.AUTH + "/{token}")
    public ResponseEntity<ResponseDto<UserDto>> authUserByToken(
            @PathVariable("token") @NotBlank(message = "User token can't be empty") String token) throws DataValidityException {
        User user = userService.findByToken(token);
        if (isEmpty(user)) {
            return ResponseEntity.notFound().build();
        }
        User update = userService.updateUser(user);
        update.setOldDatabaseKey(user.getDatabaseKey());
        userService.save(update);
        UserDto dto = UserMapper.INSTANCE.userToDto(update);
        return ResponseEntity.ok(
                new ResponseDto<>("", dto)
        );
    }

    /**
     * @param userDto User information to register
     * @return Registered user information
     */
    @PostMapping(GATE.REGISTER)
    public ResponseEntity<ResponseDto<UserDto>> registerUser(@RequestBody UserDto userDto) throws DataValidityException {
        ValidationResult validationResult = validate(userDto);
        if (!validationResult.isValid()) {
            return ResponseEntity
                    .badRequest()
                    .body(
                            new ErrorResponseDto<>(validationResult.getFirstErrorMessage())
                    );
        }
        User user = userService.findByLogin(userDto.getLogin());
        if (!isEmpty(user)) {
            return ResponseEntity.status(HttpStatus.FOUND).build();
        }
        user = userService.createUser(userDto);
        userService.save(user);
        UserDto response = UserMapper.INSTANCE.userToDto(user);
        return ResponseEntity.ok(
                new ResponseDto<>("", response)
        );
    }

    /**
     * @param e Constraint violation exception object
     * @return Error response
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto<Exception>> handleConstraintViolationException(ConstraintViolationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        new ErrorResponseDto<>(e.getMessage())
                );
    }

    @ExceptionHandler(DataValidityException.class)
    public ResponseEntity<ErrorResponseDto<Exception>> handleDataValidityException(DataValidityException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorResponseDto<>(e.getMessage())
                );
    }

}
