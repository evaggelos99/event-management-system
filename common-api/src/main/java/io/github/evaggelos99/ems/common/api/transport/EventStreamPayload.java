package io.github.evaggelos99.ems.common.api.transport;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * {
 *   "event_id": "event_abc123",
 *   "stream_type": "text",
 *   "timestamp": "2025-03-30T15:10:45Z",
 *   "message_type": "announcement",  // or "chat", "system", "qa", "poll"
 *   "content": "Welcome to the keynote session!",
 *   "language": "en",
 *   "is_important": true,
 *   "metadata": {
 *     "emoji": "🎉",
 *     "audience_group": "VIP"
 *   }
 * }
 */
public class EventStreamPayload implements Serializable {

    private UUID uuid;
    private String streamType;
    private Instant time;
    private String messageType;
    private String content;
    private String language;
    private Boolean isImportant;
    private String metadata;

    /**
     * C-or used for serializing the object
     */
    public EventStreamPayload() {

    }

    /**
     * C-or. used for cosntructing the object for transport
     * @param uuid
     * @param streamType
     * @param time
     * @param messageType
     * @param content
     * @param language
     * @param isImportant
     * @param metadata
     */
    public EventStreamPayload(UUID uuid, String streamType, Instant time, String messageType, String content, String language, Boolean isImportant, String metadata) {

        this.uuid = uuid;
        this.streamType = streamType;
        this.time = time;
        this.messageType = messageType;
        this.content = content;
        this.language = language;
        this.isImportant = isImportant;
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getStreamType() {
        return streamType;
    }

    public Instant getTime() {
        return time;
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
