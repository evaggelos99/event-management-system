package org.com.ems.event.service.db;

import java.util.Properties;

import org.com.ems.common.api.db.CrudQueriesOperations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:properties/queries.properties")
public class Queries {

    @Bean
    Properties queriesProperties(@Value("${org.com.ems.queries.event.save}") final String save,
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

}
