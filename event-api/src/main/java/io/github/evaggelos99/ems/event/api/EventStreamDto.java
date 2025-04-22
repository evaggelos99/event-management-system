package io.github.evaggelos99.ems.event.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record EventStreamDto(
        @NotBlank @Schema(example = "d68a65ca-1f66-409c-9083-795260c16fe4", description = "The UUID of the event") UUID uuid,
        @NotNull @Schema(description = "Timestamp created at") Instant creartedAt,
        @NotNull @Schema(description = "Timestamp updated at") Instant updatedAt,
        @NotBlank @Schema(examples = {"text", "video"}, description = "What kind of type is the stream") String streamType,
        @NotBlank @Schema(description = "Inception time of the message") Instant time,
        @NotBlank @Schema(examples = {"advertisement", "announcement"}, description = "What is the type of the message") String messageType,
        @NotBlank @Schema(example = "Lorem ipsum dolor sit amet", description = "The content of the stream") String content,
        @NotBlank @Schema(examples = {"EN", "GR", "RU"}, description = "The language the message is in") String language,
        @NotBlank @Schema(examples = {"false", "true"}, description = "Whenether the message is important") Boolean isImportant,
        @Schema(examples = "{\"emoji\": \"\uD83C\uDF89\",  \"audience_group\": \"VIP\"}", description = "Metadata of the message") String metadata
) {

    public static Builder builder() {

        return new Builder();
    }

    public static final class Builder {

        private UUID uuid;
        private String streamType;
        private Instant time;
        private String messageType;
        private String content;
        private String language;
        private Boolean isImportant;
        private String metadata;
        private Instant createdAt;
        private Instant updatedAt;

        private Builder() {

        }

        public EventStreamDto build() {
            return new EventStreamDto(uuid,
                    createdAt,
                    updatedAt,
                    streamType,
                    time,
                    messageType,
                    content,
                    language,
                    isImportant,
                    metadata);
        }

        public Builder uuid(final UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder streamType(final String streamType) {
            this.streamType = streamType;
            return this;
        }

        public Builder time(final Instant time) {
            this.time = time;
            return this;
        }

        public Builder messageType(final String messageType) {
            this.messageType = messageType;
            return this;
        }

        public Builder content(final String content) {
            this.content = content;
            return this;
        }

        public Builder language(final String language) {
            this.language = language;
            return this;
        }

        public Builder important(final Boolean important) {
            isImportant = important;
            return this;
        }

        public Builder metadata(final String metadata) {
            this.metadata = metadata;
            return this;
        }

        public Builder updatedAt(final Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder createdAt(final Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }
    }

}
