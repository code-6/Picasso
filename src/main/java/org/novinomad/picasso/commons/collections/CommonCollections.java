package org.novinomad.picasso.commons.collections;

import lombok.experimental.UtilityClass;
import org.novinomad.picasso.commons.exceptions.base.CommonRuntimeException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@UtilityClass
public class CommonCollections {

    private static final String DEFAULT_ELEMENT_DELIMITER = ",";
    private static final String DEFAULT_START_ENCLOSURE = "[";
    private static final String DEFAULT_END_ENCLOSURE = "]";

    public static <T> String toString(Collection<T> collection) {
        return toString(DEFAULT_ELEMENT_DELIMITER, DEFAULT_START_ENCLOSURE, DEFAULT_END_ENCLOSURE, collection);
    }

    /**
     * Prints collection to string with custom delimiter. Example [elem1, elem2, elem3] if ',' provided as delimiter.
     * */
    public static <T> String toString(String delimiter, String startEnclosure, String endEnclosure, Collection<T> collection) {

        StringBuilder sb = new StringBuilder();
        sb.append(startEnclosure);

        int i = 1;
        int size = collection.size();

        for (T e : collection) {
            if(i == size) sb.append(e.toString());
            else sb.append(e.toString()).append(delimiter);
            i++;
        }
        sb.append(endEnclosure);

        return sb.toString();
    }
}
