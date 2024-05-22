package com.LubieKakao1212.opencu.fabric.transaction;

import com.LubieKakao1212.opencu.common.transaction.IScopedContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.util.ItemScatterer;

import java.util.Stack;

public class ScopedContext implements IScopedContext, ITransactionAccess {

    private final Stack<Transaction> transactions = new Stack<>();

    public ScopedContext() {
        transactions.push(Transaction.openOuter());
    }

    @Override
    public void push() {
        var t = transactions.peek().openNested();
        transactions.push(t);
    }

    @Override
    public void pop() {
        if(transactions.size() <= 1) {
            throw new IllegalStateException("push < pop");
        }
        transactions.pop().close();
    }

    @Override
    public void close() {
        if(transactions.size() != 1) {
            throw new IllegalStateException("push > pop");
        }

        transactions.peek().close();
        transactions.clear();/*
        ItemScatterer.spawn(world, pos, scatterInventory);*/
    }

    @Override
    public void commit() {
        transactions.peek().commit();
    }

    @Override
    public Transaction getCurrentTransaction() {
        return transactions.peek();
    }
}
