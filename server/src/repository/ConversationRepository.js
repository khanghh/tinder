import toPromise from './toPromise'

class ConversationRepository {
  constructor(client) {
    this.client = client
  }

  getConversationsByUserId(user_id) {
    const query = `select * from conversation where creator_id=${user_id} or member_id=${user_id}`
    const client = this.client
    return toPromise(cb => {
      client.query(query, cb)
    })
  }

  addConversation(creator_id, member_id) {
    const query = `INSERT INTO conversation(creator_id, member_id, is_deleted) VALUES (${creator_id}, ${member_id}, 0)`
    const client = this.client
    return toPromise(cb => {
      client.query(query, cb)
    })
  }

  checkHaveConversation(user_id, conv_id) {
    const query = `SELECT 1 FROM conversation WHERE id=${conv_id} and (user_id=${user_id} or member_id=${user_id})`
    const client = this.client
    return toPromise(cb => {
      client.query(query, cb)
    }).then(result => result.length > 0)
  }
}

export default function(client) {
  return new ConversationRepository(client)
}
