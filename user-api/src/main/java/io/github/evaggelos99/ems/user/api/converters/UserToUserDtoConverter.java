package io.github.evaggelos99.ems.user.api.converters;

import io.github.evaggelos99.ems.user.api.User;
import io.github.evaggelos99.ems.user.api.UserDto;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class UserToUserDtoConverter implements Function<User, UserDto> {

    @Override
    public UserDto apply(final User ticket) {

        return UserDto.builder()
                .uuid(ticket.getUuid())
                .createdAt(ticket.getCreatedAt())
                .lastUpdated(ticket.getLastUpdated())
                .username(ticket.getUsername())
                .email(ticket.getEmail())
                .firstName(ticket.getFirstName())
                .lastName(ticket.getLastName())
                .role(ticket.getRole())
                .mobilePhone(ticket.getMobilePhone())
                .birthDate(ticket.getBirthDate())
                .build();
    }

}
