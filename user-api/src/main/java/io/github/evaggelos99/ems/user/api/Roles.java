package io.github.evaggelos99.ems.user.api;

/**
 * Enum that contains all the authentication roles
 */
public enum Roles {

    ROLE_CREATE_TICKET,
    ROLE_READ_TICKET,
    ROLE_UPDATE_TICKET,
    ROLE_DELETE_TICKET,

    ROLE_CREATE_ATTENDEE,
    ROLE_READ_ATTENDEE,
    ROLE_UPDATE_ATTENDEE,
    ROLE_DELETE_ATTENDEE,

    ROLE_CREATE_EVENT,
    ROLE_READ_EVENT,
    ROLE_UPDATE_EVENT,
    ROLE_DELETE_EVENT,

    ROLE_CREATE_SPONSOR,
    ROLE_READ_SPONSOR,
    ROLE_UPDATE_SPONSOR,
    ROLE_DELETE_SPONSOR,

    ROLE_CREATE_ORGANIZER,
    ROLE_UPDATE_ORGANIZER,
    ROLE_READ_ORGANIZER,
    ROLE_DELETE_ORGANIZER,

    /**
     * Used to onboard users into the system
     */
    ROLE_ADMIN
}
