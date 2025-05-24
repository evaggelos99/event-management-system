package io.github.evaggelos99.ems.user.api.converters;

import io.github.evaggelos99.ems.user.api.User;
import io.github.evaggelos99.ems.user.api.UserDto;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class UserDtoToUserConverter implements Function<UserDto, User> {

    @Override
    public User apply(final UserDto userDto) {

        return new User(userDto.uuid(), userDto.createdAt(), userDto.lastUpdated(),
                userDto.username(),
                userDto.email(),
                userDto.firstName(),
                userDto.lastName(),
                userDto.role(),
                userDto.mobilePhone(),
                userDto.birthDate());
    }

}
