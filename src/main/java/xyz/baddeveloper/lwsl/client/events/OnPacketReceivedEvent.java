package xyz.baddeveloper.lwsl.client.events;

import xyz.baddeveloper.lwsl.client.SocketClient;
import xyz.baddeveloper.lwsl.packet.Packet;

public interface OnPacketReceivedEvent {

    void onPacketReceived(SocketClient socket, Packet packet);
}
