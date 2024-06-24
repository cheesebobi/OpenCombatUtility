package com.LubieKakao1212.opencu.fabric.block.entity;

import com.LubieKakao1212.opencu.OpenCUConfigCommon;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.fabric.inventory.SlottedInventory;
import com.LubieKakao1212.opencu.fabric.transaction.AmmoContext;
import com.LubieKakao1212.opencu.fabric.transaction.RebornEnergyContext;
import com.LubieKakao1212.opencu.fabric.transaction.ScopedContext;
import com.LubieKakao1212.opencu.fabric.transaction.SimpleLeftoverContext;
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
    protected ModularFrameContext getNewContext() {
        return new ModularFrameContext(
                new ScopedContext(),
                new RebornEnergyContext(energyStorage),
                new AmmoContext(ammoStorage),
                new SimpleLeftoverContext(ammoStorage, world, pos)
        );
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
    protected int getCurrentEnergy() {
        return (int)energyStorage.getAmount();
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
}
