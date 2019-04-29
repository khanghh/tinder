class Client {
  constructor(socket, user) {
    this.user = user
    this.socket = socket
    this.ip = socket.handshake.address
    this.all_conv = {} // { "conv_id": "friend_id"}
  }
}

export { Client as default }
