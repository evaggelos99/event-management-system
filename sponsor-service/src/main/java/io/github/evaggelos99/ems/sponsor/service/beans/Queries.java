package io.github.evaggelos99.ems.sponsor.service.beans;

import io.github.evaggelos99.ems.common.api.db.CrudQueriesOperations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Properties;

@Configuration
@PropertySource("classpath:properties/queries.properties")
public class Queries {

    @Bean
    Properties queriesProperties(@Value("${org.com.ems.queries.sponsor.save}") final String save,
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

}
