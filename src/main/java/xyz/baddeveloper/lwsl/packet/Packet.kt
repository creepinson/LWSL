/*
 * Copyright (c) Creepinson
 */
package xyz.baddeveloper.lwsl.packet

import org.json.JSONObject

open class Packet @JvmOverloads constructor(var jsonObject: JSONObject = JSONObject()) {
    fun isPacket(packet: Class<out Packet?>): Boolean {
        return isPacket(packet.simpleName.replace("Packet", ""))
    }

    fun isPacket(packetName: String): Boolean {
        return jsonObject.has("packetName") && jsonObject.getString("packetName") == packetName
    }

    var name: String?
        get() = jsonObject.getString("packetName")
        set(name) {
            jsonObject.put("packetName", name)
        }

}