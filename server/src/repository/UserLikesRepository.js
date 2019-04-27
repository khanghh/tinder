import toPromise from './toPromise'

class UserLikesRepository {
  constructor(client) {
    this.client = client
  }

  async addLike(liker_id, liked_id) {
    const query = `INSERT INTO user_likes(liker_user_id, liked_user_id) VALUES (${liker_id}, ${liked_id})`
    const client = this.client
    const data = await toPromise(cb => {
      client.query(query, cb)
    })
    return data
  }
}

export default function(client) {
  return new UserLikesRepository(client)
}
