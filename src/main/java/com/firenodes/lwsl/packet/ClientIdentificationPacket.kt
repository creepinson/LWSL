package com.firenodes.lwsl.packet

import com.google.gson.JsonObject

class ClientIdentificationPacket(id: String) : Packet(JsonObject().apply {
    this.addProperty("id", id)
})
