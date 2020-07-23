package xyz.baddeveloper.lwsl.packet

import org.json.JSONObject

class ClientIdentificationPacket(id: String) : Packet(JSONObject().put("id", id))