package com.LubieKakao1212.opencu.gui.container;

import com.LubieKakao1212.opencu.block.tileentity.TileEntityOmniDispenser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class OmnidispenserContainer extends Container {

    private final int slotSize = 18;

    private final int playerSlotsStart;
    private final int slotCount;

    TileEntityOmniDispenser tileEntity;

    public OmnidispenserContainer(TileEntityOmniDispenser te, InventoryPlayer playerInventory) {
        this.tileEntity = te;
        IItemHandler dispenserInventory = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        this.addSlotToContainer(new SlotItemHandler(dispenserInventory, 0, 43, 33));

        int startX = 80;
        int startY = 15;
        final int[] index = { 1 };

        AddSlotBlock(startX, startY, 3, 3, slotSize, (int x, int y) -> { return new SlotItemHandler(dispenserInventory, index[0]++, x, y);});

        playerSlotsStart = this.inventorySlots.size();

        index[0] = 0;

        SlotFactory playerSlotFactory = (int x, int y) -> {
            return new Slot(playerInventory, index[0]++, x, y);
        };

        //Hotbar
        AddSlotBlock(8, 142, 9, 1, slotSize, playerSlotFactory);

        //Main player Inventory
        AddSlotBlock(8, 84, 9, 3, slotSize, playerSlotFactory);

        this.slotCount = this.inventorySlots.size();
    }

    public void AddSlotBlock(int startX, int startY, int blockWidth, int blockHeight, int slotSize, SlotFactory slotFactory) {
            for(int y = 0; y < blockHeight; y++)
                for(int x = 0; x < blockWidth; x++) {
                    this.addSlotToContainer(slotFactory.get(startX + x * slotSize,  startY + y * slotSize));
                }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        Slot slot = this.inventorySlots.get(index);

        ItemStack stack = ItemStack.EMPTY;

        if(slot.getHasStack()) {
            ItemStack stack1 = slot.getStack();
            if (!stack1.isEmpty()) {
                stack = stack1.copy();
                //Is in dispenser inventory
                if (slot instanceof SlotItemHandler) {
                    if (!this.mergeItemStack(stack1, playerSlotsStart, slotCount, false)) {
                        return ItemStack.EMPTY;
                    }

                    //slot.onSlotChange(stack1, stack);

                }else //Is in player inventory
                {
                    if(!this.mergeItemStack(stack1, 0, playerSlotsStart, false)) {
                        return ItemStack.EMPTY;
                    }
                }

                if (stack1.isEmpty())
                {
                    slot.putStack(ItemStack.EMPTY);
                }
                else
                {
                    slot.onSlotChanged();
                }

                if (stack1.getCount() == stack.getCount())
                {
                    return ItemStack.EMPTY;
                }

                slot.onTake(playerIn, stack1);
            }
        }
        return stack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return tileEntity.isUsableBy(playerIn);
    }
}
