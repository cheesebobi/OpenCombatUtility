package com.LubieKakao1212.opencu.common.util;

import java.util.function.Supplier;

public class Lazy<T> {

    private T value = null;

    private final Supplier<T> supplier;

    public Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public T getValue() {
        if(value == null) {
            value = supplier.get();
        }
        return value;
    }


}
