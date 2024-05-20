package com.LubieKakao1212.opencu.common.transaction;

public interface IScopedContext extends AutoCloseable{


    void push();
    void pop();

    void commit();

}
