package io.github.evaggelos99.ems.user.service.repository;

import io.github.evaggelos99.ems.common.api.db.CrudQueriesOperations;
import io.github.evaggelos99.ems.common.api.domainobjects.AbstractDomainObject;
import io.github.evaggelos99.ems.common.api.domainobjects.UserRole;
import io.github.evaggelos99.ems.user.api.User;
import io.github.evaggelos99.ems.user.api.UserDto;
import io.github.evaggelos99.ems.user.api.UserQueriesOperations;
import io.github.evaggelos99.ems.user.api.converters.UserDtoToUserConverter;
import io.github.evaggelos99.ems.user.api.repo.IUserRepository;
import io.github.evaggelos99.ems.user.api.repo.UserRowMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

@Component
public class UserRepository implements IUserRepository {

    private final DatabaseClient databaseClient;
    private final UserRowMapper userRowMapper;
    private final Function<UserDto, User> userDtoToUserConverter;
    private final Map<CrudQueriesOperations, String> crudQueriesProperties;
    private final Map<UserQueriesOperations, String> userQueriesProperties;

    /**
     * C-or
     *
     * @param databaseClient             the {@link DatabaseClient} used for
     *                                   connecting to the database for the User
     *                                   objects
     * @param userRowMapper            the {@link UserRowMapper} used for
     *                                   returning User objects from the database
     * @param userDtoToUserConverter the {@link UserDtoToUserConverter} used
     *                                   for converting {@link UserDto} to
     *                                   {@link User}
     * @param crudQueriesProperties      the {@link Map} which are used for
     *                                   getting the right query CRUD database
     *                                   operations
     */
    public UserRepository(final DatabaseClient databaseClient,
                          @Qualifier("userRowMapper") final UserRowMapper userRowMapper,
                          @Qualifier("userDtoToUserConverter") final Function<UserDto, User> userDtoToUserConverter,
                          @Qualifier("queriesProperties") final Map<CrudQueriesOperations, String> crudQueriesProperties,
                          @Qualifier("userQueriesProperties")final Map<UserQueriesOperations, String> userQueriesProperties) {

        this.databaseClient = databaseClient;
        this.userRowMapper = userRowMapper;
        this.userDtoToUserConverter = userDtoToUserConverter;
        this.crudQueriesProperties = crudQueriesProperties;
        this.userQueriesProperties = userQueriesProperties;
    }

    @Override
    public Mono<User> save(final UserDto dto) {

        return saveUser(dto);
    }

    @Override
    public Mono<User> findById(final UUID uuid) {

        return databaseClient.sql(crudQueriesProperties.get(CrudQueriesOperations.GET_ID))
                .bind(0, uuid)
                .map(userRowMapper)
                .one();
    }

    @Override
    public Mono<Boolean> deleteById(final UUID uuid) {

        return databaseClient.sql(crudQueriesProperties.get(CrudQueriesOperations.DELETE_ID)).bind(0, uuid).fetch().rowsUpdated().filter(this::rowsAffectedIsOne).map(x-> Boolean.TRUE);
    }

    @Override
    public Mono<Boolean> existsById(final UUID uuid) {

        return findById(uuid).map(Objects::nonNull).defaultIfEmpty(false);
    }

    @Override
    public Flux<User> findAll() {

        return databaseClient.sql(crudQueriesProperties.get(CrudQueriesOperations.GET_ALL)).map(userRowMapper).all();
    }

    @Override
    public Mono<User> edit(final UserDto dto) {

        return editUser(dto);
    }

    @Override
    public Mono<User> findByEntityId(final UUID entityUuid) {
        return databaseClient.sql(userQueriesProperties.get(UserQueriesOperations.GET_ENTITY_UUID))
                .bind(0, entityUuid)
                .map(userRowMapper)
                .one();
}

    @Override
    public Mono<Void> addEntityUuid(final User userDto, final UUID entityUuid) {

        return databaseClient.sql(userQueriesProperties.get(query(userDto.getRole())))
                .bind(0, entityUuid)
                .bind(1, userDto.getUuid())
                .fetch()
                .one()
                .then();
    }

    private UserQueriesOperations query(final UserRole role) {

        return switch (role) {
            case SPONSOR -> UserQueriesOperations.ADD_ENTITY_SPONSOR;
            case ATTENDEE -> UserQueriesOperations.ADD_ENTITY_ATTENDEE;
            case ORGANIZER -> UserQueriesOperations.ADD_ENTITY_ORGANIZER;
            case ADMIN -> throw new RuntimeException("Unreachable Code"); // todo refactor
        };
    }

    private Mono<User> editUser(final UserDto dto) {

        OffsetDateTime updatedAt = OffsetDateTime.now();
        return databaseClient.sql(crudQueriesProperties.get(CrudQueriesOperations.EDIT))
                .bind(0, updatedAt)
                .bind(1, dto.username())
                .bind(2, dto.email())
                .bind(3, dto.firstName())
                .bind(4, dto.lastName())
                .bind(5, dto.role())
                .bind(6, dto.mobilePhone())
                .bind(7, dto.birthDate())
                .bind(8, dto.uuid())
                .fetch().rowsUpdated()
                .filter(this::rowsAffectedIsOne).flatMap(x -> findById(dto.uuid()))
                .map(AbstractDomainObject::getCreatedAt)
                .map(createdAt -> userDtoToUserConverter.apply(
                        UserDto.from(dto)
                                .createdAt(createdAt)
                                .lastUpdated(updatedAt)
                                .build()));
    }

    private Mono<User> saveUser(final UserDto dto) {

        final OffsetDateTime createdAt = OffsetDateTime.now();

        final UserDto newDto = UserDto.from(dto).createdAt(createdAt).lastUpdated(createdAt).build();

        return databaseClient.sql(crudQueriesProperties.get(CrudQueriesOperations.SAVE))
                .bind(0, newDto.uuid())
                .bind(1, newDto.createdAt())
                .bind(2, newDto.lastUpdated())
                .bind(3, newDto.username())
                .bind(4, newDto.email())
                .bind(5, newDto.firstName())
                .bind(6, newDto.lastName())
                .bind(7, newDto.role())
                .bind(8, newDto.mobilePhone())
                .bind(9, newDto.birthDate())
                .fetch().rowsUpdated().filter(this::rowsAffectedIsOne).map(num -> userDtoToUserConverter.apply(newDto));
    }

    private boolean rowsAffectedIsOne(final Long x) {
        return x == 1;
    }

}
