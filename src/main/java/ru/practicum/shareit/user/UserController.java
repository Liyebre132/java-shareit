package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;
    @PostMapping
    public UserDto add(@Valid @RequestBody UserDto user) {
        return userService.addNewUser(user);
    }

    @PatchMapping("{id}")
    public UserDto update(@PathVariable long id, @RequestBody UserDto user) {
        return userService.update(id, user);
    }

    @GetMapping("{id}")
    public UserDto getById(@PathVariable long id) {
        return userService.getById(id);
    }

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll();
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable long id) {
        userService.delete(id);
    }
}
