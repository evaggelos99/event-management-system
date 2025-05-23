package io.github.evaggelos99.ems.security.lib;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record IdentityUserDto(UUID id,
                              String firstName,
                              String lastName,
                              String email,
                              LocalDate birthDate,
                              String mobilePhone,
                              List<String> roles) {

}
