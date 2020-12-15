package ru.kpekepsalt.ruvik.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kpekepsalt.ruvik.dto.UserDto;
import ru.kpekepsalt.ruvik.model.User;
import ru.kpekepsalt.ruvik.service.UserService;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@RestController
@RequestMapping("/api/v1/gate")
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

    @GetMapping("/auth-token/{token}")
    public ResponseEntity<User> authUserByToken(@PathVariable("token") String token) {
        if(isEmpty(token)) {
            return ResponseEntity.badRequest().build();
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
    public ResponseEntity<User> registerUser(@RequestBody UserDto userDto) {
        if(isEmpty(userDto)) {
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
