package com.LubieKakao1212.opencu.fabric.transaction;

import com.LubieKakao1212.opencu.common.transaction.IContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;

public interface ITransactionAccess {

    Transaction getCurrentTransaction();

    static Transaction transactionFromContext(IContext ctx) {
        return ((ITransactionAccess)ctx).getCurrentTransaction();
    }

}
