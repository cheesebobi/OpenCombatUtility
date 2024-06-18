package com.LubieKakao1212.opencu.common.device.event;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Objects;

public class BlockPosEventNode implements IEventNode {

    private BlockPos pos;
    private World world;

    @Nullable
    private BlockEntityType<? extends IEventNode> type;

    public BlockPosEventNode(BlockPos pos, World world) {
        this(pos, world, null);
    }

    public BlockPosEventNode(BlockPos pos, World world, @Nullable BlockEntityType<? extends IEventNode> type) {
        this.pos = pos;
        this.world = world;
        this.type = type;
    }

    @Override
    public void handleEvent(IEventData data) {
        IEventNode node = null;

        if(type != null) {
            node = type.get(world, pos);
        }
        else {
            var be = world.getBlockEntity(pos);
            if(be instanceof IEventNode node1) {
                node = node1;
            }
        }

        if(node != null) {
            node.handleEvent(data);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, pos, type);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BlockPosEventNode node && node.world == world && node.pos.equals(pos) && node.type == type;
    }
}
