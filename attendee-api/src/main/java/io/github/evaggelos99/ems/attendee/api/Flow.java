package io.github.evaggelos99.ems.attendee.api;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class Flow {

    public record EmailDto(Pair<String,String> from,
                           Pair<String, String> to,
                           String subject,
                           String body,
                           List<String> cc) {

        /**
         * {@code lhs} corresponds to: name
         * {@code rhs} corresponds to: email
         */
        @Override
        public Pair<String, String> to() {
            return to;
        }


        /**
         * {@code lhs} corresponds to: name
         * {@code rhs} corresponds to: email domain
         */
        @Override
        public Pair<String, String> from() {
            return from;
        }
    }
}
