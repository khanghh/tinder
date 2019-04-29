import toPromise from './toPromise'

class LikedRepository {
  constructor(client) {
    this.client = client
  }

  addLike(liker_id, liked_id) {
    const query = `INSERT INTO user_likes(liker_id, liked_id) VALUES (${liker_id}, ${liked_id})`
    const client = this.client
    return toPromise(cb => {
      client.query(query, cb)
    })
  }

  checkLike(liker_id, liked_id) {
    const query = `SELECT id FROM user_likes WHERE liker_id=${liker_id} and liked_id=${liked_id}`
    const client = this.client
    return toPromise(cb => {
      client.query(query, cb)
    }).then(result => result.length > 0)
  }
}

export default function(client) {
  return new LikedRepository(client)
}
