package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    @Validated({Marker.OnCreate.class})
    public UserDto add(@RequestBody @Valid UserDto user) {
        return userService.addNewUser(user);
    }

    @PatchMapping("{id}")
    @Validated({Marker.OnUpdate.class})
    public UserDto update(@PathVariable long id, @RequestBody @Valid UserDto user) {
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
