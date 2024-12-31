package io.github.evaggelos99.ems.kafka.lib.serializer;

import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ByteArraySerializer implements Serializer<Serializable> {

    @Override
    public byte[] serialize(final String topic, final Serializable data) {

        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
             ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {

            objectStream.writeObject(data);
            objectStream.flush();
            return byteStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] serialize(final String topic, final Headers headers, final Serializable data) {

        return serialize(topic, data);
    }
}
