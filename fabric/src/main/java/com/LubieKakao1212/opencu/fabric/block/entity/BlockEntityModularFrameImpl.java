package com.LubieKakao1212.opencu.fabric.block.entity;

import com.LubieKakao1212.opencu.OpenCUConfigCommon;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.fabric.inventory.SlottedInventory;
import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.InfiniteEnergyStorage;
import team.reborn.energy.api.base.LimitingEnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.Stack;


public class BlockEntityModularFrameImpl extends BlockEntityModularFrame {

    private static final int slotCount = 10;
    private static final int ammoSlotCount = 9;
    private static final int ammoSlotsStart = 1;
    private static final int ammoSlotsEnd = 10;

    private static final int dispenserSotCount = 1;
    private static final int dispenserSlotsStart = 0;
    private static final int dispenserSlotsEnd = 1;

    private final InventoryStorage inventoryStorage;

    public final Storage<ItemVariant> dispenserStorage;
    public final Storage<ItemVariant> ammoStorage;

    private final EnergyStorage energyStorage;
    public final EnergyStorage exposedEnegyStorage;

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

        //region energy
        var useEnergy = OpenCUConfigCommon.general().energyEnabled();

        if(useEnergy) {
            var capacity = OpenCUConfigCommon.capacitor().energyCapacity();
            energyStorage = new SimpleEnergyStorage(capacity, capacity, capacity);
            exposedEnegyStorage = new LimitingEnergyStorage(energyStorage, capacity, 0);
        } else {
            energyStorage = new InfiniteEnergyStorage();
            exposedEnegyStorage = null;
        }
        //endregion
    }

    @Override
    protected IModularFrameContext getNewContext() {
        return new TransactionModularFrameContext();
    }

    /*@Override
    protected ItemStack useAmmo(ActionContext ctx) {

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
    }*/

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

    private class TransactionModularFrameContext implements IModularFrameContext {

        private final Stack<Transaction> transactions = new Stack<>();

        private final SimpleInventory scatterInventory;
        private final InventoryStorage scatterStorage;

        public TransactionModularFrameContext() {
            transactions.push(Transaction.openOuter());

            //TODO Expose slot count to config
            scatterInventory = new SimpleInventory(1024);

            scatterStorage = InventoryStorage.of(scatterInventory, null);
        }

        @Override
        public long useEnergy(long amount) {
            return energyStorage.extract(amount, transactions.peek());
        }

        @Override
        public ItemStack useAmmoFirst() {
            for(var view : ammoStorage.nonEmptyViews()) {
                var res = view.getResource();
                if(view.extract(res, 1, transactions.peek()) != 1) {
                    return ItemStack.EMPTY;
                }
                return res.toStack();
            }
            return ItemStack.EMPTY;
        }

        @Override
        public ItemStack useAmmoRandom() {
            var list = Lists.newArrayList(ammoStorage.nonEmptyViews());
            if(list.size() > 0) {
                var i = world.getRandom().nextInt(list.size());
                var view = list.get(i);
                var res = view.getResource();
                if(view.extract(res, 1, transactions.peek()) != 1) {
                    return ItemStack.EMPTY;
                }
                return res.toStack();
            }

            return ItemStack.EMPTY;
        }

        @Override
        public void handleLeftover(ItemStack stack) {
            var res = ItemVariant.of(stack);
            var transaction = transactions.peek();
            var inserted = ammoStorage.insert(res, stack.getCount(), transaction);
            var leftoverAmount = stack.getCount() - inserted;
            if(leftoverAmount > 0) {
                if(scatterStorage.insert(res, leftoverAmount, transaction) < leftoverAmount) {
                    throw new IllegalStateException("Cannot insert to scatter inventory");
                }
            }
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

            ItemScatterer.spawn(world, pos, scatterInventory);
            transactions.peek().close();
            transactions.clear();
        }

        @Override
        public void commit() {
            transactions.peek().commit();
        }
    }
}
