import toPromise from './toPromise'

class ConversationRepository {
  constructor(client) {
    this.client = client
  }

  async getConversations(user_id, fields = '*') {
    const query = `select ${fields} from conversation where creator_id=${user_id} or member_id=${user_id}`
    const client = this.client
    const data = await toPromise(cb => {
      client.query(query, cb)
    })
    return data
  }
}

export default function(client) {
  return new ConversationRepository(client)
}
