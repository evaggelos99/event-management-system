package io.github.evaggelos99.ems.kafka.lib.object.deserializer;

import org.apache.kafka.common.KafkaException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class ObjectDeserializer {

    public Object convertBytesToObject(byte[] bytes) {

        try (InputStream is = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(is)) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException ioe) {
            throw new KafkaException(ioe);
        }
    }
}
