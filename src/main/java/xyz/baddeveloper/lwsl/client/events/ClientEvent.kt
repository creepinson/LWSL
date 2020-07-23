package xyz.baddeveloper.lwsl.client.events

import xyz.baddeveloper.lwsl.client.SocketClient

open class ClientEvent(open val type: String, open val client: SocketClient)