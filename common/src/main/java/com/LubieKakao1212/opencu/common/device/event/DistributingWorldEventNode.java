package com.LubieKakao1212.opencu.common.device.event;

import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.device.event.data.IEventData;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class DistributingWorldEventNode implements IEventNode {

    private static final double maximumLinkDistance = 8;
    private static final double maximumLinkDistanceSq = maximumLinkDistance * maximumLinkDistance;

    private final Set<BlockPos> recipients = new HashSet<>();
    private World world;
    private final BlockPos pos;

    private boolean lock = false;

    public DistributingWorldEventNode(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public void handleEvent(IEventData data) {
        if(lock) {
            OpenCUModCommon.LOGGER.info("Encountered an event loop, skipping");
            return;
        }

        lock = true;

        validateRecipients();
        for(var pos : recipients) {
            var recipient = (IEventNode) world.getBlockEntity(pos);
            assert recipient != null;
            recipient.handleEvent(data);
        }
        lock = false;
    }

    public LinkResult toggleTarget(BlockPos pos) {
        if(recipients.contains(pos)) {
            removeTarget(pos);
            return LinkResult.SUCCESS_UNLINK;
        }
        else {
            return registerTarget(pos);
        }
    }


    public LinkResult registerTarget(BlockPos node) {
        if(pos.equals(node)) {
            return LinkResult.TARGET_SELF;
        }
        if(node.getSquaredDistance(pos) > maximumLinkDistanceSq) {
            return LinkResult.TOO_FAR;
        }
        if(world != null) {
            if(validateRecipient(node) == null) {
                return LinkResult.INVALID_TARGET;
            }
        }
        else {
            //We are deserializing and don't have access to world
            //Continue for now, validate later
        }
        recipients.add(node);
        return LinkResult.SUCCESS_LINK;
    }

    public void removeTarget(BlockPos node) {
        recipients.remove(node);
    }

    public void validateRecipients() {
        //we know p is in range since registerTarget
        recipients.removeIf((p) -> !(world.getBlockEntity(p) instanceof IEventNode));
    }

    public NbtList serialize() {
        var nbtOut = new NbtList();
        for (var pos : recipients)
        {
            nbtOut.add(NbtHelper.fromBlockPos(pos));
        }
        return nbtOut;
    }

    public void deserialize(NbtList nbtIn) {
        recipients.clear();
        for(int i=0; i<nbtIn.size(); i++) {
            registerTarget(NbtHelper.toBlockPos(nbtIn.getCompound(i)));
        }
    }

    public int recipientCount() {
        validateRecipients();
        return recipients.size();
    }

    public void setWorld(World world) {
        this.world = world;
    }

    @Nullable
    private IEventNode validateRecipient(BlockPos target) {
        var be = world.getBlockEntity(target);
        if (be instanceof IEventNode node) {
            return node;
        }
        return null;
    }

    public enum LinkResult {
        SUCCESS_LINK("message.opencu.link.success.link", true),
        SUCCESS_UNLINK("message.opencu.link.success.unlink", true),
        TOO_FAR("message.opencu.link.fail.distance", false),
        INVALID_TARGET("message.opencu.link.fail.target", false),
        TARGET_SELF("message.opencu.link.fail.target.self", false);

        private final String langKey;
        public final boolean isSuccess;

        LinkResult(String langKey, boolean isSuccess) {
            this.langKey = langKey;
            this.isSuccess = isSuccess;
        }

        public Text description(BlockPos target) {
            return Text.translatable(langKey, target.toShortString());
        }
    }

}
