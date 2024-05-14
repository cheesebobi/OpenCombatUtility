package com.LubieKakao1212.opencu.fabric.block.entity;

import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.fabric.inventory.SlottedInventory;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;


public class BlockEntityModularFrameImpl extends BlockEntityModularFrame {

    private static final int slotCount = 10;
    private static final int ammoSlotCount = 9;
    private static final int ammoSlotsStart = 1;
    private static final int ammoSlotsEnd = 10;

    private static final int dispenserSotCount = 1;
    private static final int dispenserSlotsStart = 0;
    private static final int dispenserSlotsEnd = 1;

    private final InventoryStorage inventoryStorage;

    private final Storage<ItemVariant> dispenserStorage;
    private final Storage<ItemVariant> ammoStorage;

    private final SlottedInventory inventory;

    public BlockEntityModularFrameImpl(BlockPos pos, BlockState blockState) {
        super(pos, blockState);
        inventory = new SlottedInventory(slotCount) {
            @Override
            public void markDirty() {
                BlockEntityModularFrameImpl.this.markDirty();
            }
        };
        inventoryStorage = InventoryStorage.of(inventory, null);
        var slots = inventoryStorage.getSlots();
        dispenserStorage = new CombinedStorage<>(slots.subList(dispenserSlotsStart, dispenserSlotsEnd));
        ammoStorage = new CombinedStorage<>(slots.subList(ammoSlotsStart, ammoSlotsEnd));
    }

    @Override
    protected ActionContext getNewContext() {
        return new TransactionActionContext();
    }

    @Override
    protected ItemStack useAmmo(ActionContext ctx) {
        var transactionCtx = (TransactionActionContext) ctx;
        for(var view : ammoStorage.nonEmptyViews()) {
            var res = view.getResource();
            if(view.extract(res, 1, transactionCtx.transaction) != 1) {
                return ItemStack.EMPTY;
            }
            return res.toStack();
        }
        return ItemStack.EMPTY;
    }

    @Override
    protected ItemStack handleLeftover(ActionContext ctx, ItemStack leftover) {
        if(!leftover.isEmpty()){
            var transactionCtx = (TransactionActionContext) ctx;
            try(var inner = Transaction.openNested(transactionCtx.transaction)) {
                if(ammoStorage.insert(ItemVariant.of(leftover), leftover.getCount(), inner) == leftover.getCount()) {
                    inner.commit();
                    return ItemStack.EMPTY;
                }
            }
        }
        return leftover;
    }

    /**
     * Creates a slot for gui
     *
     * @param idx slot index 0 => device; 1-9 => ammo
     * @param x
     * @param y
     */
    @Override
    public Slot createSlot(int idx, int x, int y) {
        if(idx == 0) {
            return new Slot(inventory, idx, x, y) {
                @Override
                public void markDirty() {
                    super.markDirty();
                    updateDispenser();
                }
            };
        }
        return new Slot(inventory, idx, x, y);
    }

    public void scatterInventory() {
        ItemScatterer.spawn(world, pos, inventory);
    }

    @Override
    protected ItemStack getCurrentDeviceItemServer() {
        return inventory.getStack(0);
    }

    @Override
    public void writeNbt(@NotNull NbtCompound compound) {
        super.writeNbt(compound);

        compound.put("inventory", inventory.writeNbt());
    }

    @Override
    public void readNbt(@NotNull NbtCompound compound) {
        super.readNbt(compound);

        var invTag = compound.getList("inventory", NbtElement.COMPOUND_TYPE);
        if(!invTag.isEmpty()) {
            inventory.readNbt(invTag);
        }
    }


    private class TransactionActionContext implements ActionContext {

        public final Transaction transaction;

        public TransactionActionContext() {
            transaction = Transaction.openOuter();
        }

        @Override
        public void close() {
            transaction.close();
        }

        @Override
        public void commit() {
            transaction.commit();
        }
    }
}
