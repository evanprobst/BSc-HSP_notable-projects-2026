#CS3357 Assignment 3 Chatroom
#eprobst2

import socket
import threading
import select

#----------------------------------------------------------------------------
# TCP Chatroom
#----------------------------------------------------------------------------
# Server
class ServerTCP:
    def __init__(self, server_port):
        try:
            # Store server port
            self.server_port = server_port

            # Init server socket
            self.server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            # Allow re-use of the address
            self.server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)

            # Bind to local machine IP on given port
            addr = socket.gethostbyname(socket.gethostname())
            self.server_socket.bind((addr, self.server_port))

            # Listen for incoming connections
            self.server_socket.listen(5)

            # Dictionary mapping client_socket -> client_name
            self.clients = {}

            # Events to control server state
            self.run_event = threading.Event()
            self.handle_event = threading.Event()
        except Exception:
            raise SystemExit(1)

    #------------------------------------------------------------------------
    def accept_client(self):
        try:
            # Use select to check if there is a pending connection
            rlist, _, _ = select.select([self.server_socket], [], [], 1.0)
            if not rlist:
                return False

            client_socket, _ = self.server_socket.accept()

            # Receive client's name
            data = client_socket.recv(1024)
            if not data:
                client_socket.close()
                return False

            name = data.decode().strip()

            # Name already used
            if name in self.clients.values():
                try:
                    client_socket.sendall(b"Name already taken")
                except Exception:
                    pass
                client_socket.close()
                return False

            # Add client and send welcome
            self.clients[client_socket] = name
            try:
                client_socket.sendall(b"Welcome")
            except Exception:
                # If we cannot send welcome, close client
                self.close_client(client_socket)
                return False

            # Broadcast join message
            self.broadcast(client_socket, "join")

            # Start handler thread for this client
            t = threading.Thread(target=self.handle_client, args=(client_socket,))

            t.start()

            return True
        except Exception:
            raise SystemExit(1)

    #------------------------------------------------------------------------
    def close_client(self, client_socket):
        try:
            # Close socket
            if client_socket in self.clients:
                try:
                    client_socket.close()
                except Exception:
                    pass
                # Remove from dictionary
                del self.clients[client_socket]
                return True
            return False
        except Exception:
            raise SystemExit(1)

    #------------------------------------------------------------------------
    def broadcast(self, client_socket_sent, message):
        try:
            if client_socket_sent not in self.clients:
                return

            name = self.clients[client_socket_sent]

            # Build broadcast message
            if message == "join":
                out_msg = f"User {name} joined"
            elif message == "exit":
                out_msg = f"User {name} left"
            else:
                out_msg = f"{name}: {message}"

            data = out_msg.encode()

            # Send to all other clients
            for sock in list(self.clients.keys()):
                if sock is client_socket_sent:
                    continue
                try:
                    sock.sendall(data)
                except Exception:
                    # On send error, close that client
                    self.close_client(sock)
        except Exception:
            raise SystemExit(1)

    #------------------------------------------------------------------------
    def shutdown(self):
        try:
            # Stop events so loops can exit
            self.run_event.set()
            self.handle_event.set()

            # Inform all clients and close sockets
            for sock in list(self.clients.keys()):
                try:
                    sock.sendall(b"server-shutdown")
                except Exception:
                    pass
                self.close_client(sock)

            # Close server socket
            try:
                self.server_socket.close()
            except Exception:
                pass
        except Exception:
            raise SystemExit(1)

    #------------------------------------------------------------------------
    def get_clients_number(self):
        try:
            return len(self.clients)
        except Exception:
            raise SystemExit(1)

    #------------------------------------------------------------------------
    def handle_client(self, client_socket):
        try:
            while not self.handle_event.is_set() and client_socket in self.clients:
                # Use select to check if there is a pending connection
                rlist, _, _ = select.select([client_socket], [], [], 1.0)
                if not rlist:
                    continue

                data = client_socket.recv(1024)
                if not data:
                    self.close_client(client_socket)
                    break

                text = data.decode().strip()
                if text == "exit":
                    # Notify others and close client
                    self.broadcast(client_socket, "exit")
                    self.close_client(client_socket)
                    break

                # Broadcast normal message
                self.broadcast(client_socket, text)
        except Exception:
            # On any error, close client
            try:
                self.close_client(client_socket)
            except Exception:
                pass
            raise SystemExit(1)

    #------------------------------------------------------------------------
    def run(self):
        try:
            while not self.run_event.is_set():
                try:
                    self.accept_client()
                except SystemExit:
                    raise
                except Exception:
                    continue
        except KeyboardInterrupt:
            self.shutdown()
        except Exception:
            self.shutdown()
            raise SystemExit(1)

#----------------------------------------------------------------------------
# Client
class ClientTCP:
    def __init__(self, client_name, server_port):
        try:
            self.client_name = client_name
            self.server_port = server_port

            # Determine server address (local machine)
            self.server_addr = socket.gethostbyname(socket.gethostname())

            # Create TCP socket
            self.client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

            # Events controlling loops
            self.exit_run = threading.Event()
            self.exit_receive = threading.Event()
        except Exception:
            raise SystemExit(1)

    #------------------------------------------------------------------------
    def connect_server(self):
        try:
            # Connect to server
            self.client_socket.connect((self.server_addr, self.server_port))

            # Send client name
            try:
                self.client_socket.sendall(self.client_name.encode())
            except Exception:
                return False

            # Wait for server reply
            data = self.client_socket.recv(1024)
            if not data:
                return False

            msg = data.decode()
            if "Welcome" in msg:
                return True

            return False
        except Exception:
            return False

    #------------------------------------------------------------------------
    def send(self, text):
        try:
            data = text.encode()
            self.client_socket.sendall(data)
        except Exception:
            raise SystemExit(1)

    #------------------------------------------------------------------------
    def receive(self):
        try:
            while not self.exit_receive.is_set():
                # Use select to check if there is a pending connection
                rlist, _, _ = select.select([self.client_socket], [], [], 1.0)
                if not rlist:
                    continue

                data = self.client_socket.recv(1024)
                if not data:
                    # Connection closed
                    self.exit_run.set()
                    self.exit_receive.set()
                    break

                msg = data.decode().strip()
                if msg == "server-shutdown":
                    # Server is shutting down
                    print("Server is shutting down.")
                    self.exit_run.set()
                    self.exit_receive.set()
                    break

                # Print any other message
                print(msg)
        except Exception:
            # Fatal error in receive loop
            self.exit_run.set()
            self.exit_receive.set()
            raise SystemExit(1)

    #------------------------------------------------------------------------
    def run(self):
        try:
            # Connect first
            if not self.connect_server():
                print("Could not connect to server.")
                return

            # Start thread for receiving messages
            t = threading.Thread(target=self.receive)

            t.start()

            # Main loop for user input
            while not self.exit_run.is_set():
                try:
                    text = input()
                except EOFError:
                    text = "exit"
                except KeyboardInterrupt:
                    text = "exit"

                if text.strip().lower() == "exit":
                    try:
                        self.send("exit")
                    except Exception:
                        pass
                    self.exit_run.set()
                    self.exit_receive.set()
                    break

                # Normal message
                self.send(text)
        except Exception:
            raise SystemExit(1)
        finally:
            try:
                self.client_socket.close()
            except Exception:
                pass

#----------------------------------------------------------------------------
# UDP Chatroom
#----------------------------------------------------------------------------
# Server
class ServerUDP:
    def __init__(self, server_port):
        try:
            self.server_port = server_port

            # UDP server socket
            self.server_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

            addr = socket.gethostbyname(socket.gethostname())
            self.server_socket.bind((addr, self.server_port))

            # Map client_addr -> client_name
            self.clients = {}

            # List of (client_addr, message) tuples
            self.messages = []
        except Exception:
            raise SystemExit(1)

    #------------------------------------------------------------------------
    def accept_client(self, client_addr, message):
        try:
            # Extract name from message
            msg = message.strip()
            if ": " in msg:
                name, _ = msg.split(": ", 1)
            else:
                name = msg

            # Check if name already used
            if name in self.clients.values():
                try:
                    self.server_socket.sendto(b"Name already taken", client_addr)
                except Exception:
                    pass
                return False

            # Add client and send welcome
            self.clients[client_addr] = name
            try:
                self.server_socket.sendto(b"Welcome", client_addr)
            except Exception:
                # If we cannot send welcome, remove client
                del self.clients[client_addr]
                return False

            # Prepare join message
            joined = f"User {name} joined"
            self.messages.append((client_addr, joined))
            self.broadcast()
            return True
        except Exception:
            raise SystemExit(1)

    #------------------------------------------------------------------------
    def close_client(self, client_addr):
        try:
            # Remove client from dictionary
            if client_addr in self.clients:
                name = self.clients[client_addr]
                del self.clients[client_addr]

                # Prepare left message
                left = f"User {name} left"
                self.messages.append((client_addr, left))
                self.broadcast()
                return True
            return False
        except Exception:
            raise SystemExit(1)

    #------------------------------------------------------------------------
    def broadcast(self):
        try:
            if not self.messages:
                return

            # Take most recent message
            sender_addr, msg = self.messages[-1]
            data = msg.encode()

            for addr in list(self.clients.keys()):
                if addr == sender_addr:
                    continue
                try:
                    self.server_socket.sendto(data, addr)
                except Exception:
                    # Ignore errors for UDP
                    pass
        except Exception:
            raise SystemExit(1)

    #------------------------------------------------------------------------
    def shutdown(self):
        try:
            # Notify all clients that server is shutting down
            for addr in list(self.clients.keys()):
                try:
                    self.server_socket.sendto(b"server-shutdown", addr)
                except Exception:
                    pass
                try:
                    self.close_client(addr)
                except Exception:
                    pass
            try:
                self.server_socket.close()
            except Exception:
                pass
        except Exception:
            raise SystemExit(1)

    #------------------------------------------------------------------------
    def get_clients_number(self):
        try:
            return len(self.clients)
        except Exception:
            raise SystemExit(1)

    #------------------------------------------------------------------------
    def run(self):
        try:
            while True:
                try:
                    data, client_addr = self.server_socket.recvfrom(4096)
                except KeyboardInterrupt:
                    break
                except Exception:
                    # Any socket error is fatal
                    raise
                if not data:
                    continue
                text = data.decode().strip()

                # Expect format "name: command" or similar
                if ": " in text:
                    name_part, msg_part = text.split(": ", 1)
                else:
                    name_part = ""
                    msg_part = text

                if msg_part == "join":
                    # New client trying to join
                    self.accept_client(client_addr, text)
                elif msg_part == "exit":
                    # Existing client leaving
                    if client_addr in self.clients:
                        self.close_client(client_addr)
                else:
                    # Normal message from existing client
                    if client_addr in self.clients:
                        full_msg = f"{name_part}: {msg_part}" if name_part else msg_part
                        self.messages.append((client_addr, full_msg))
                        self.broadcast()
        except KeyboardInterrupt:
            # Ctrl+C
            self.shutdown()
        except Exception:
            self.shutdown()
            raise SystemExit(1)

#----------------------------------------------------------------------------
# Client
class ClientUDP:
    def __init__(self, client_name, server_port):
        try:
            self.client_name = client_name
            self.server_port = server_port

            # Server address (local machine)
            self.server_addr = socket.gethostbyname(socket.gethostname())

            # UDP socket
            self.client_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

            # Events controlling loops
            self.exit_run = threading.Event()
            self.exit_receive = threading.Event()
        except Exception:
            raise SystemExit(1)

    #------------------------------------------------------------------------
    def connect_server(self):
        try:
            # Send join request
            self.send("join")

            # Wait for response
            self.client_socket.settimeout(2.0)
            try:
                data, _ = self.client_socket.recvfrom(4096)
            except socket.timeout:
                return False
            finally:
                self.client_socket.settimeout(None)

            if not data:
                return False
            # Success
            msg = data.decode()
            if "Welcome" in msg:
                return True
            return False
        except Exception:
            return False

    #------------------------------------------------------------------------
    def send(self, text):
        try:
            msg = f"{self.client_name}: {text}"
            data = msg.encode()
            self.client_socket.sendto(data, (self.server_addr, self.server_port))
        except Exception:
            raise SystemExit(1)

    #------------------------------------------------------------------------
    def receive(self):
        try:
            # Until the exit_receive event is set
            while not self.exit_receive.is_set():
                # Use select to check if there is a pending connection
                rlist, _, _ = select.select([self.client_socket], [], [], 1.0)
                if not rlist:
                    continue

                data, _ = self.client_socket.recvfrom(4096)
                if not data:
                    continue

                msg = data.decode().strip()
                # Shut down
                if msg == "server-shutdown":
                    print("Server is shutting down.")
                    self.exit_run.set()
                    self.exit_receive.set()
                    break

                print(msg)
        except Exception:
            self.exit_run.set()
            self.exit_receive.set()
            raise SystemExit(1)

    #------------------------------------------------------------------------
    def run(self):
        try:
            if not self.connect_server():
                print("Could not connect to server.")
                return

            # Start thread for receiving messages
            t = threading.Thread(target=self.receive)

            t.start()

            # Main loop for input
            while not self.exit_run.is_set():
                try:
                    text = input()
                except EOFError:
                    text = "exit"
                except KeyboardInterrupt:
                    text = "exit"

                if text.strip().lower() == "exit":
                    try:
                        self.send("exit")
                    except Exception:
                        pass
                    self.exit_run.set()
                    self.exit_receive.set()
                    break
                # Normal message
                self.send(text)
        except Exception:
            raise SystemExit(1)
        finally:
            try:
                self.client_socket.close()
            except Exception:
                pass
