package io.github.evaggelos99.ems.attendee.service.config;

import io.github.evaggelos99.ems.common.api.db.CrudQueriesOperations;
import io.github.evaggelos99.ems.common.api.db.MappingQueriesOperations;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;

@Configuration
@PropertySource("classpath:properties/queries.properties")
@ConfigurationProperties(prefix = "org.com.ems.queries.attendee")
public class QueriesConfiguration {

    private String save;
    private String edit;
    private String getAll;
    private String getId;
    private String deleteId;
    private String saveTicketMappings;
    private String deleteTicketMappings;
    private String getTickets;
    private String deleteTicketSingularMapping;

    @Bean
    Map<CrudQueriesOperations, String> queriesProperties() {

        return Map.of(CrudQueriesOperations.SAVE, save,
                CrudQueriesOperations.EDIT, edit,
                CrudQueriesOperations.GET_ALL, getAll,
                CrudQueriesOperations.GET_ID, getId,
                CrudQueriesOperations.DELETE_ID, deleteId);
    }

    @Bean
    Map<MappingQueriesOperations, String> mappingQueriesProperties() {

        return Map.of(MappingQueriesOperations.SAVE_MAPPING, saveTicketMappings,
                MappingQueriesOperations.DELETE_MAPPINGS, deleteTicketMappings,
                MappingQueriesOperations.DELETE_SINGULAR_MAPPING, deleteTicketSingularMapping,
                MappingQueriesOperations.GET_MAPPINGS, getTickets);
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

    public void setSaveTicketMappings(final String saveTicketMapping) {
        this.saveTicketMappings = saveTicketMapping;
    }

    public String getSaveTicketMappings() {
        return saveTicketMappings;
    }

    public void setDeleteTicketMappings(final String deleteTicketMappings) {
        this.deleteTicketMappings = deleteTicketMappings;
    }

    public String getDeleteTicketMappings() {
        return deleteTicketMappings;
    }

    public void setGetTickets(final String getTickets) {
        this.getTickets = getTickets;
    }

    public String getGetTickets() {
        return getTickets;
    }

    public void setDeleteTicketSingularMapping(final String deleteTicketSingularMapping) {
        this.deleteTicketSingularMapping = deleteTicketSingularMapping;
    }

    public String getDeleteTicketSingularMapping() {
        return deleteTicketSingularMapping;
    }
}
