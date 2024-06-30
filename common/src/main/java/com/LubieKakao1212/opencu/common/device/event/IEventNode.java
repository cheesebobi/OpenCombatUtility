package com.LubieKakao1212.opencu.common.device.event;

import com.LubieKakao1212.opencu.common.device.event.data.IEventData;

public interface IEventNode {

    void handleEvent(IEventData data);

}
