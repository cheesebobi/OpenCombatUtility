package com.LubieKakao1212.opencu.fabric.transaction;

import com.LubieKakao1212.opencu.common.transaction.IAmmoContext;
import com.LubieKakao1212.opencu.common.transaction.IContext;
import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AmmoContext implements IAmmoContext {

    private Storage<ItemVariant> sourceStorage;

    public AmmoContext(Storage<ItemVariant> sourceStorage) {
        this.sourceStorage = sourceStorage;
    }

    @Override
    public ItemStack useAmmoFirst(IContext ctx) {
        for(var view : sourceStorage.nonEmptyViews()) {
            var res = view.getResource();
            if(view.extract(res, 1, ITransactionAccess.transactionFromContext(ctx)) != 1) {
                return ItemStack.EMPTY;
            }
            return res.toStack();
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack useAmmoRandom(Random random, IContext ctx) {
        var list = Lists.newArrayList(sourceStorage.nonEmptyViews());
        if(list.size() > 0) {
            var i = random.nextInt(list.size());
            var view = list.get(i);
            var res = view.getResource();
            if(view.extract(res, 1, ITransactionAccess.transactionFromContext(ctx)) != 1) {
                return ItemStack.EMPTY;
            }
            return res.toStack();
        }

        return ItemStack.EMPTY;
    }

    /**
     * Does not take, {@link IContext} since it does not modify the state
     *
     * @return
     */
    @Override
    public List<ItemStack> availableAmmo() {
        var listOut = new ArrayList<ItemStack>();
        for(var view : sourceStorage.nonEmptyViews()) {
            listOut.add(view.getResource().toStack((int)view.getAmount()));
        }
        return listOut;
    }

}
