package io.github.evaggelos99.ems.user.service.config;

import io.github.evaggelos99.ems.common.api.db.CrudQueriesOperations;
import io.github.evaggelos99.ems.common.api.db.ReportQueriesOperations;
import io.github.evaggelos99.ems.user.api.UserQueriesOperations;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;

@Configuration
@PropertySource("classpath:properties/queries.properties")
@ConfigurationProperties(prefix = "io.github.evaggelos99.ems.user-service.db")
public class QueriesConfiguration {

    private String save;
    private String edit;
    private String getAll;
    private String getId;
    private String deleteId;
    private String attendeesCameToAllEvents;
    private String entityUuid;
    private String addAttendeeEntityUuid;
    private String addSponsorEntityUuid;
    private String addOrganizerEntityUuid;

    @Bean
    Map<CrudQueriesOperations, String> queriesProperties() {

        return Map.of(CrudQueriesOperations.SAVE, save,
                CrudQueriesOperations.EDIT, edit,
                CrudQueriesOperations.GET_ALL, getAll,
                CrudQueriesOperations.GET_ID, getId,
                CrudQueriesOperations.DELETE_ID, deleteId);
    }

    @Bean
    Map<ReportQueriesOperations, String> reportQueriesProperties() {

        return Map.of(ReportQueriesOperations.ATTENDEES_CAME_TO_ALL_EVENTS, attendeesCameToAllEvents);
    }

    @Bean
    Map<UserQueriesOperations, String> userQueriesProperties() {

        return Map.of(UserQueriesOperations.ADD_ENTITY_SPONSOR, addSponsorEntityUuid,
                UserQueriesOperations.ADD_ENTITY_ATTENDEE, addAttendeeEntityUuid,
                UserQueriesOperations.ADD_ENTITY_ORGANIZER, addOrganizerEntityUuid,
                UserQueriesOperations.GET_ENTITY_UUID, entityUuid);
    }

    public String getSave() {
        return save;
    }

    public void setSave(String save) {
        this.save = save;
    }

    public String getEdit() {
        return edit;
    }

    public void setEdit(String edit) {
        this.edit = edit;
    }

    public String getGetAll() {
        return getAll;
    }

    public void setGetAll(String getAll) {
        this.getAll = getAll;
    }

    public String getGetId() {
        return getId;
    }

    public void setGetId(String getId) {
        this.getId = getId;
    }

    public String getDeleteId() {
        return deleteId;
    }

    public void setDeleteId(String deleteId) {
        this.deleteId = deleteId;
    }

    public void setAttendeesCameToAllEvents(String attendeesCameToAllEvents) {
        this.attendeesCameToAllEvents = attendeesCameToAllEvents;
    }

    public String getAttendeesCameToAllEvents() {
        return attendeesCameToAllEvents;
    }

    public String getEntityUuid() {
        return entityUuid;
    }

    public void setEntityUuid(final String entityUuid) {
        this.entityUuid = entityUuid;
    }

    public String getAddAttendeeEntityUuid() {
        return addAttendeeEntityUuid;
    }

    public void setAddAttendeeEntityUuid(final String addAttendeeEntityUuid) {
        this.addAttendeeEntityUuid = addAttendeeEntityUuid;
    }

    public String getAddSponsorEntityUuid() {
        return addSponsorEntityUuid;
    }

    public void setAddSponsorEntityUuid(final String addSponsorEntityUuid) {
        this.addSponsorEntityUuid = addSponsorEntityUuid;
    }

    public String getAddOrganizerEntityUuid() {
        return addOrganizerEntityUuid;
    }

    public void setAddOrganizerEntityUuid(final String addOrganizerEntityUuid) {
        this.addOrganizerEntityUuid = addOrganizerEntityUuid;
    }
}
