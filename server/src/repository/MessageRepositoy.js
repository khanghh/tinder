import toPromise from './toPromise'

class MessageRepository {
  constructor(client) {
    this.client = client
  }

  async getMessages(conversation_id, fields = '*') {
    const query = `select ${fields} from messages where conversation_id=${conversation_id} order by created_at`
    const client = this.client
    const data = await toPromise(cb => {
      client.query(query, cb)
    })
    return data
  }
}

export default function(client) {
  return new MessageRepository(client)
}
