package com.LubieKakao1212.opencu.common.device.event;

import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.device.event.data.IEventData;

import java.util.HashSet;
import java.util.Set;

public class DistributingEventNode implements IEventNode {

    private final Set<IEventNode> recipients = new HashSet<>();
    //private final Set<IEventData> handledEvents = new HashSet<>();

    private boolean lock = false;

    @Override
    public void handleEvent(IEventData data) {
        /*if(handledEvents.contains(data))
            return;
        handledEvents.add(data);*/

        if(lock) {
            OpenCUModCommon.LOGGER.info("Encountered an event loop, skipping");
            return;
        }

        lock = true;

        for(var target : recipients) {
            target.handleEvent(data);
        }

        lock = false;
    }

    public void registerTarget(IEventNode node) {
        recipients.add(node);
    }

    public void removeTarget(IEventNode node) {
        recipients.remove(node);
    }

    /*public void tick() {
        handledEvents.clear();
    }*/
}
