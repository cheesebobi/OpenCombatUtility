package com.LubieKakao1212.opencu.common.gui.container;

import com.LubieKakao1212.opencu.common.storage.IItemStorage;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.registry.CUMenu;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ModularFrameMenu extends ScreenHandler {

    private final int slotSize = 18;

    private final int playerSlotsStart;
    private final int slotCount;

    BlockEntityModularFrame blockEntity;

    public ModularFrameMenu(int id, PlayerInventory playerInventory, World world, BlockPos pos) {
        super(CUMenu.modularFrame(), id);

        this.blockEntity = (BlockEntityModularFrame) world.getBlockEntity(pos);
        assert blockEntity != null;
        IItemStorage dispenserInventory = blockEntity.getInventory();//blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).resolve().get();
        this.addSlot(new SlotItemStorage(dispenserInventory, 0, 43, 33));

        int startX = 80;
        int startY = 15;
        final int[] index = {1};

        AddSlotBlock(startX, startY, 3, 3, slotSize, (int x, int y) -> new SlotItemStorage(dispenserInventory, index[0]++, x, y));

        playerSlotsStart = this.slots.size();

        index[0] = 0;

        SlotFactory playerSlotFactory = (int x, int y) -> new Slot(playerInventory, index[0]++, x, y);

        //Hotbar
        AddSlotBlock(8, 142, 9, 1, slotSize, playerSlotFactory);

        //Main player Inventory
        AddSlotBlock(8, 84, 9, 3, slotSize, playerSlotFactory);

        this.slotCount = this.slots.size();
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
            ItemStack stack1 = slot.getStack();
            if (!stack1.isEmpty()) {
                stack = stack1.copy();
                //Is in dispenser inventory
                if (slot instanceof SlotItemStorage) {
                    if (!this.insertItem(stack1, playerSlotsStart, slotCount, false)) {
                        return ItemStack.EMPTY;
                    }

                    //slot.onSlotChange(stack1, stack);

                }else //Is in player inventory
                {
                    if(this.insertItem(stack1, 0, playerSlotsStart, false)) {
                        return ItemStack.EMPTY;
                    }
                }

                if (stack1.isEmpty())
                {
                    slot.setStackNoCallbacks(ItemStack.EMPTY);
                }
                else
                {
                    slot.markDirty();
                }

                if (stack1.getCount() == stack.getCount())
                {
                    return ItemStack.EMPTY;
                }

                slot.onTakeItem(player, stack1);
            }
        }
        return stack;
    }

    @Override
    public boolean canUse(PlayerEntity pPlayer) {
        return blockEntity.isUsableBy(pPlayer);
    }
}
