package io.github.evaggelos99.ems.common.api.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record IdpUserProperties(UUID id,
                                String firstName,
                                String lastName,
                                String email,
                                LocalDate birthDate,
                                String mobilePhone,
                                List<String> roles) {

}
