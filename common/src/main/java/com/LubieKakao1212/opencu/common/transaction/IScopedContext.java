package com.LubieKakao1212.opencu.common.transaction;

public interface IScopedContext extends IContext {

    void push();
    void pop();

}
