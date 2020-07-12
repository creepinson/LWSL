/*
 * Copyright (c) Creepinson
 */

package xyz.baddeveloper.lwsl;

import org.json.JSONObject;

public class Packet {

    private JSONObject object;

    public Packet() {
        this(new JSONObject());
    }

    public Packet(JSONObject object) {
        this.object = object;
    }

    public boolean isPacket(Class<? extends Packet> packet) {
        return isPacket(packet.getSimpleName().replace("Packet", ""));
    }

    public boolean isPacket(String packetName) {
        return this.object.has("packetName") && this.object.getString("packetName").equals(packetName);
    }

    public JSONObject getObject() {
        return object;
    }

    public void setObject(JSONObject object) {
        this.object = object;
    }

    public String getName() {
        return this.object.getString("packetName");
    }

    public void setName(String name) {
        this.object.put("packetName", name);
    }
}
