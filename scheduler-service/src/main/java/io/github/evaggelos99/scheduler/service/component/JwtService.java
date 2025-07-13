package io.github.evaggelos99.scheduler.service.component;

import io.github.evaggelos99.scheduler.service.model.TicketInfo;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.lang3.tuple.Triple;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;


@ApplicationScoped
public class JwtService {

    private static final String ISSUER = "scheduler-service";
    private final String secretKey = "AZGSBQFTQEZRMQSBALQUMJCIQOXMRKCLFWJSMUEKFONGCVKYQWPMHYZZJLWVGBTXJABIFOHHZDFOZNLYBBWUMMYCEXCLYUHOUHQBUMVJAHERIMVHADGLEYBLYOQOJGJAOWNVJORKIEQJHYCIWIKUEPIPRSILNNMPWNVYBYWUBBVTCKUYIKZOWGLQDINACLUEYDDBWSARHXAAEXVDLRTTWMOBDGDGSQAEMVRIHGMWMVVZEFUPQOLGBPSRHHMYFYWGKGBUNUIQWGLFQHASXHDKQXXOUBUIMSPJXXAULDGFJSFIMANVOKDQEFBVXIXXQCEEIZPFYVEVEAAIOWLAKLGXCDERSIMKICAPSTNHIRIMWHQHAFLVGCPVFZREKDCRDTJFXCQNGLPXAHGKZCEPKLWJWBGYHVHXXWVKCQKHLZLWEDXYBUDWWJOXEKBCYVJSSKBUCJKBISXTPBUIUUXJPJDXKTUYRCLYEKIOSKOQSYFQTSMGPAKLUWZPGVQTKRKMMMWIALOONYMBTGDWZDJBQUPUDRIYRNHRVIKIXNKFMAHWIPMWAVAYJGHXLNDPSGEYPALULAVSECJHZTUXDLYATAJOKDGH";

    public String generateJwt(TicketInfo ticketInfo) {

        Date expiry = Date.from(Instant.now().plus(Duration.ofDays(2)));

        return Jwts.builder()
                .issuedAt(Date.from(Instant.now()))
                .issuer(ISSUER)
                .claim("email", ticketInfo.attendeeEmail())
                .expiration(expiry)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .claim("purpose", ClaimValuePurpose.FEEDBACK)
                .claim("ticket_id", ticketInfo.ticketId())
                .claim("event_id", ticketInfo.eventId())
                .compact();
    }

    public Triple<UUID, UUID, Boolean> validateJwt(String jwt) {

        final JwtParser parser = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build();

        final Claims claims = parser.parseSignedClaims(jwt).getPayload();

        validateClaims(claims);

        return Triple.of(UUID.fromString(claims.get("ticket_id", String.class)), UUID.fromString(claims.get("event_id", String.class)), parser.isSigned(jwt) && claims.get("purpose", String.class).equals(ClaimValuePurpose.FEEDBACK.name()));
    }

    private void validateClaims(final Claims claims) {
        if (!ISSUER.equals(claims.get("iss", String.class))) {
            throw new RuntimeException("Issuer claim is incorrect");
        }

        final Instant instantOfJwt = Instant.ofEpochMilli(claims.get("exp", Long.class));
        if (instantOfJwt.isBefore(Instant.now().minus(Duration.ofDays(10)))) {
            throw new RuntimeException("JWT is expired");
        }
    }

    private enum ClaimValuePurpose {

        FEEDBACK
    }

}
