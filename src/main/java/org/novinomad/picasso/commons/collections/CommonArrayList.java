package org.novinomad.picasso.commons.collections;

import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class CommonArrayList<E> extends ArrayList<E> {
    private final boolean nonNullableElements;
    private final boolean uniqueElements;

    public CommonArrayList(int initialCapacity) {
        super(initialCapacity);
        nonNullableElements = true;
        uniqueElements = true;
    }

    public CommonArrayList() {
        nonNullableElements = true;
        uniqueElements = true;
    }

    public CommonArrayList(Collection<? extends E> c) {
        super(c);
        nonNullableElements = true;
        uniqueElements = true;
    }

    @Override
    public boolean add(E e) {
        if((nonNullableElements && e == null) || (uniqueElements && contains(e))) return false;

        return super.add(e);
    }

    @Override
    public void add(int index, E e) {
        if((nonNullableElements && e == null) || (uniqueElements && contains(e))) return;
        super.add(index, e);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        Stream<? extends E> stream = c.stream();

        if(uniqueElements) {
            stream = stream.distinct();
        }
        if(nonNullableElements) {
            stream = stream.filter(Objects::nonNull);
        }
        return super.addAll(stream.toList());
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        Stream<? extends E> stream = c.stream();

        if(uniqueElements) {
            stream = stream.distinct();
        }
        if(nonNullableElements) {
            stream = stream.filter(Objects::nonNull);
        }
        return super.addAll(index, stream.toList());
    }
}
