import toPromise from './toPromise'

class UserRepository {
  constructor(client) {
    this.client = client
  }

  async getUserByUserId(user_id, fields = '*') {
    const query = `select ${fields} from user where id=${user_id}`
    const client = this.client
    const data = await toPromise(cb => {
      client.query(query, cb)
    })
    return data[0]
  }

  async like(liker_id, liked_id) {
    const query = `insert into user_likes (liker_user_id, liked_user_id) values (${liker_id},${liked_id})`
    const client = this.client
    const data = await toPromise(cb => {
      client.query(query, cb)
    })
    return data
  }
}

export default function(client) {
  return new UserRepository(client)
}
