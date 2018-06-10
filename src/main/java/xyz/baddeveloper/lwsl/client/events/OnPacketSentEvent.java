package xyz.baddeveloper.lwsl.client.events;

import xyz.baddeveloper.lwsl.client.SocketClient;
import xyz.baddeveloper.lwsl.packet.Packet;
import xyz.baddeveloper.lwsl.server.SocketHandler;

public interface OnPacketSentEvent {

    void onPacketSent(SocketClient socketClient, Packet packet);
}
