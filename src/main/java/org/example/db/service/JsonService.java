package org.example.db.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface JsonService {
    void writeToJsonObjects(Object obj, Writer writer);
    Object readObjectJson(InputStream inputStream, Class<?> clazz);
}
