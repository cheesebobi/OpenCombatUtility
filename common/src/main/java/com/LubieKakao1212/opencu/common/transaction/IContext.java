package com.LubieKakao1212.opencu.common.transaction;

public interface IContext extends AutoCloseable {

    void commit();

    //Disable exception
    @Override
    void close();

}
