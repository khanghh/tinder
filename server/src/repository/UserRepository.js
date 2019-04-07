import toPromise from './toPromise'

class UserRepository {
  constructor(client) {
    this.client = client
  }

  async getUserByUserId(userid, fields = '*') {
    const query = `SELECT ${fields} FROM user WHERE id=${userid}`
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

  async update(info) {
    const query = 'update'
    const client = this.client
  }
}

export default function(client) {
  return new UserRepository(client)
}
