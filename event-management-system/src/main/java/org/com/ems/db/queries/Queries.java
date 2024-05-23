package org.com.ems.db.queries;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:properties/queries.properties")
public class Queries {

    public static enum CrudQueriesOperations {

	SAVE, EDIT, GET_ALL, GET_ID, DELETE_ID

    }

    @Bean
    Properties attendeeQueriesProperties(@Value("${org.com.ems.queries.attendee.save}") final String save,
					 @Value("${org.com.ems.queries.attendee.edit}") final String edit,
					 @Value("${org.com.ems.queries.attendee.get-all}") final String getAll,
					 @Value("${org.com.ems.queries.attendee.get-id}") final String getId,
					 @Value("${org.com.ems.queries.attendee.delete-id}") final String deleteId) {

	final Properties props = new Properties();

	props.put(CrudQueriesOperations.SAVE.name(), save);
	props.put(CrudQueriesOperations.EDIT.name(), edit);
	props.put(CrudQueriesOperations.GET_ALL.name(), getAll);
	props.put(CrudQueriesOperations.GET_ID.name(), getId);
	props.put(CrudQueriesOperations.DELETE_ID.name(), deleteId);

	return props;

    }

    @Bean
    Properties eventQueriesProperties(@Value("${org.com.ems.queries.event.save}") final String save,
				      @Value("${org.com.ems.queries.event.edit}") final String edit,
				      @Value("${org.com.ems.queries.event.get-all}") final String getAll,
				      @Value("${org.com.ems.queries.event.get-id}") final String getId,
				      @Value("${org.com.ems.queries.event.delete-id}") final String deleteId) {

	final Properties props = new Properties();

	props.put(CrudQueriesOperations.SAVE.name(), save);
	props.put(CrudQueriesOperations.EDIT.name(), edit);
	props.put(CrudQueriesOperations.GET_ALL.name(), getAll);
	props.put(CrudQueriesOperations.GET_ID.name(), getId);
	props.put(CrudQueriesOperations.DELETE_ID.name(), deleteId);

	return props;

    }

    @Bean
    Properties organizerQueriesProperties(@Value("${org.com.ems.queries.organizer.save}") final String save,
					  @Value("${org.com.ems.queries.organizer.edit}") final String edit,
					  @Value("${org.com.ems.queries.organizer.get-all}") final String getAll,
					  @Value("${org.com.ems.queries.organizer.get-id}") final String getId,
					  @Value("${org.com.ems.queries.organizer.delete-id}") final String deleteId) {

	final Properties props = new Properties();

	props.put(CrudQueriesOperations.SAVE.name(), save);
	props.put(CrudQueriesOperations.EDIT.name(), edit);
	props.put(CrudQueriesOperations.GET_ALL.name(), getAll);
	props.put(CrudQueriesOperations.GET_ID.name(), getId);
	props.put(CrudQueriesOperations.DELETE_ID.name(), deleteId);

	return props;

    }

    @Bean
    Properties sponsorQueriesProperties(@Value("${org.com.ems.queries.sponsor.save}") final String save,
					@Value("${org.com.ems.queries.sponsor.edit}") final String edit,
					@Value("${org.com.ems.queries.sponsor.get-all}") final String getAll,
					@Value("${org.com.ems.queries.sponsor.get-id}") final String getId,
					@Value("${org.com.ems.queries.sponsor.delete-id}") final String deleteId) {

	final Properties props = new Properties();

	props.put(CrudQueriesOperations.SAVE.name(), save);
	props.put(CrudQueriesOperations.EDIT.name(), edit);
	props.put(CrudQueriesOperations.GET_ALL.name(), getAll);
	props.put(CrudQueriesOperations.GET_ID.name(), getId);
	props.put(CrudQueriesOperations.DELETE_ID.name(), deleteId);

	return props;

    }

    @Bean
    Properties ticketQueriesProperties(@Value("${org.com.ems.queries.ticket.save}") final String save,
				       @Value("${org.com.ems.queries.ticket.edit}") final String edit,
				       @Value("${org.com.ems.queries.ticket.get-all}") final String getAll,
				       @Value("${org.com.ems.queries.ticket.get-id}") final String getId,
				       @Value("${org.com.ems.queries.ticket.delete-id}") final String deleteId) {

	final Properties props = new Properties();

	props.put(CrudQueriesOperations.SAVE.name(), save);
	props.put(CrudQueriesOperations.EDIT.name(), edit);
	props.put(CrudQueriesOperations.GET_ALL.name(), getAll);
	props.put(CrudQueriesOperations.GET_ID.name(), getId);
	props.put(CrudQueriesOperations.DELETE_ID.name(), deleteId);

	return props;

    }

}
