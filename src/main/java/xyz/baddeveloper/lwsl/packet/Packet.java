package xyz.baddeveloper.lwsl.packet;

import org.json.JSONObject;

public class Packet {

    private JSONObject object;

    public Packet(){}

    public Packet(JSONObject object) { this.object = object; }

    public JSONObject getObject() { return object; }

    public void setObject(JSONObject object) { this.object = object; }
}
