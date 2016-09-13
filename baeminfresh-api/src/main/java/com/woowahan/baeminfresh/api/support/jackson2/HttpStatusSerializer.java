package com.woowahan.baeminfresh.api.support.jackson2;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.http.HttpStatus;

import java.io.IOException;

/**
 * @author ykpark@woowahan.com
 */
public class HttpStatusSerializer extends JsonSerializer<HttpStatus> {

    @Override
    public void serialize(HttpStatus status, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {
        generator.writeNumber(status.value());
    }

}