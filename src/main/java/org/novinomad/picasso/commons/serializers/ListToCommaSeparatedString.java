package org.novinomad.picasso.commons.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.List;

public class ListToCommaSeparatedString extends JsonSerializer<List<Long>> {
    @Override
    public void serialize(List<Long> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < value.size(); i++) {
            if(i == value.size() - 1)
                sb.append(value.get(i));
            else
                sb.append(value.get(i)).append(",");
        }

        gen.writeString(sb.toString());
    }
}
