import toPromise from './toPromise'

class MessageRepository {
  constructor(client) {
    this.client = client
  }

  getMessages(conversation_id, time, limit) {
    const query = `SELECT sender_id, message, UNIX_TIMESTAMP(created_at) as created_at FROM messages WHERE conversation_id=${conversation_id} and UNIX_TIMESTAMP(created_at) < ${time} order by created_at desc limit ${limit}`
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
