package xyz.baddeveloper.lwsl.server.events;

import xyz.baddeveloper.lwsl.packet.Packet;
import xyz.baddeveloper.lwsl.server.SocketHandler;

public interface OnPacketReceivedEvent {

    void onPacketReceived(SocketHandler socket, Packet packet);

}
