package com.LubieKakao1212.opencu.common.device;

import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public abstract class ShotMappings {

    public static final PostShootAction DEFAULT_SHOOT_ACTION = (entity, forward, velocity) -> { };

    public static final PostSpawnAction DEFAULT_SPAWN_ACTION = (entity, forward, velocity) -> { };

    public static final ShotEntry ITEM_ENTRY = new ShotEntry((stack, level) -> {

        ItemEntity item = new ItemEntity(level, 0, 0 ,0, stack);
        return item;
    }, ItemStack.EMPTY, 1., 1., 1.);

    /**
     * Experimental
     */
    public static final ShotEntry BLOCK_ENTRY = new ShotEntry((stack, world) -> {
        //TODO make fake player place items
        BlockItem blockItem = (BlockItem)stack.getItem();
        Block block = blockItem.getBlock();
        FallingBlockEntity sand = null;//new FallingBlockEntity(world, 0, 0, 0, null);
        return sand;
    }, ItemStack.EMPTY, 1., 0.1, 1.);

    private final HashMap<Item, ShotEntry> mappings = new HashMap<>();

    private final List<MappingRedirection> redirections = new ArrayList<>();

    public void register(Item item, ShotEntry mapping) {
        if(mappings.put(item, mapping) != null) {
            OpenCUModCommon.LOGGER.warn("Assigned dispenser mapping for same item twice, previous one will be overwritten");
        }
    }

    public void register(MappingRedirection predicate) {
        redirections.add(predicate);
    }

    public ShotEntry getShotResult(ItemStack stack) {
        final Item[] item = {stack.getItem()};
        for(MappingRedirection redirector : redirections) {
            redirector.get(stack).ifPresent((i) -> item[0] = i);
        }
        return mappings.getOrDefault(item[0], getDefaultEntry());
    }

    public abstract void init();

    protected ShotEntry getDefaultEntry() {
        return ITEM_ENTRY;
    }

    @FunctionalInterface
    public interface EntityMapping {
        Entity get(ItemStack stack, World Level);
    }

    @FunctionalInterface
    public interface PostShootAction {
        void Execute(Entity entity, Vector3d forward, double velocity);
    }

    @FunctionalInterface
    public interface PostSpawnAction {
        void Execute(Entity entity, Vector3d forward, double velocity);
    }

    @FunctionalInterface
    public interface MappingRedirection {
        Optional<Item> get(ItemStack stack);

    }

}
