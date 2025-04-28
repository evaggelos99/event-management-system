package io.github.evaggelos99.ems.user.api;

import io.github.evaggelos99.ems.common.api.domainobjects.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * User Dto object
 *
 * @author Evangelos Georgiou
 */
public record UserDto(
        @Schema(hidden = true, description = "The UUID of the User") UUID uuid,
        @Null @Schema(hidden = true) OffsetDateTime createdAt, //
        @Null @Schema(hidden = true) OffsetDateTime lastUpdated,
        @NotNull @Schema(description = "The username of the User") String username,
        @NotNull @Schema(description = "The email of the User") String email,
        @Schema(description = "The first name of the User") String firstName,
        @Schema(description = "The last name of the User") String lastName,
        @NotNull @Schema(description = "The role of the User") UserRole role,
        @Schema(description = "The phone number of the User") String mobilePhone,
        @Schema(description = "The birth date of the User") LocalDate birthDate
) {

    public static Builder builder() {

        return new Builder();
    }

    public static Builder from(final UserDto dto) {

        return new Builder()
                .uuid(dto.uuid)
                .createdAt(dto.createdAt)
                .lastUpdated(dto.lastUpdated)
                .username(dto.username)
                .email(dto.email)
                .firstName(dto.firstName)
                .lastName(dto.lastName)
                .role(dto.role)
                .mobilePhone(dto.mobilePhone)
                .birthDate(dto.birthDate);
    }

    public static final class Builder {

        private UUID uuid;
        private OffsetDateTime createdAt;
        private OffsetDateTime lastUpdated;
        private String username;
        private String email;
        private String firstName;
        private String lastName;
        private UserRole role;
        private String mobilePhone;
        private LocalDate birthDate;

        private Builder() {

        }

        public UserDto build() {

            return new UserDto(uuid, createdAt, lastUpdated, username, email, firstName, lastName, role, mobilePhone, birthDate);
        }

        public Builder uuid(final UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder createdAt(final OffsetDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder lastUpdated(final OffsetDateTime lastUpdated) {
            this.lastUpdated = lastUpdated;
            return this;
        }

        public Builder username(final String username) {
            this.username = username;
            return this;
        }

        public Builder email(final String email) {
            this.email = email;
            return this;
        }

        public Builder firstName(final String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(final String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder role(final UserRole role) {
            this.role = role;
            return this;
        }

        public Builder mobilePhone(final String mobilePhone) {
            this.mobilePhone = mobilePhone;
            return this;
        }

        public Builder birthDate(final LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }
    }

}
