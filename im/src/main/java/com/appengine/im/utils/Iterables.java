package com.appengine.im.utils;

import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * @Author fuyou
 * @Date 2018/12/19 16:37
 */
public class Iterables {
    public static <E> void forEach(
            Iterable<? extends E> elements, BiConsumer<Integer, ? super E> action) {
        Objects.requireNonNull(elements);
        Objects.requireNonNull(action);

        int index = 0;
        for (E element : elements) {
            action.accept(index++, element);
        }
    }
}
