package ru.kpekepsalt.ruvik.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kpekepsalt.ruvik.Urls;
import ru.kpekepsalt.ruvik.dto.UserDto;
import ru.kpekepsalt.ruvik.model.User;
import ru.kpekepsalt.ruvik.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static ru.kpekepsalt.ruvik.Urls.API_PATH;
import static ru.kpekepsalt.ruvik.Utils.ValidationUtils.isValid;

@RestController
@RequestMapping(API_PATH+ Urls.GATE.END_POINT)
public class GateController {

    @Autowired
    private UserService userService;

    @GetMapping("/auth")
    public ResponseEntity<UserDto> authUserByLogin() {
        if(!userService.isAuth()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = userService.getCurrentUser();
        User newUser = userService.updateUser(user);
        userService.save(newUser);
        UserDto dto = new UserDto(newUser);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/auth/{token}")
    public ResponseEntity<User> authUserByToken(@Valid @NotNull @PathVariable("token") String token) {
        if(isEmpty(token)) {
            return ResponseEntity.badRequest().body(new User());
        }
        User user = userService.findByToken(token);
        if(isEmpty(user)) {
            return ResponseEntity.notFound().build();
        }
        User update = userService.updateUser(user);
        update.setOldDatabaseKey(user.getDatabaseKey());
        userService.save(update);
        return ResponseEntity.ok(update);
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@NotNull @Valid @RequestBody UserDto userDto) {
        if(isValid(userDto)) {
            return ResponseEntity.badRequest().build();
        }
        User user = userService.findByLogin(userDto.getLogin());
        if(!isEmpty(user)){
            return ResponseEntity.status(HttpStatus.FOUND).build();
        }
        user = userService.createUser(userDto);
        userService.save(user);
         return ResponseEntity.ok(user);
    }

}
