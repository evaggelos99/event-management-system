package io.github.evaggelos99.ems.common.api.domainobjects.validators.constraints;

import io.github.evaggelos99.ems.common.api.controller.exceptions.UnauthorizedRoleException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

public class PublisherValidator {

    public static <T> Flux<T> validateBooleanFlux(final boolean x, final Supplier<Flux<T>> o) {
        return x ? o.get() : Flux.error(new UnauthorizedRoleException());
    }

    public static <T> Mono<T> validateBooleanMono(final boolean x, final Supplier<Mono<T>> o) {
        return x ? o.get() : Mono.error(new UnauthorizedRoleException());
    }

}
