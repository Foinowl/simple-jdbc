package org.example.db.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.db.service.JsonService;

public class JsonServiceImpl implements JsonService {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void writeToJsonObjects(Object obj, Writer writer) {
        try {
            writer.write(mapper.writeValueAsString(obj));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object readObjectJson(InputStream inputStream, Class<?> clazz) {
        String json;
        try (InputStream ip = inputStream) {
            byte[] bytes = new byte[ip.available()];
            ip.read(bytes);
            json = new String(bytes);

            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
