package com.LubieKakao1212.opencu.gui.container;

import com.LubieKakao1212.opencu.block.entity.BlockEntityOmniDispenser;
import com.LubieKakao1212.opencu.init.CUMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class OmnidispenserMenu extends AbstractContainerMenu {

    private final int slotSize = 18;

    private final int playerSlotsStart;
    private final int slotCount;

    BlockEntityOmniDispenser blockEntity;

    public OmnidispenserMenu(int id, Inventory playerInventory, Level world, BlockPos pos) {
        super(CUMenu.OMNI_DISPENSER, id);

        this.blockEntity = (BlockEntityOmniDispenser) world.getBlockEntity(pos);
        IItemHandler dispenserInventory = blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).resolve().get();
        this.addSlot(new SlotItemHandler(dispenserInventory, 0, 43, 33));

        int startX = 80;
        int startY = 15;
        final int[] index = {1};

        AddSlotBlock(startX, startY, 3, 3, slotSize, (int x, int y) -> new SlotItemHandler(dispenserInventory, index[0]++, x, y));

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
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);

        ItemStack stack = ItemStack.EMPTY;

        if(slot.hasItem()) {
            ItemStack stack1 = slot.getItem();
            if (!stack1.isEmpty()) {
                stack = stack1.copy();
                //Is in dispenser inventory
                if (slot instanceof SlotItemHandler) {
                    if (!this.moveItemStackTo(stack1, playerSlotsStart, slotCount, false)) {
                        return ItemStack.EMPTY;
                    }

                    //slot.onSlotChange(stack1, stack);

                }else //Is in player inventory
                {
                    if(this.moveItemStackTo(stack1, 0, playerSlotsStart, false)) {
                        return ItemStack.EMPTY;
                    }
                }

                if (stack1.isEmpty())
                {
                    slot.set(ItemStack.EMPTY);
                }
                else
                {
                    slot.setChanged();
                }

                if (stack1.getCount() == stack.getCount())
                {
                    return ItemStack.EMPTY;
                }

                slot.onTake(player, stack1);
            }
        }
        return stack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return blockEntity.isUsableBy(pPlayer);
    }
}
