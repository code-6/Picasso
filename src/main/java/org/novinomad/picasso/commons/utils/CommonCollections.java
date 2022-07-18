package org.novinomad.picasso.commons.utils;

import lombok.experimental.UtilityClass;

import java.util.Collection;

@UtilityClass
public class CommonCollections {

    /**
     * Prints collection to string with custom delimiter. Example [elem1, elem2, elem3] if ',' provided as delimiter.
     * */
    public static <T> String toString(String delimiter, Collection<T> collection) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        int i = 1;
        int size = collection.size();

        for (T e : collection) {
            if(i == size) sb.append(e.toString());
            else sb.append(e.toString()).append(delimiter);
            i++;
        }
        sb.append("]");

        return sb.toString();
    }
}
