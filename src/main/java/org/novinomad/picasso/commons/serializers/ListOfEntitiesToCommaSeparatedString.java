package org.novinomad.picasso.commons.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.novinomad.picasso.erm.entities.base.IdAware;

import java.io.IOException;
import java.util.List;

public class ListOfEntitiesToCommaSeparatedString extends JsonSerializer<List<IdAware>> {

    private static final ListToCommaSeparatedString LIST_TO_COMMA_SEPARATED_STRING = new ListToCommaSeparatedString();

    @Override
    public void serialize(List<IdAware> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        List<Object> longs = value.stream().map(IdAware::getId).toList();
        LIST_TO_COMMA_SEPARATED_STRING.serialize(longs, gen, serializers);
    }
}
