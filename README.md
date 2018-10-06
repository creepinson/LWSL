# Light Weight Socket Library  
[![CircleCI](https://circleci.com/gh/StijnSimons/LWSL/tree/master.svg?style=svg)](https://circleci.com/gh/StijnSimons/LWSL/tree/master) 
[![GitHub release](https://img.shields.io/github/release/StijnSimons/LWSL.svg)](https://github.com/StijnSimons/LWSL) [![Discord](https://img.shields.io/discord/452873518070366219.svg)](https://discord.gg/dmr73XH)


  

LWSL is an open source socket library made in Java using JSON packets.  
It is used to create small backend applications such as verification servers, rats or other simple tasks.  
Everyone can contribute by making pull requests.  
If you have any issues / new features, present them in the issues tab.  
For a faster support, feel free to join my discord [here](https://discordapp.com/invite/dmr73XH).

## Usage

### Example chat application
You can take a look at the example on [this repository (LWSLChatExample)](https://github.com/StijnSimons/LWSLChatExample).

### Server
To create a server using LWSL, take a look at the example below.  
```java
socketServer = new SocketServer(25566)
                .setMaxConnections(20) // 0 for infinite
                .addConnectEvent(socket -> System.out.println(String.format("Client connected! (%s)", socket.toString())))
                .addDisconnectEvent(socket -> System.out.println(String.format("Client disconnected! (%s)", socket.toString())))
                .addPacketReceivedEvent((socket, packet) -> System.out.println(String.format("Packet received! (%s)", packet.getObject().toString())))
                .addReadyEvent(socketServ -> System.out.println(String.format("Socket server is ready for connections! (%s)", socketServ.getServerSocket().toString())))
                .addPacketSentEvent(((socketHandler, packet) -> System.out.println(String.format("Packet sent! (%s)", packet.getObject().toString()))));
socketServer.start();
```

### Client
As for the client side, look at the following example.  
```java
SocketClient socketclient = new SocketClient("localhost", 25566)
                .addConnectEvent(onConnect -> System.out.println("Connected!"))
                .addDisconnectEvent(onDisconnect -> System.out.println("Disconnected!"))
                .addPacketReceivedEvent(((socket, packet) -> System.out.println(String.format("Received packet %s from %s.", packet.getObject().toString(), socket.getAddress()))))
                .addPacketSentEvent(((socketClient, packet) -> System.out.println(String.format("Sent packet %s to %s.", packet.getObject().toString(), socketClient.getAddress()))));
socketclient.connect();
```

### Packets
Both the SocketClient and SocketServer can send packets by using the #sendPacket(Packet) method.  
To make a custom packet, look at the example below.
```java
public class LoginPacket extends Packet {

    public LoginPacket(String username, String password){
        getObject().put("username", username);
        getObject().put("password", password);
    }
}
```
That's it! To send it to the server, use the following method.
```java
socketClient.sendPacket(new LoginPacket("Baddeveloper", "password123"));
```
Or for the client server side:
```java
socketHandler.sendPacket(new LoginPacket("Baddeveloper", "password123"));
```
You can handle these packets by making an "OnPacketReceived" event.
```java
.addPacketReceivedEvent(((socket, packet) -> {
    JSONObject object = packet.getObject();
        
    if(object.getString("username").equals("Baddeveloper") && object.getString("password").equals("password123"))
    	System.out.println("Logged in!");
})
```
### Controller

The controller can be used to kick all clients, kick a specific client, get a client by its IP, send packets to all connected clients, ...  
To get the controller's instance, use the following method.
```java
socketServer.getController();
```

Here are a couple examples of the controller's usage.
```java
final Controller controller = socketServer.getController();
        
controller.kickClient(controller.getClientByIP("127.0.0.1"));
        
controller.sendPacketToAll(new MessagePacket("Shutting down server!"));
controller.kickAll();
```

### SSL/TLS

You can create an encrypted socket connection by passing an `SSLContext` object to the `SocketClient` and `SocketServer` constructors:

```java
SocketServer socketServer = new SocketServer(25566, SSLContext.getDefault());
socketServer.start();
```

```java
SocketClient socketclient = new SocketClient("localhost", 25566, SSLContext.getDefault())
socketclient.connect();
```

To create an SSLContext, you can use the `SSLContextUtility` in conjunction with the `SSLContextConfig`. 

The `SSLContextConfig` contains several defaults, so for a standard `.jks` file, you only need to fill in the location (classpath or filesystem) and password. 

These defaults can be overwritten for different types of keystore/truststore, such as PKCS11.

Below is an example of using the `SSLContextUtility` to construct an `SSLContext` from an `SSLContextConfig`:

```java
final SSLContextConfig config = SSLContextConfig.builder()
        .keystoreLocation("keystore.jks") // This is on the classpath
        .keystorePassword("changeit")
        .truststoreLocation("/usr/local/truststore.jks") // This is on the filesystem
        .truststorePassword("changeit")
        .build();
final SSLContext sslContext = SSLContextUtility.getInstance().getSslContext(config);
```

## Installation
If your project is using Maven or Gradle, check the tutorials below.

### Maven

```xml
<repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>
```
```xml
<dependency>
	<groupId>com.github.StijnSimons</groupId>
	<artifactId>LWSL</artifactId>
	<version>VERSIONHERE</version>
</dependency>
```
Don't forget to fill in the version where "VERSIONHERE" is present.  
Latest version:  
[![GitHub release](https://img.shields.io/github/release/StijnSimons/LWSL.svg)](https://github.com/StijnSimons/LWSL)  

### Gradle

```gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
```gradle
dependencies {
	implementation 'com.github.StijnSimons:LWSL:VERSIONHERE'
}
```
Don't forget to fill in the version where "VERSIONHERE" is present.  
Latest version:  
[![GitHub release](https://img.shields.io/github/release/StijnSimons/LWSL.svg)](https://github.com/StijnSimons/LWSL)  

## Contributors
- [Baddeveloper (Stijn Simons)](https://github.com/StijnSimons)
- [arankin-irl (Alex Rankin)](https://github.com/arankin-irl)
