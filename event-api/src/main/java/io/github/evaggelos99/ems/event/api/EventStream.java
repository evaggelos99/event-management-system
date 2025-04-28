package io.github.evaggelos99.ems.event.api;

import io.github.evaggelos99.ems.common.api.domainobjects.AbstractDomainObject;
import io.r2dbc.postgresql.codec.Json;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

public class EventStream extends AbstractDomainObject {

    private final String streamType;
    private final Instant inceptionTime;
    private final String messageType;
    private final String content;
    private final String language;
    private final Boolean isImportant;
    private final String metadata;

    public EventStream(final UUID uuid, final OffsetDateTime createdAt, final OffsetDateTime lastUpdated, final String streamType, final Instant inceptionTime, final String messageType, final String content, final String language, final Boolean isImportant, final Json metadata) {

        super(uuid, createdAt, lastUpdated);

        this.streamType = streamType;
        this.inceptionTime = inceptionTime;
        this.messageType = messageType;
        this.content = content;
        this.language = language;
        this.isImportant = isImportant;
        this.metadata = metadata.asString();
    }


    public String getStreamType() {
        return streamType;
    }

    public Instant getInceptionTime() {
        return inceptionTime;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getContent() {
        return content;
    }

    public String getLanguage() {
        return language;
    }

    public Boolean getImportant() {
        return isImportant;
    }

    public String getMetadata() {
        return metadata;
    }
}
