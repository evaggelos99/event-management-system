
CREATE TABLE IF NOT EXISTS ems_event.event_stream(
  uuid UUID NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  last_updated TIMESTAMP WITH TIME ZONE NOT NULL,
  stream_type TEXT NOT NULL,
  inception_time TIMESTAMP WITH TIME ZONE NOT NULL,
  message_type TEXT NOT NULL,
  content TEXT NOT NULL,
  language TEXT NOT NULL,
  is_important BOOLEAN NOT NULL,
  metadata JSON
);

CREATE INDEX index_uuid_event_stream ON ems_event.event_stream (uuid);