package xyz.baddeveloper.lwsl.server.events;

import xyz.baddeveloper.lwsl.packet.Packet;
import xyz.baddeveloper.lwsl.server.SocketHandler;

public interface OnPacketSentEvent {

    void onPacketSent(SocketHandler socketHandler, Packet packet);
}
