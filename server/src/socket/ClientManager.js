class ClientManager {
  constructor() {
    this.AllClient = {}
  }

  addClient(client) {
    this.AllClient[client.user.id] = client
  }

  removeClient(user_id) {
    delete this.AllClient[user_id]
  }

  getClient(user_id) {
    return this.AllClient[user_id]
  }

  getAllClient() {
    return this.AllClient
  }

  getCount() {
    return Object.keys(this.AllClient).length
  }

  static getInstance() {
    if (!ClientManager.instance) {
      ClientManager.instance = new ClientManager()
    }
    return ClientManager.instance
  }
}

export default ClientManager
