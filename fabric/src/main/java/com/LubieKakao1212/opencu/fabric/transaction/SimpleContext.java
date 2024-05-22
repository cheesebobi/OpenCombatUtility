package com.LubieKakao1212.opencu.fabric.transaction;

import com.LubieKakao1212.opencu.common.transaction.IContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;

public class SimpleContext implements IContext, ITransactionAccess {

    private Transaction transaction;

    public SimpleContext() {
        transaction = Transaction.openOuter();
    }

    @Override
    public void commit() {
        transaction.commit();
    }

    @Override
    public void close() {
        transaction.close();
    }

    @Override
    public Transaction getCurrentTransaction() {
        return transaction;
    }
}
