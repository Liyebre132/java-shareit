package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.Marker;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank(groups = {Marker.OnCreate.class})
    private String name;

    @Email(groups = {Marker.OnUpdate.class, Marker.OnCreate.class})
    @NotNull(groups = {Marker.OnCreate.class})
    private String email;
}
