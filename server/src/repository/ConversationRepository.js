import toPromise from './toPromise'

class ConversationRepository {
  constructor(client) {
    this.client = client
  }

  async getConversationsByUserId(user_id) {
    const query = `select * from conversation where creator_id=${user_id} or member_id=${user_id}`
    const client = this.client
    return toPromise(cb => {
      client.query(query, cb)
    })
  }

  async addConversation(creator_id, member_id) {
    const query = `INSERT INTO conversation(creator_id, member_id, is_deleted) VALUES (${creator_id}, ${member_id}, 0)`
    const client = this.client
    return toPromise(cb => {
      client.query(query, cb)
    })
  }
}

export default function(client) {
  return new ConversationRepository(client)
}
