package com.LubieKakao1212.opencu.common.gui.container;

import com.LubieKakao1212.opencu.PlatformUtil;
import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.common.util.RedstoneControlType;
import com.LubieKakao1212.opencu.registry.CUBlocks;
import com.LubieKakao1212.opencu.registry.CUDispensers;
import com.LubieKakao1212.opencu.registry.CUMenu;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;

public class ModularFrameMenu extends ScreenHandler {

    private static final int deviceSlot = 0;
    private static final int ammoSlotStart = 1;
    private static final int ammoSlotCount = 9;
    private static final int ammoSlotEnd = ammoSlotStart + ammoSlotCount;

    private static final int dispenserSlotsStart = 0;
    private static final int dispenserSlotCount = 10;
    private static final int dispenserSlotsEnd = dispenserSlotsStart + dispenserSlotCount;

    private static final int playerSlotsStart = dispenserSlotsEnd;
    private static final int playerSlotCount = 36;
    private static final int playerSlotsEnd = playerSlotsStart + playerSlotCount;

    private static final int slotSize = 18;
    private final int slotCount;

    private final ScreenHandlerContext context;
    private final PropertyDelegate properties;

    public ModularFrameMenu(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, SlotProvider.dummy(dispenserSlotCount), ScreenHandlerContext.EMPTY, new ArrayPropertyDelegate(BlockEntityModularFrame.screenPropertyCount));
    }

    public ModularFrameMenu(int id, PlayerInventory playerInventory, SlotProvider modularFrameSlots, ScreenHandlerContext context, PropertyDelegate properties) {
        super(CUMenu.modularFrame(), id);

        this.addSlot(modularFrameSlots.createSlot( 0, 43, 33));

        this.context = context;

        int startX = 80;
        int startY = 15;
        final int[] index = {1};

        AddSlotBlock(startX, startY, 3, 3, slotSize, (int x, int y) -> modularFrameSlots.createSlot(index[0]++, x, y));

        index[0] = 0;

        SlotFactory playerSlotFactory = (int x, int y) -> new Slot(playerInventory, index[0]++, x, y);

        //Hotbar
        AddSlotBlock(8, 142, 9, 1, slotSize, playerSlotFactory);

        //Main player Inventory
        AddSlotBlock(8, 84, 9, 3, slotSize, playerSlotFactory);

        this.slotCount = this.slots.size();

        this.properties = properties;
        this.addProperties(properties);
    }

    public void AddSlotBlock(int startX, int startY, int blockWidth, int blockHeight, int slotSize, SlotFactory slotFactory) {
            for(int y = 0; y < blockHeight; y++)
                for(int x = 0; x < blockWidth; x++) {
                    this.addSlot(slotFactory.get(startX + x * slotSize,  startY + y * slotSize));
                }
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        Slot slot = this.slots.get(index);

        ItemStack stack = ItemStack.EMPTY;

        if(slot.hasStack()) {
            ItemStack stackCpy = slot.getStack();
            if (!stackCpy.isEmpty()) {
                stack = stackCpy.copy();
                //Is in dispenser inventory
                if (slot.id < dispenserSlotsEnd) {
                    if (!this.insertItem(stackCpy, playerSlotsStart, slotCount, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else //Is in player inventory
                {
                    var flag = false;
                    if(PlatformUtil.getDispenser(stackCpy) != null) {
                        var deviceSlotSlot = slots.get(deviceSlot);
                        var currentDevice = deviceSlotSlot.getStack();
                        if(currentDevice.isEmpty()) {
                            deviceSlotSlot.setStack(stackCpy.split(1));
                            flag = true;
                        }
                    }
                    if(!this.insertItem(stackCpy, ammoSlotStart, ammoSlotEnd, false) && !flag) {
                        return ItemStack.EMPTY;
                    }
                }

                if (stackCpy.isEmpty())
                {
                    slot.setStack(ItemStack.EMPTY);
                }
                else
                {
                    slot.markDirty();
                }

                if (stackCpy.getCount() == stack.getCount())
                {
                    OpenCUModCommon.LOGGER.warn("Impossible situation detected (Probably)");
                    return ItemStack.EMPTY;
                }

                slot.onTakeItem(player, stackCpy);
            }
        }
        return stack;
    }

    @Override
    public boolean canUse(PlayerEntity pPlayer) {
        return ScreenHandler.canUse(this.context, pPlayer, CUBlocks.modularFrame());
    }

    public boolean isRequiresLock() {
        return properties.get(BlockEntityModularFrame.requiresLockPropertyIndex) > 0;
    }

    public int getRedstoneControlTypeIndex() {
        return properties.get(BlockEntityModularFrame.redstoneControlPropertyIndex);
    }

    public int getEnergy() {
        return properties.get(BlockEntityModularFrame.energyPropertyIndex);
    }

    public int getMaxEnergy() {
        return properties.get(BlockEntityModularFrame.maxEnergyPropertyIndex);
    }

    public float getEnergyRatio() {
        return (float) getEnergy() / (float) getMaxEnergy();
    }

    public BlockPos targetPosition() {
        return new BlockPos(
                properties.get(BlockEntityModularFrame.xPropertyIndex),
                properties.get(BlockEntityModularFrame.yPropertyIndex),
                properties.get(BlockEntityModularFrame.zPropertyIndex)
        );
    }
}
