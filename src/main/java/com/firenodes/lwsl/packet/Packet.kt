package com.firenodes.lwsl.packet

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import reactor.core.publisher.Flux
import reactor.netty.Connection

open class Packet @JvmOverloads constructor(var jsonObject: JsonObject = JsonObject()) {
    infix fun Packet.isOf(packet: Class<out Packet?>): Boolean {
        return isOf(packet.simpleName.replace("Packet", ""))
    }

    infix fun Packet.isOf(packetName: String): Boolean {
        return jsonObject.has("packetName") && jsonObject.get("packetName").asString == packetName
    }

    var name: String?
        get() = jsonObject.get("packetName").asString
        set(name) {
            jsonObject.addProperty("packetName", name)
        }


    companion object {
        fun parseInbound(conn: Connection): Flux<Packet> = conn.inbound().receive().map {
            it.toString()
        }.map {
            Packet(JsonParser.parseString(it).asJsonObject)
        }
    }
}
