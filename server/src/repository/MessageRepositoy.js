import toPromise from './toPromise'

class MessageRepository {
  constructor(client) {
    this.client = client
  }

  getMessages(conversation_id) {
    const query = `select * from messages where conversation_id=${conversation_id} order by created_at`
    const client = this.client
    return toPromise(cb => {
      client.query(query, cb)
    })
  }

  addMessage(conversation_id, sender_id, message) {
    const query = `INSERT INTO messages(conversation_id, sender_id, message, seen) VALUES (${conversation_id}, ${sender_id}, "${message}", 0)`
    const client = this.client
    return toPromise(cb => {
      client.query(query, cb)
    })
  }

  updateSeenMessage(message_id) {
    const query = `UPDATE messages SET seen=1, seen_at=now() WHERE id=${message_id}`
    const client = this.client
    return toPromise(cb => {
      client.query(query, cb)
    })
  }
}

export default function(client) {
  return new MessageRepository(client)
}
