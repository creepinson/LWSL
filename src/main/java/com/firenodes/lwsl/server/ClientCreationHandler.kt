package com.firenodes.lwsl.server

interface ClientCreationHandler {
    /**
     * Handles the creation of clients, useful for setting custom IDs.
     * If you are using this, make sure to add the client to the clients map!
     */
    fun createClient(socketHandler: SocketHandler)
}
