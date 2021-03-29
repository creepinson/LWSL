package com.firenodes.lwsl.client.events

import com.firenodes.lwsl.client.SocketClient

open class ClientEvent(open val type: String, open val client: SocketClient)
