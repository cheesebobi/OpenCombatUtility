package com.LubieKakao1212.opencu.common.util;

import java.io.Closeable;
import java.util.function.Consumer;

public class EndBoolean implements AutoCloseable {

    public boolean result;
    private final Consumer<Boolean> action;

    public EndBoolean(Consumer<Boolean> action) {
        this.action = action;
    }

    @Override
    public void close() {
        action.accept(result);
    }

}
