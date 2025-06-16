package io.github.evaggelos99.ems.common.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;

public interface IGenericController {

    /**
     * Ping endpoint for the REST Controllers
     *
     * @return {@link Boolean#TRUE} if the service is reachable other wise
     * {@link Boolean#FALSE} if the service is not reachable
     */
    @Operation(summary = "GET operation checks if the service is healthy", description = "Ping endpoint")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful operation")})
    @GetMapping("/ping")
    Mono<ResponseEntity<Void>> ping();
}
