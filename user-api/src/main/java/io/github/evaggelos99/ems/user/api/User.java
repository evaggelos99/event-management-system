package io.github.evaggelos99.ems.user.api;

import io.github.evaggelos99.ems.common.api.domainobjects.AbstractDomainObject;
import io.github.evaggelos99.ems.common.api.domainobjects.UserRole;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public final class User extends AbstractDomainObject {

    private final String username;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final UserRole role;
    private final String mobilePhone;
    private final LocalDate birthDate;

    public User(final UUID id, final OffsetDateTime createdAt, final OffsetDateTime lastUpdated,
                @NotNull final String username,
                @NotNull final String email,
                final String firstName,
                final String lastName,
                final UserRole role,
                final String mobilePhone,
                final LocalDate birthDate) {

        super(id, createdAt, lastUpdated);
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.mobilePhone = mobilePhone;
        this.birthDate = birthDate;
    }

    @Override
    public int hashCode() {

        return new HashCodeBuilder(17, 37).appendSuper(super.hashCode())
                .append(username)
                .append(email)
                .append(firstName)
                .append(lastName)
                .append(role)
                .append(mobilePhone)
                .append(birthDate)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object o) {

        if (this == o) return true;

        if (!(o instanceof final User user)) return false;

        return new EqualsBuilder().appendSuper(super.equals(o))
                .append(username, user.username)
                .append(email, user.email)
                .append(firstName, user.firstName)
                .append(lastName, user.lastName)
                .append(role, user.role)
                .append(mobilePhone, user.mobilePhone)
                .append(birthDate, user.birthDate)
                .isEquals();
    }

    @Override
    public String toString() {

        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("username", username)
                .append("email", email)
                .append("firstName", firstName)
                .append("lastName", lastName)
                .append("role", role)
                .append("mobilePhone", mobilePhone)
                .append("birthDate", birthDate)
                .toString();
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public UserRole getRole() {
        return role;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }
}
