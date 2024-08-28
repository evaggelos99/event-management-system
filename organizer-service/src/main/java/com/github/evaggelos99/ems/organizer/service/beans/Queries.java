package com.github.evaggelos99.ems.organizer.service.beans;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.github.evaggelos99.ems.common.api.db.CrudQueriesOperations;

@Configuration
@PropertySource("classpath:properties/queries.properties")
public class Queries {

	@Bean
	Properties queriesProperties(@Value("${org.com.ems.queries.organizer.save}") final String save,
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

}
