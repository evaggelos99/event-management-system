package io.github.evaggelos99.ems.event.service.config;

import io.github.evaggelos99.ems.common.api.db.CrudQueriesOperations;
import io.github.evaggelos99.ems.common.api.db.MappingQueriesOperations;
import io.github.evaggelos99.ems.event.api.repo.EventStreamQueriesOperations;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;

@Configuration
@PropertySource("classpath:properties/queries.properties")
@ConfigurationProperties(prefix = "org.com.ems.queries.event")
public class QueriesConfiguration {

    private String save;
    private String edit;
    private String getAll;
    private String getId;
    private String deleteId;
    private String addEventStream;
    private String getEventStream;
    private String saveAttendeeMapping;
    private String deleteAttendeeMapping;
    private String saveSponsorMapping;
    private String deleteSponsorMapping;
    private String getAttendees;
    private String getSponsors;
    private String deleteAttendeeSingularMapping;
    private String deleteSponsorSingularMapping;

    @Bean
    Map<CrudQueriesOperations, String> queriesProperties() {

        return Map.of(CrudQueriesOperations.SAVE, save,
                CrudQueriesOperations.EDIT, edit,
                CrudQueriesOperations.GET_ALL, getAll,
                CrudQueriesOperations.GET_ID, getId,
                CrudQueriesOperations.DELETE_ID, deleteId);
    }

    @Bean
    Map<EventStreamQueriesOperations, String> saveEventStreamQueriesProperties() {

        return Map.of(EventStreamQueriesOperations.ADD, addEventStream,
                EventStreamQueriesOperations.GET_STREAM, getEventStream);
    }

    @Bean
    Map<MappingQueriesOperations, String> attendeeMappingQueriesProperties() {

        return Map.of(MappingQueriesOperations.SAVE_MAPPING, saveAttendeeMapping,
                MappingQueriesOperations.DELETE_MAPPINGS, deleteAttendeeMapping,
                MappingQueriesOperations.DELETE_SINGULAR_MAPPING, deleteAttendeeSingularMapping,
                MappingQueriesOperations.GET_MAPPINGS, getAttendees);
    }

    @Bean
    Map<MappingQueriesOperations, String> sponsorMappingQueriesProperties() {

        return Map.of(MappingQueriesOperations.SAVE_MAPPING, saveSponsorMapping,
                MappingQueriesOperations.DELETE_MAPPINGS, deleteSponsorMapping,
                MappingQueriesOperations.DELETE_SINGULAR_MAPPING, deleteSponsorSingularMapping,
                MappingQueriesOperations.GET_MAPPINGS, getSponsors);
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

    public void setAddEventStream(final String addEventStream) {
        this.addEventStream = addEventStream;
    }

    public void setGetEventStream(final String getEventStream) {
        this.getEventStream = getEventStream;
    }

    public String getAddEventStream() {
        return this.addEventStream;
    }

    public String getGetEventStream() {
        return this.getEventStream;
    }

    public String getSaveAttendeeMapping() {
        return saveAttendeeMapping;
    }

    public void setSaveAttendeeMapping(String saveAttendeeMapping) {
        this.saveAttendeeMapping = saveAttendeeMapping;
    }

    public String getDeleteAttendeeMapping() {
        return deleteAttendeeMapping;
    }

    public void setDeleteAttendeeMapping(String deleteAttendeeMapping) {
        this.deleteAttendeeMapping = deleteAttendeeMapping;
    }

    public String getSaveSponsorMapping() {
        return saveSponsorMapping;
    }

    public void setSaveSponsorMapping(String saveSponsorMapping) {
        this.saveSponsorMapping = saveSponsorMapping;
    }

    public String getDeleteSponsorMapping() {
        return deleteSponsorMapping;
    }

    public void setDeleteSponsorMapping(String deleteSponsorMapping) {
        this.deleteSponsorMapping = deleteSponsorMapping;
    }

    public String getGetAttendees() {
        return getAttendees;
    }

    public void setGetAttendees(String getAttendees) {
        this.getAttendees = getAttendees;
    }

    public String getGetSponsors() {
        return getSponsors;
    }

    public void setGetSponsors(String getSponsors) {
        this.getSponsors = getSponsors;
    }
    public String getDeleteAttendeeSingularMapping() {
        return deleteAttendeeSingularMapping;
    }

    public void setDeleteAttendeeSingularMapping(String deleteAttendeeSingularMapping) {
        this.deleteAttendeeSingularMapping = deleteAttendeeSingularMapping;
    }

    public String getDeleteSponsorSingularMapping() {
        return deleteSponsorSingularMapping;
    }

    public void setDeleteSponsorSingularMapping(String deleteSponsorSingularMapping) {
        this.deleteSponsorSingularMapping = deleteSponsorSingularMapping;
    }
}
