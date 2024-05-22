package com.LubieKakao1212.opencu.fabric.transaction;

import com.LubieKakao1212.opencu.common.transaction.IContext;
import com.LubieKakao1212.opencu.common.transaction.ILeftoverItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SimpleLeftoverContext implements ILeftoverItemContext {

    private final SimpleInventory scatterInventory;
    private final InventoryStorage scatterStorage;

    private final Storage<ItemVariant> leftoverTargetStorage;

    private final World scatterTragetWorld;
    private final BlockPos scatterTargetPos;

    public SimpleLeftoverContext(Storage<ItemVariant> targetStorage, World scatterWorld, BlockPos scatterPos) {
        //TODO Expose slot count to config
        scatterInventory = new SimpleInventory(1024);

        scatterStorage = InventoryStorage.of(scatterInventory, null);

        this.leftoverTargetStorage = targetStorage;
        this.scatterTragetWorld = scatterWorld;
        this.scatterTargetPos = scatterPos;
    }

    @Override
    public void handleLeftover(ItemStack stack, IContext ctx) {
        var res = ItemVariant.of(stack);
        var transaction = ITransactionAccess.transactionFromContext(ctx);
        var inserted = leftoverTargetStorage.insert(res, stack.getCount(), transaction);
        var leftoverAmount = stack.getCount() - inserted;
        if(leftoverAmount > 0) {
            if(scatterStorage.insert(res, leftoverAmount, transaction) < leftoverAmount) {
                throw new IllegalStateException("Cannot insert to scatter inventory");
            }
        }
    }

    //Disable exception
    @Override
    public void close() {
        ItemScatterer.spawn(scatterTragetWorld, scatterTargetPos, scatterInventory);
    }

}
