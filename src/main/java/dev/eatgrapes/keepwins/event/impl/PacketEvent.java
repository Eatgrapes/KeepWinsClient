package dev.eatgrapes.keepwins.event.impl;

import dev.eatgrapes.keepwins.event.api.CancellableEvent;
import net.minecraft.network.Packet;

/**
 * @author ChengFeng
 * @since 2024/8/7
 **/
public class PacketEvent extends CancellableEvent {
    public Packet<?> packet;

    public PacketEvent(Packet<?> packet) {
        this.packet = packet;
    }
}
