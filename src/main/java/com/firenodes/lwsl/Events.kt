package com.firenodes.lwsl

enum class Events(val id: String) {
    READY("ready"),
    CONNECT("connect"),
    DISCONNECT("disconnect"),
    PACKET_RECEIVED("packetReceived"),
    PACKET_SENT("packetSent");

    override fun toString(): String {
        return id
    }
}
