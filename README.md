# Chat Server 💬
## About the application 📄
This Chat servver application  was written as an extension to a university assigned coursework, used to assess students knowledge of Sockets and Servers.

This app contains a Server, Client, Bot and DOD Game which can be played within the server if there is only one client connected.
### Chat Server 🖥️
The multi-threaded Server spawns threads to handle incoming Client requests in parallel. When the Server receives a message from a client, it broadcasts it to all connected client threads. In addition, the Server does not stop if one or more clients disconnect from it. It can shut down cleanly by the user entering the "EXIT" command on the terminal. Instructions on how to run the Server can be found below.
### Chat Client 💻
The Client, once connected, is capable of sending, and receiving messages from the Server. 
The Client supports reading input form the console and displaying all messages received by the server at the same time, by using a threaded approach. The Client can directly interact with the Server using some predefined commands ("BOT", "EXIT", "JOIN"). Instructions on how to run the Client can be found below.
### Chat Bot 🤖
The Bot functions like any other Client. Once started, the Bot connects to the Server and begins interacting with other Clients. Whenever the Bot receives a message, it will generate a response, and send it to the Server. 

## Instructions 📜
### Server
  - To start the Server, run the ChatServer Class.
  - You can use the -csp optional parameter to change the port that is used to listen for new Client connections.
    Example: java ChatServer -csp 14005. The default port is 14001.
  - To cleanly shut down the Server the clients can enter the "EXIT" command, which will inform all connected Clients, the
    Server is shutting down and then close the server.

### Client
  - The Client can be started by running the ChatClient Class.
  - You can use the -cca optional parameter to change the IP address the Client attempts to connect to.
    Example: java ChatClient -cca 192.168.10.250. The default address is localhost.
  -You can use the -ccp optional parameter to change the port the Client attempts to connect to.
    Example: java ChatClient -ccp 14005. The default port is 14001.
  - You can pass use the -cca and -ccp optional parameters together, in order to change the IP address and port.
    Example: java ChatClient -cca 192.168.10.250 -ccp 14005.

### Bot
  - The Bot can be started by running the ChatBot Class.
  - The Bot functions like any other Client, thus it supports the same optional parameters (-ccp and -cca).
  - Once the bot is connected to the Server, the bot will generate and send suitable responses to the server and therefore
    the clients.

### DOD
  - To start the DOD a Client must enter the "JOIN" command, while there is only one client in the room.
  - The rules:
  - You are being chased through a Dungeon and must avoid the Bot (represented by 'B') and pick up all the gold before you exit.
  - However the dungeon is dark and you can only see whats in your close proximity.
  - Try to avoid the Bot and collect all the gold before escaping.
  - The commands for the game are:
    - MOVE (N/S/E/W): Moves the player P Up/Down/Right/Left on the map
    - LOOK: Shows the the 5x5 view of the map
    - PICKUP: Picks up the Gold when you are stood on a G map position
    - HELLO: Shows how much gold you have left
    - QUIT: Leaves the Game and if all the gold is picked up says "YOU WIN" otherwise "LOSE"
