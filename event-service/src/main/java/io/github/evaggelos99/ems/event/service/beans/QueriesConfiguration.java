package io.github.evaggelos99.ems.event.service.beans;

import io.github.evaggelos99.ems.common.api.db.CrudQueriesOperations;
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


}
